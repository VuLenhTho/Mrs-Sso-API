package com.vulenhtho.mrssso.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoWebResponseDTO {
    private Long id;

    private String userName;

    private String fullName;

    private Boolean sex;

    private String email;

    private String phone;

    private String avatarUrl;

    private Long coins;

    private Boolean activated;

    private Boolean locked;

    private List<Long> receiptIds;
}
