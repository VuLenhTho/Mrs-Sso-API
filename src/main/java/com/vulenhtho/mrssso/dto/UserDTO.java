package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String userName;

    private String password;

    private String fullName;

    private Boolean sex;

    private String email;

    private String phone;

    private String avatarUrl;

    private Long coins;

    private Boolean activated;

    private Boolean locked;

    private String activationKey;

    private String resetKey;

    private Instant resetDate;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<RoleDTO> roles = new HashSet<>();

}
