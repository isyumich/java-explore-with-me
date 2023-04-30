package ru.practicum.explore_with_me.compilation.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.compilation.dto.OutputCompilationDto;
import ru.practicum.explore_with_me.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicCompilationController {
    final CompilationService compilationService;

    @Autowired
    public PublicCompilationController(@Qualifier("CompilationServiceDb") CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<OutputCompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                      @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка подборок");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public OutputCompilationDto getCompilation(@PathVariable(name = "compId") Long compId) {
        log.info(String.format("%s %d", "Запрос на получение подборки с id =", compId));
        return compilationService.getCompilation(compId);
    }

}
