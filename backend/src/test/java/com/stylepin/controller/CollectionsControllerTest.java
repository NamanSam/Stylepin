package com.stylepin.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylepin.dto.*;
import com.stylepin.service.AuthService;
import com.stylepin.repository.OutfitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class CollectionsControllerTest {
    @Autowired MockMvc mvc;
    @Autowired AuthService auth;
    @Autowired OutfitRepository outfits;
    @Autowired ObjectMapper json;
    String token(){
        String u="u"+UUID.randomUUID().toString().replace("-","").substring(0,20);
        auth.register(new RegisterRequestDTO(u,u+"@example.com","long secure password"));
        return "Bearer "+auth.login(new LoginRequestDTO(u+"@example.com","long secure password"),null).body().accessToken();
    }
    @Test void concurrentSavesRemainIdempotent() throws Exception {
        String authorization=token(); Long id=outfits.findAll().getFirst().getId();
        var executor=java.util.concurrent.Executors.newFixedThreadPool(2);
        var start=new java.util.concurrent.CountDownLatch(1);
        try {
            java.util.concurrent.Callable<Integer> task=()->{
                start.await();
                return mvc.perform(post("/api/users/me/saved-outfits/"+id).header("Authorization",authorization)).andReturn().getResponse().getStatus();
            };
            var a=executor.submit(task); var b=executor.submit(task); start.countDown();
            assertEquals(204,a.get(10,java.util.concurrent.TimeUnit.SECONDS));
            assertEquals(204,b.get(10,java.util.concurrent.TimeUnit.SECONDS));
            mvc.perform(get("/api/users/me/saved-outfits").header("Authorization",authorization)).andExpect(jsonPath("$.totalElements").value(1));
        } finally { executor.shutdownNow(); }
    }
    @Test void savesAreIdempotentPrivateAndPaginated() throws Exception {
        String a=token(), b=token(); Long id=outfits.findAll().getFirst().getId();
        mvc.perform(post("/api/users/me/saved-outfits/"+id)).andExpect(status().isUnauthorized());
        for(int i=0;i<2;i++)mvc.perform(post("/api/users/me/saved-outfits/"+id).header("Authorization",a)).andExpect(status().isNoContent());
        mvc.perform(get("/api/users/me/saved-outfits").header("Authorization",a))
            .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.items[0].products").isArray());
        mvc.perform(get("/api/users/me/saved-outfits").header("Authorization",b)).andExpect(jsonPath("$.totalElements").value(0));
        for(int i=0;i<2;i++)mvc.perform(delete("/api/users/me/saved-outfits/"+id).header("Authorization",a)).andExpect(status().isNoContent());
        mvc.perform(post("/api/users/me/saved-outfits/99999999").header("Authorization",a)).andExpect(status().isNotFound());
        mvc.perform(get("/api/users/me/saved-outfits?size=1000").header("Authorization",a)).andExpect(status().isBadRequest());
        assertTrue(outfits.existsById(id));
    }
    @Test void boardsEnforceOwnershipAndPreserveOutfits() throws Exception {
        String a=token(),b=token(); Long outfit=outfits.findAll().getFirst().getId();
        var created=mvc.perform(post("/api/boards").header("Authorization",a).contentType("application/json")
            .content("{\"name\":\"Winter Looks\",\"description\":\"My collection\",\"userId\":999}"))
            .andExpect(status().isCreated()).andReturn();
        long board=json.readTree(created.getResponse().getContentAsString()).get("id").asLong();
        String path="/api/boards/"+board;
        mvc.perform(get(path)).andExpect(status().isUnauthorized());
        mvc.perform(get(path).header("Authorization",b)).andExpect(status().isForbidden());
        mvc.perform(post(path+"/outfits/"+outfit).header("Authorization",b)).andExpect(status().isForbidden());
        mvc.perform(delete(path+"/outfits/"+outfit).header("Authorization",b)).andExpect(status().isForbidden());
        mvc.perform(delete(path).header("Authorization",b)).andExpect(status().isForbidden());
        mvc.perform(get("/api/boards").header("Authorization",b)).andExpect(jsonPath("$.totalElements").value(0));
        for(int i=0;i<2;i++)mvc.perform(post(path+"/outfits/"+outfit).header("Authorization",a)).andExpect(status().isNoContent());
        mvc.perform(get(path).header("Authorization",a)).andExpect(jsonPath("$.outfits.totalElements").value(1));
        for(int i=0;i<2;i++)mvc.perform(delete(path+"/outfits/"+outfit).header("Authorization",a)).andExpect(status().isNoContent());
        mvc.perform(get(path).header("Authorization",a)).andExpect(jsonPath("$.outfits.totalElements").value(0));
        mvc.perform(post(path+"/outfits/"+outfit).header("Authorization",a)).andExpect(status().isNoContent());
        mvc.perform(delete(path).header("Authorization",a)).andExpect(status().isNoContent());
        assertTrue(outfits.existsById(outfit));
        mvc.perform(get(path).header("Authorization",a)).andExpect(status().isNotFound());
        mvc.perform(post("/api/boards").header("Authorization",a).contentType("application/json").content("{\"name\":\" \"}"))
            .andExpect(status().isBadRequest());
    }
}
