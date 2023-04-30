package ru.practicum.explore_with_me.compilation.service;

import ru.practicum.explore_with_me.compilation.dto.InputCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.OutputCompilationDto;

import java.util.List;

public interface CompilationService {
    OutputCompilationDto saveCompilation(InputCompilationDto inputCompilationDto);

    OutputCompilationDto updateCompilation(InputCompilationDto inputCompilationDto, Long compId);

    void deleteCompilation(Long compId);

    List<OutputCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    OutputCompilationDto getCompilation(Long compId);

}
