package dev.checklist.service;

import dev.checklist.dto.ChecklistItemDto;
import dev.checklist.dto.CreateChecklistItemRequest;
import dev.checklist.dto.UpdateChecklistItemRequest;
import dev.checklist.entity.ChecklistItem;
import dev.checklist.exception.ChecklistItemNotFoundException;
import dev.checklist.mapper.ChecklistItemMapper;
import dev.checklist.repository.ChecklistItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChecklistItemService {

    private final ChecklistItemRepository repository;
    private final ChecklistItemMapper mapper;

    public ChecklistItemService(ChecklistItemRepository repository, ChecklistItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ChecklistItemDto> findAll() {
        return mapper.toDtoList(repository.findAllByOrderByCreatedAtDesc());
    }

    public List<ChecklistItemDto> findByCompleted(boolean completed) {
        return mapper.toDtoList(repository.findByCompletedOrderByCreatedAtDesc(completed));
    }

    public ChecklistItemDto findById(Long id) {
        ChecklistItem item = repository.findById(id)
                .orElseThrow(() -> new ChecklistItemNotFoundException(id));
        return mapper.toDto(item);
    }

    @Transactional
    public ChecklistItemDto create(CreateChecklistItemRequest request) {
        ChecklistItem item = mapper.toEntity(request);
        ChecklistItem saved = repository.save(item);
        return mapper.toDto(saved);
    }

    @Transactional
    public ChecklistItemDto update(Long id, UpdateChecklistItemRequest request) {
        ChecklistItem item = repository.findById(id)
                .orElseThrow(() -> new ChecklistItemNotFoundException(id));
        mapper.updateEntity(request, item);
        ChecklistItem saved = repository.save(item);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ChecklistItemNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
