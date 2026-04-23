package dev.checklist.mapper;

import dev.checklist.dto.ChecklistItemDto;
import dev.checklist.dto.CreateChecklistItemRequest;
import dev.checklist.dto.UpdateChecklistItemRequest;
import dev.checklist.entity.ChecklistItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-15T13:58:28+0200",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.100.v20260320-0641, environment: Java 21.0.10 (Ubuntu)"
)
@Component
public class ChecklistItemMapperImpl implements ChecklistItemMapper {

    @Override
    public ChecklistItemDto toDto(ChecklistItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String description = null;
        boolean completed = false;
        LocalDateTime createdAt = null;

        id = entity.getId();
        title = entity.getTitle();
        description = entity.getDescription();
        completed = entity.isCompleted();
        createdAt = entity.getCreatedAt();

        ChecklistItemDto checklistItemDto = new ChecklistItemDto( id, title, description, completed, createdAt );

        return checklistItemDto;
    }

    @Override
    public List<ChecklistItemDto> toDtoList(List<ChecklistItem> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ChecklistItemDto> list = new ArrayList<ChecklistItemDto>( entities.size() );
        for ( ChecklistItem checklistItem : entities ) {
            list.add( toDto( checklistItem ) );
        }

        return list;
    }

    @Override
    public ChecklistItem toEntity(CreateChecklistItemRequest request) {
        if ( request == null ) {
            return null;
        }

        String title = null;
        String description = null;

        title = request.title();
        description = request.description();

        ChecklistItem checklistItem = new ChecklistItem( title, description );

        checklistItem.setCompleted( false );
        checklistItem.setCreatedAt( java.time.LocalDateTime.now() );

        return checklistItem;
    }

    @Override
    public void updateEntity(UpdateChecklistItemRequest request, ChecklistItem entity) {
        if ( request == null ) {
            return;
        }

        if ( request.title() != null ) {
            entity.setTitle( request.title() );
        }
        if ( request.description() != null ) {
            entity.setDescription( request.description() );
        }
        if ( request.completed() != null ) {
            entity.setCompleted( request.completed() );
        }
    }
}
