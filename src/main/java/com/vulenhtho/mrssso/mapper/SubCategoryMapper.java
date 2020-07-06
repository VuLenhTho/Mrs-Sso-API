package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.SubCategoryDTO;
import com.vulenhtho.mrssso.entity.SubCategory;
import com.vulenhtho.mrssso.repository.SubCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryMapper(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public SubCategoryDTO toDTO(SubCategory subCategory) {
        if (subCategory == null) return null;
        return new SubCategoryDTO(subCategory.getId(), subCategory.getName()
                , subCategory.getCategory() != null ? subCategory.getCategory().getId() : null
                , subCategory.getCategory() != null ? subCategory.getCategory().getName() : null);
    }

    public Set<SubCategoryDTO> toDTO(Set<SubCategory> categories) {
        if (categories == null) return null;
        return categories.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public SubCategory toEntity(SubCategoryDTO subCategoryDTO) {
        if (subCategoryDTO == null) return null;
        return subCategoryRepository.getOne(subCategoryDTO.getId());
    }

    public Set<SubCategory> toEntity(Set<SubCategoryDTO> subCategoryDTOS) {
        if (subCategoryDTOS == null) return null;
        return subCategoryDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }

}
