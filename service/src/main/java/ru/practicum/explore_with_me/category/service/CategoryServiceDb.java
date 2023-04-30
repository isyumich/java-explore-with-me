package ru.practicum.explore_with_me.category.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.category.dto.CategoryDtoMapper;
import ru.practicum.explore_with_me.category.dto.InputCategoryDto;
import ru.practicum.explore_with_me.category.dto.OutputCategoryDto;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.category.repository.CategoryRepository;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.ConflictException;
import ru.practicum.explore_with_me.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("CategoryServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceDb implements CategoryService {

    CategoryRepository categoryRepository;
    EventRepository eventRepository;


    @Autowired
    public CategoryServiceDb(CategoryRepository categoryRepository,
                             EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public OutputCategoryDto addCategory(InputCategoryDto inputCategoryDto) {
        try {
            return modelToOutputDto(categoryRepository.save(CategoryDtoMapper.inputToModelMapper(inputCategoryDto)));
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public OutputCategoryDto updateCategory(InputCategoryDto inputCategoryDto, Long catId) {
        Category updatedCategory = CategoryDtoMapper.inputToModelMapper(inputCategoryDto);
        updatedCategory.setId(catId);
        try {
            return modelToOutputDto(categoryRepository.save(updatedCategory));
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = getCategoryModel(catId);
        if (eventRepository.findEventsByCategory(category).size() > 0) {
            String message = "Попытка удаления категории, по которой уже есть события";
            log.info(message);
            throw new ConflictException(message);
        }
        categoryRepository.delete(category);
    }

    @Override
    public List<OutputCategoryDto> getCategories(Integer from, Integer size) {
        return categoryListToOutputCategoryDtoList(categoryRepository.findAllCategories(PageRequest.of(from / size, size)));
    }

    @Override
    public OutputCategoryDto getCategory(Long catId) {
        return modelToOutputDto(getCategoryModel(catId));
    }


    private Category getCategoryModel(Long catId) {
        if (categoryRepository.findById(catId).isEmpty()) {
            String message = String.format("%s %d %s", "Категория с id =", catId, "не найдена");
            log.info(message);
            throw new NotFoundException(message);
        }
        return categoryRepository.findById(catId).get();
    }

    private OutputCategoryDto modelToOutputDto(Category category) {
        return CategoryDtoMapper.modelToOutputMapper(category);
    }

    private List<OutputCategoryDto> categoryListToOutputCategoryDtoList(List<Category> categories) {
        List<OutputCategoryDto> outputCategoryDtoList = new ArrayList<>();
        for (Category category : categories) {
            outputCategoryDtoList.add(modelToOutputDto(category));
        }
        return outputCategoryDtoList;
    }
}
