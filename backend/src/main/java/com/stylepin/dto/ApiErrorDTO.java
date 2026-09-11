package com.stylepin.dto;
import java.util.Map;
public record ApiErrorDTO(int status, String code, String message, Map<String, String> fieldErrors) {}
