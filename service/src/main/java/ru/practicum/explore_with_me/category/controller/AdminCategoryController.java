package ru.practicum.explore_with_me.category.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.category.dto.InputCategoryDto;
import ru.practicum.explore_with_me.category.dto.OutputCategoryDto;
import ru.practicum.explore_with_me.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
    String pathForCatId = "/{catId}";
    String pathVarCatId = "catId";
    CategoryService categoryService;

    @Autowired
    public AdminCategoryController(@Qualifier("CategoryServiceDb") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputCategoryDto addCategory(@Valid @RequestBody InputCategoryDto inputCategoryDto) {
        log.info("Запрос на создание новой категории");
        return categoryService.addCategory(inputCategoryDto);
    }

    @PatchMapping(pathForCatId)
    public OutputCategoryDto updateCategory(@Valid @RequestBody InputCategoryDto inputCategoryDto,
                                            @PathVariable(name = pathVarCatId) Long catId) {
        log.info(String.format("%s %d", "Запрос на изменение категории с id =", catId));
        return categoryService.updateCategory(inputCategoryDto, catId);
    }

    @DeleteMapping(pathForCatId)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = pathVarCatId) Long catId) {
        log.info(String.format("%s %d", "Запрос на удаление категории с id =", catId));
        categoryService.deleteCategory(catId);
    }
}
