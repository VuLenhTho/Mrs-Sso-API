package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.CategoryDTO;
import com.vulenhtho.mrssso.entity.Category;
import com.vulenhtho.mrssso.repository.CategoryRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private CategoryRepository categoryRepository;

    public CategoryMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO toDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.refine(category, categoryDTO, BeanUtils::copyNonNull);
        return categoryDTO;
    }

    public Set<CategoryDTO> toDTO(Set<Category> categories){
        return categories.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    public Category toEntity(CategoryDTO categoryDTO){
        return categoryRepository.getOne(categoryDTO.getId());
    }

    public Set<Category> toEntity(Set<CategoryDTO> categoryDTOS){
        return categoryDTOS.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
