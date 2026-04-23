package dev.checklist.service;

import dev.checklist.dto.ChecklistItemDto;
import dev.checklist.dto.CreateChecklistItemRequest;
import dev.checklist.dto.UpdateChecklistItemRequest;
import dev.checklist.entity.ChecklistItem;
import dev.checklist.exception.ChecklistItemNotFoundException;
import dev.checklist.mapper.ChecklistItemMapper;
import dev.checklist.repository.ChecklistItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChecklistItemServiceTest {

    @Mock
    private ChecklistItemRepository repository;

    @Mock
    private ChecklistItemMapper mapper;

    @InjectMocks
    private ChecklistItemService service;

    private ChecklistItem sampleEntity;
    private ChecklistItemDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleEntity = new ChecklistItem("Buy groceries", "Milk, eggs, bread");
        sampleEntity.setId(1L);
        sampleEntity.setCreatedAt(LocalDateTime.of(2026, 1, 15, 10, 0));

        sampleDto = new ChecklistItemDto(1L, "Buy groceries", "Milk, eggs, bread", false,
                LocalDateTime.of(2026, 1, 15, 10, 0));
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("should return all items ordered by creation date descending")
        void returnsAllItems() {
            when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(sampleEntity));
            when(mapper.toDtoList(List.of(sampleEntity))).thenReturn(List.of(sampleDto));

            List<ChecklistItemDto> result = service.findAll();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().title()).isEqualTo("Buy groceries");
            verify(repository).findAllByOrderByCreatedAtDesc();
        }

        @Test
        @DisplayName("should return empty list when no items exist")
        void returnsEmptyList() {
            when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());
            when(mapper.toDtoList(List.of())).thenReturn(List.of());

            List<ChecklistItemDto> result = service.findAll();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByCompleted")
    class FindByCompleted {

        @Test
        @DisplayName("should return only completed items when filter is true")
        void returnsCompletedItems() {
            sampleEntity.setCompleted(true);
            ChecklistItemDto completedDto = new ChecklistItemDto(1L, "Buy groceries",
                    "Milk, eggs, bread", true, sampleDto.createdAt());

            when(repository.findByCompletedOrderByCreatedAtDesc(true)).thenReturn(List.of(sampleEntity));
            when(mapper.toDtoList(List.of(sampleEntity))).thenReturn(List.of(completedDto));

            List<ChecklistItemDto> result = service.findByCompleted(true);

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().completed()).isTrue();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return item when it exists")
        void returnsItem() {
            when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(mapper.toDto(sampleEntity)).thenReturn(sampleDto);

            ChecklistItemDto result = service.findById(1L);

            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("Buy groceries");
        }

        @Test
        @DisplayName("should throw ChecklistItemNotFoundException when item does not exist")
        void throwsWhenNotFound() {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(999L))
                    .isInstanceOf(ChecklistItemNotFoundException.class)
                    .hasMessageContaining("999");
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create and return new item")
        void createsItem() {
            CreateChecklistItemRequest request = new CreateChecklistItemRequest("Buy groceries",
                    "Milk, eggs, bread");

            when(mapper.toEntity(request)).thenReturn(sampleEntity);
            when(repository.save(sampleEntity)).thenReturn(sampleEntity);
            when(mapper.toDto(sampleEntity)).thenReturn(sampleDto);

            ChecklistItemDto result = service.create(request);

            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("Buy groceries");
            assertThat(result.completed()).isFalse();
            verify(repository).save(sampleEntity);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("should update existing item and return updated dto")
        void updatesItem() {
            UpdateChecklistItemRequest request = new UpdateChecklistItemRequest("Buy groceries updated",
                    "Milk, eggs, bread, butter", true);
            ChecklistItemDto updatedDto = new ChecklistItemDto(1L, "Buy groceries updated",
                    "Milk, eggs, bread, butter", true, sampleDto.createdAt());

            when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(repository.save(sampleEntity)).thenReturn(sampleEntity);
            when(mapper.toDto(sampleEntity)).thenReturn(updatedDto);

            ChecklistItemDto result = service.update(1L, request);

            assertThat(result.title()).isEqualTo("Buy groceries updated");
            assertThat(result.completed()).isTrue();
            verify(mapper).updateEntity(request, sampleEntity);
            verify(repository).save(sampleEntity);
        }

        @Test
        @DisplayName("should throw ChecklistItemNotFoundException when updating non-existent item")
        void throwsWhenNotFound() {
            UpdateChecklistItemRequest request = new UpdateChecklistItemRequest("Title", null, false);

            when(repository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(999L, request))
                    .isInstanceOf(ChecklistItemNotFoundException.class)
                    .hasMessageContaining("999");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("should delete existing item")
        void deletesItem() {
            when(repository.existsById(1L)).thenReturn(true);

            service.delete(1L);

            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw ChecklistItemNotFoundException when deleting non-existent item")
        void throwsWhenNotFound() {
            when(repository.existsById(999L)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(999L))
                    .isInstanceOf(ChecklistItemNotFoundException.class)
                    .hasMessageContaining("999");

            verify(repository, never()).deleteById(any());
        }
    }
}
