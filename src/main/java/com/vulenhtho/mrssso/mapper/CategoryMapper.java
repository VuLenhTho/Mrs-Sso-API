package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.CategoryDTO;
import com.vulenhtho.mrssso.entity.Category;
import com.vulenhtho.mrssso.repository.CategoryRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private final CategoryRepository categoryRepository;

    private final SubCategoryMapper subCategoryMapper;

    public CategoryMapper(CategoryRepository categoryRepository, SubCategoryMapper subCategoryMapper) {
        this.categoryRepository = categoryRepository;
        this.subCategoryMapper = subCategoryMapper;
    }

    public CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.refine(category, categoryDTO, BeanUtils::copyNonNull);
        categoryDTO.setSubCategoryDTOS(subCategoryMapper.toDTO(category.getSubCategories()));

        return categoryDTO;
    }

    public List<CategoryDTO> toDTO(List<Category> categories) {
        if (categories == null) return null;
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) return null;
        return categoryRepository.findById(categoryDTO.getId()).orElse(null);
    }

    public Set<Category> toEntity(Set<CategoryDTO> categoryDTOS) {
        if (categoryDTOS == null) return null;
        return categoryDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
