package ru.practicum.explore_with_me.compilation.dto;

import ru.practicum.explore_with_me.compilation.model.Compilation;

public class CompilationDtoMapper {
    public static Compilation inputToModelMapper(InputCompilationDto inputCompilationDto) {
        return Compilation.builder()
                .pinned(inputCompilationDto.getPinned())
                .title(inputCompilationDto.getTitle())
                .build();
    }

    public static OutputCompilationDto modelToOutputMapper(Compilation compilation) {
        return OutputCompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
