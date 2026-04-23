package dev.checklist.repository;

import dev.checklist.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    List<ChecklistItem> findByCompletedOrderByCreatedAtDesc(boolean completed);

    List<ChecklistItem> findAllByOrderByCreatedAtDesc();
}
