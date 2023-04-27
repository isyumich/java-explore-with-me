package ru.practicum.explore_with_me.compilation.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.dto.InputCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.OutputCompilationDto;
import ru.practicum.explore_with_me.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCompilationController {
    final String pathForCompId = "/{compId}";
    final CompilationService compilationService;

    @Autowired
    public AdminCompilationController(@Qualifier("CompilationServiceDb") CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutputCompilationDto saveCompilation(@Valid @RequestBody InputCompilationDto inputCompilationDto) {
        log.info("Запрос на добавление подборки");
        return compilationService.saveCompilation(inputCompilationDto);
    }

    @PatchMapping(pathForCompId)
    public OutputCompilationDto updateCompilation(@RequestBody InputCompilationDto inputCompilationDto,
                                                  @PathVariable(name = "compId") Long compId) {
        log.info(String.format("%s %d", "Запрос на изменение подборки с id =", compId));
        return compilationService.updateCompilation(inputCompilationDto, compId);
    }

    @DeleteMapping(pathForCompId)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") Long compId) {
        log.info(String.format("%s %d", "Запрос на удаление подборки с id =", compId));
        compilationService.deleteCompilation(compId);
    }
}
