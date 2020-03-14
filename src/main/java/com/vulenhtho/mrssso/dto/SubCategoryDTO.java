package com.vulenhtho.mrssso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {
    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;

}
