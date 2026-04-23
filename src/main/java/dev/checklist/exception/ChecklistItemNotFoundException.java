package dev.checklist.exception;

public class ChecklistItemNotFoundException extends RuntimeException {

    private final Long itemId;

    public ChecklistItemNotFoundException(Long itemId) {
        super("Checklist item not found with id: " + itemId);
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }
}
