package com.sample.poc.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String name;
    private String accessToken;
    private String type;
    private long expiresIn;

    public AuthResponse(String name, String accessToken, long expiresIn) {
        this.name = name;
        this.accessToken = accessToken;
        this.type = "Bearer";
        this.expiresIn = expiresIn;
    }
}
