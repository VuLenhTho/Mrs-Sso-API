package com.vulenhtho.mrssso.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WelcomeSlideDTO {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private Boolean isDisabled;
}
