package ru.practicum.explore_with_me.category.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.category.dto.OutputCategoryDto;
import ru.practicum.explore_with_me.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCategoryController {
    CategoryService categoryService;

    @Autowired
    public PublicCategoryController(@Qualifier("CategoryServiceDb") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<OutputCategoryDto> getCategories(@RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Запрос на получение всех категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public OutputCategoryDto getCategory(@PathVariable(name = "catId") Long catId) {
        log.info(String.format("%s %d", "Запрос на получение категории с id =", catId));
        return categoryService.getCategory(catId);
    }
}
