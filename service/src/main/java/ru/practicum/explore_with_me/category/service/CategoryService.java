package ru.practicum.explore_with_me.category.service;

import ru.practicum.explore_with_me.category.dto.InputCategoryDto;
import ru.practicum.explore_with_me.category.dto.OutputCategoryDto;

import java.util.List;

public interface CategoryService {
    OutputCategoryDto addCategory(InputCategoryDto inputCategoryDto);

    OutputCategoryDto updateCategory(InputCategoryDto inputCategoryDto, Long catId);

    void deleteCategory(Long catId);

    List<OutputCategoryDto> getCategories(Integer from, Integer size);

    OutputCategoryDto getCategory(Long catId);
}
