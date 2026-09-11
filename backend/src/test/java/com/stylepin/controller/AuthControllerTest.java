package com.stylepin.controller;

import com.fasterxml.jackson.databind.*;
import com.stylepin.repository.*;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AuthControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserRepository users;
    String name() { return "u" + UUID.randomUUID().toString().replace("-", "").substring(0,20); }
    String registration(String username, String email) throws Exception {
        return json.writeValueAsString(Map.of("username",username,"email",email,"password","a secure password 123"));
    }
    MvcResult register(String username) throws Exception {
        return mvc.perform(post("/api/auth/register").with(csrf()).contentType("application/json")
            .content(registration(username,username+"@example.com"))).andExpect(status().isCreated()).andReturn();
    }
    MvcResult login(String username) throws Exception {
        return mvc.perform(post("/api/auth/login").with(csrf()).contentType("application/json")
            .content(json.writeValueAsString(Map.of("email",username+"@example.com","password","a secure password 123"))))
            .andExpect(status().isOk()).andExpect(jsonPath("$.passwordHash").doesNotExist())
            .andExpect(jsonPath("$.user.passwordHash").doesNotExist()).andReturn();
    }
    String access(MvcResult result) throws Exception { return json.readTree(result.getResponse().getContentAsString()).get("accessToken").asText(); }
    Cookie refresh(MvcResult result) { return result.getResponse().getCookie("stylepin_refresh"); }
    @Test void registrationValidationAndDuplicates() throws Exception {
        String u = name(); register(u);
        assertTrue(users.findByEmail(u+"@example.com").orElseThrow().getPasswordHash().startsWith("$2"));
        mvc.perform(post("/api/auth/register").with(csrf()).contentType("application/json").content(registration(name(),u+"@example.com")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/auth/register").with(csrf()).contentType("application/json").content(registration(u.toUpperCase(),name()+"@example.com")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/auth/register").with(csrf()).contentType("application/json").content("{}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fieldErrors.password").exists());
        mvc.perform(post("/api/auth/register").with(csrf()).contentType("application/json")
            .content(json.writeValueAsString(Map.of("username",name(),"email","valid@example.com","password","é".repeat(40)))))
            .andExpect(status().isBadRequest());
    }
    @Test void loginAndPrivateIdentity() throws Exception {
        String u = name(); register(u); var result = login(u);
        mvc.perform(get("/api/users/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users/me").header("Authorization","Bearer "+access(result)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.username").value(u));
        mvc.perform(get("/api/users/me").header("Authorization","Bearer malformed"))
            .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").with(csrf()).contentType("application/json")
            .content("{\"email\":\"missing@example.com\",\"password\":\"wrong password\"}"))
            .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/outfits")).andExpect(status().isOk());
        assertTrue(refresh(result).isHttpOnly());
        assertEquals("/api/auth",refresh(result).getPath());
        assertTrue(result.getResponse().getHeader("Set-Cookie").contains("SameSite=Strict"));
    }
    @Test void rotationReplayRevokesEntireSession() throws Exception {
        String u = name(); register(u); var first = login(u);
        var second = mvc.perform(post("/api/auth/refresh").with(csrf()).cookie(refresh(first)))
            .andExpect(status().isOk()).andReturn();
        assertNotEquals(refresh(first).getValue(),refresh(second).getValue());
        mvc.perform(post("/api/auth/refresh").with(csrf()).cookie(refresh(first))).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/refresh").with(csrf()).cookie(refresh(second))).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users/me").header("Authorization","Bearer "+access(second))).andExpect(status().isUnauthorized());
    }
    @Test void logoutRevokesRefreshAndAccess() throws Exception {
        String u = name(); register(u); var first = login(u);
        var logout = mvc.perform(post("/api/auth/logout").with(csrf()).cookie(refresh(first))).andExpect(status().isNoContent()).andReturn();
        assertEquals(0,refresh(logout).getMaxAge());
        mvc.perform(post("/api/auth/refresh").with(csrf()).cookie(refresh(first))).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users/me").header("Authorization","Bearer "+access(first))).andExpect(status().isUnauthorized());
    }
    @Test void cookieEndpointsRequireCsrfAndRejectForeignOrigins() throws Exception {
        mvc.perform(post("/api/auth/refresh")).andExpect(status().isForbidden());
        mvc.perform(post("/api/auth/logout")).andExpect(status().isForbidden());
        mvc.perform(post("/api/auth/login").contentType("application/json").content("{}")).andExpect(status().isForbidden());
        mvc.perform(options("/api/auth/refresh").header("Origin","https://evil.example")
            .header("Access-Control-Request-Method","POST")).andExpect(status().isForbidden());
    }
}
