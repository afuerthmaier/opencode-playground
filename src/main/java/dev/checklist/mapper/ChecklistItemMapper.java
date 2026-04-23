package dev.checklist.mapper;

import dev.checklist.dto.ChecklistItemDto;
import dev.checklist.dto.CreateChecklistItemRequest;
import dev.checklist.dto.UpdateChecklistItemRequest;
import dev.checklist.entity.ChecklistItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChecklistItemMapper {

    ChecklistItemDto toDto(ChecklistItem entity);

    List<ChecklistItemDto> toDtoList(List<ChecklistItem> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ChecklistItem toEntity(CreateChecklistItemRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UpdateChecklistItemRequest request, @MappingTarget ChecklistItem entity);
}
