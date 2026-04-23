package dev.checklist.controller;

import dev.checklist.dto.ChecklistItemDto;
import dev.checklist.dto.CreateChecklistItemRequest;
import dev.checklist.dto.UpdateChecklistItemRequest;
import dev.checklist.service.ChecklistItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checklist-items")
public class ChecklistItemController {

    private final ChecklistItemService service;

    public ChecklistItemController(ChecklistItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<ChecklistItemDto> getAll(
            @RequestParam(required = false) Boolean completed) {
        if (completed != null) {
            return service.findByCompleted(completed);
        }
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ChecklistItemDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChecklistItemDto create(@Valid @RequestBody CreateChecklistItemRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ChecklistItemDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChecklistItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
