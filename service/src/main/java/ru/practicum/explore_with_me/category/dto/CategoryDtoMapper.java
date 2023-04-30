package ru.practicum.explore_with_me.category.dto;

import ru.practicum.explore_with_me.category.model.Category;

public class CategoryDtoMapper {
    public static Category inputToModelMapper(InputCategoryDto inputCategoryDto) {
        return Category.builder()
                .name(inputCategoryDto.getName())
                .build();
    }

    public static OutputCategoryDto modelToOutputMapper(Category category) {
        return OutputCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
