package com.vulenhtho.mrssso.security.jwt;

import com.vulenhtho.mrssso.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;

    private String type = "Bearer";

    private UserDTO userDTO;

    public JwtResponse(String accessToken, UserDTO userDTO) {
        this.userDTO = userDTO;
        this.token = accessToken;
    }

}
