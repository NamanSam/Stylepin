package com.stylepin.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties = {"stylepin.auth.cookie-secure=true", "spring.datasource.url=jdbc:h2:mem:cookie_test"})
@AutoConfigureMockMvc @ActiveProfiles("test")
class SecurityCookieTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Test void realCookieCsrfFlowAndSecureFlags() throws Exception {
        var result = mvc.perform(get("/api/auth/csrf").secure(true)).andExpect(status().isOk()).andReturn();
        var cookie = result.getResponse().getCookie("XSRF-TOKEN");
        assertNotNull(cookie); assertTrue(cookie.isHttpOnly()); assertTrue(cookie.getSecure());
        String token = json.readTree(result.getResponse().getContentAsString()).get("token").asText();
        var logout = mvc.perform(post("/api/auth/logout").secure(true).cookie(cookie).header("X-XSRF-TOKEN",token))
            .andExpect(status().isNoContent()).andReturn();
        var refresh = logout.getResponse().getCookie("stylepin_refresh");
        assertTrue(refresh.isHttpOnly()); assertTrue(refresh.getSecure()); assertEquals(0,refresh.getMaxAge());
        mvc.perform(post("/api/auth/refresh").cookie(cookie).header("X-XSRF-TOKEN","forged")).andExpect(status().isForbidden());
    }
}
