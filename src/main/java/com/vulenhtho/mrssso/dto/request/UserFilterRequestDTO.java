package com.vulenhtho.mrssso.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterRequestDTO {

    private String sort;

    private Boolean sex;

    private Boolean activated;

    private Boolean locked;

    private String search;

    private String role;

    private Integer page;

    private Integer size;
}
