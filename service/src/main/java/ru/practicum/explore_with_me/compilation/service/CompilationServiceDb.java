package ru.practicum.explore_with_me.compilation.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.compilation.dto.CompilationDtoMapper;
import ru.practicum.explore_with_me.compilation.dto.InputCompilationDto;
import ru.practicum.explore_with_me.compilation.dto.OutputCompilationDto;
import ru.practicum.explore_with_me.compilation.model.Compilation;
import ru.practicum.explore_with_me.compilation.repository.CompilationRepository;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Qualifier("CompilationServiceDb")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceDb implements CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;


    @Autowired
    public CompilationServiceDb(CompilationRepository compilationRepository,
                                EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public OutputCompilationDto saveCompilation(InputCompilationDto inputCompilationDto) {
        Compilation compilation = compilationRepository.save(CompilationDtoMapper.inputToModelMapper(inputCompilationDto));
        for (Long eventId : inputCompilationDto.getEvents()) {
            eventRepository.addEventToCompilation(eventId, compilation.getId());
        }
        return modelToOutputDto(compilation, inputCompilationDto);
    }

    @Override
    public OutputCompilationDto updateCompilation(InputCompilationDto inputCompilationDto, Long compId) {
        Compilation compilation = getCompilationModel(compId);
        Compilation updatedCompilation = CompilationDtoMapper.inputToModelMapper(inputCompilationDto);
        if (inputCompilationDto.getTitle() == null) {
            updatedCompilation.setTitle(compilation.getTitle());
        }
        if (inputCompilationDto.getPinned() == null) {
            updatedCompilation.setPinned(compilation.getPinned());
        }
        updatedCompilation.setId(compId);
        for (Long eventId : inputCompilationDto.getEvents()) {
            eventRepository.addEventToCompilation(eventId, compId);
        }
        Compilation savedCompilation = compilationRepository.save(updatedCompilation);
        return modelToOutputDto(savedCompilation, inputCompilationDto);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = getCompilationModel(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public List<OutputCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return compilationListToOutputCompilationDtoList(compilationRepository.findAllCompilations(PageRequest.of(from / size, size)));
        } else {
            return compilationListToOutputCompilationDtoList(compilationRepository.findCompilationsByPinned(pinned, PageRequest.of(from / size, size)));
        }
    }

    @Override
    public OutputCompilationDto getCompilation(Long compId) {
        return modelToOutputDto(getCompilationModel(compId), null);
    }


    private Compilation getCompilationModel(Long compId) {
        if (compilationRepository.findById(compId).isEmpty()) {
            String message = String.format("%s %d %s", "Подборка с id =", compId, "не найдена");
            log.info(message);
            throw new NotFoundException(message);
        }
        return compilationRepository.findById(compId).get();
    }

    private OutputCompilationDto modelToOutputDto(Compilation compilation, InputCompilationDto inputCompilationDto) {
        OutputCompilationDto outputCompilationDto = CompilationDtoMapper.modelToOutputMapper(compilation);
        List<Event> events = !eventRepository.findEventByCompilation(compilation.getId()).isEmpty()
                ? eventRepository.findEventByCompilation(compilation.getId())
                : new ArrayList<>();
        outputCompilationDto.setEvents(events);
        return outputCompilationDto;
    }

    private List<OutputCompilationDto> compilationListToOutputCompilationDtoList(List<Compilation> compilations) {
        List<OutputCompilationDto> outputCompilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            outputCompilationDtoList.add(modelToOutputDto(compilation, null));
        }
        return outputCompilationDtoList;
    }
}
