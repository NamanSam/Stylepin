package com.stylepin.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylepin.dto.ApiErrorDTO;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;
@Component
public class SecurityErrorHandler {
    private final ObjectMapper mapper;
    public SecurityErrorHandler(ObjectMapper mapper) { this.mapper = mapper; }
    public void write(HttpServletResponse response, int status) throws IOException {
        response.setStatus(status); response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), new ApiErrorDTO(status, "HTTP_" + status,
            status == 401 ? "Sign in required or session expired" : "Request not permitted", Map.of()));
    }
}
