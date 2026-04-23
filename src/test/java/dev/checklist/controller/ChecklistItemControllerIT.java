package dev.checklist.controller;

import dev.checklist.IntegrationTestBase;
import dev.checklist.repository.ChecklistItemRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

class ChecklistItemControllerIT extends IntegrationTestBase {

    @Autowired
    private ChecklistItemRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/checklist-items")
    class CreateItem {

        @Test
        @DisplayName("should create item and return 201")
        void createsItem() {
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "Buy groceries",
                        "description": "Milk, eggs, bread"
                    }
                    """)
            .when()
                .post()
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Buy groceries"))
                .body("description", equalTo("Milk, eggs, bread"))
                .body("completed", equalTo(false))
                .body("createdAt", notNullValue());
        }

        @Test
        @DisplayName("should return 400 when title is blank")
        void rejectBlankTitle() {
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "",
                        "description": "Some description"
                    }
                    """)
            .when()
                .post()
            .then()
                .statusCode(400)
                .body("fieldErrors.title", not(emptyString()));
        }

        @Test
        @DisplayName("should return 400 when title is missing")
        void rejectMissingTitle() {
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "description": "No title here"
                    }
                    """)
            .when()
                .post()
            .then()
                .statusCode(400)
                .body("fieldErrors.title", not(emptyString()));
        }
    }

    @Nested
    @DisplayName("GET /api/checklist-items")
    class GetAll {

        @Test
        @DisplayName("should return empty list when no items exist")
        void returnsEmptyList() {
            given()
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(0));
        }

        @Test
        @DisplayName("should return all items")
        void returnsAllItems() {
            createSampleItem("First item");
            createSampleItem("Second item");

            given()
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(2));
        }

        @Test
        @DisplayName("should filter by completed status")
        void filtersByCompleted() {
            int itemId = createSampleItem("Task to complete");
            markAsCompleted(itemId);
            createSampleItem("Pending task");

            given()
                .queryParam("completed", true)
            .when()
                .get()
            .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].completed", equalTo(true));
        }
    }

    @Nested
    @DisplayName("GET /api/checklist-items/{id}")
    class GetById {

        @Test
        @DisplayName("should return item when it exists")
        void returnsItem() {
            int itemId = createSampleItem("Existing item");

            given()
            .when()
                .get("/{id}", itemId)
            .then()
                .statusCode(200)
                .body("id", equalTo(itemId))
                .body("title", equalTo("Existing item"));
        }

        @Test
        @DisplayName("should return 404 when item does not exist")
        void returns404() {
            given()
            .when()
                .get("/{id}", 999999)
            .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", notNullValue());
        }
    }

    @Nested
    @DisplayName("PUT /api/checklist-items/{id}")
    class UpdateItem {

        @Test
        @DisplayName("should update item and return updated data")
        void updatesItem() {
            int itemId = createSampleItem("Original title");

            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "Updated title",
                        "description": "Updated description",
                        "completed": true
                    }
                    """)
            .when()
                .put("/{id}", itemId)
            .then()
                .statusCode(200)
                .body("id", equalTo(itemId))
                .body("title", equalTo("Updated title"))
                .body("description", equalTo("Updated description"))
                .body("completed", equalTo(true));
        }

        @Test
        @DisplayName("should return 404 when updating non-existent item")
        void returns404() {
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "Does not matter",
                        "completed": false
                    }
                    """)
            .when()
                .put("/{id}", 999999)
            .then()
                .statusCode(404);
        }

        @Test
        @DisplayName("should return 400 when validation fails on update")
        void rejectsInvalidUpdate() {
            int itemId = createSampleItem("Valid item");

            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "",
                        "completed": true
                    }
                    """)
            .when()
                .put("/{id}", itemId)
            .then()
                .statusCode(400)
                .body("fieldErrors.title", not(emptyString()));
        }
    }

    @Nested
    @DisplayName("DELETE /api/checklist-items/{id}")
    class DeleteItem {

        @Test
        @DisplayName("should delete item and return 204")
        void deletesItem() {
            int itemId = createSampleItem("Item to delete");

            given()
            .when()
                .delete("/{id}", itemId)
            .then()
                .statusCode(204);

            // Verify it's gone
            given()
            .when()
                .get("/{id}", itemId)
            .then()
                .statusCode(404);
        }

        @Test
        @DisplayName("should return 404 when deleting non-existent item")
        void returns404() {
            given()
            .when()
                .delete("/{id}", 999999)
            .then()
                .statusCode(404);
        }
    }

    // -- helper methods --

    private int createSampleItem(String title) {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "%s",
                        "description": "Test description"
                    }
                    """.formatted(title))
            .when()
                .post()
            .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private void markAsCompleted(int itemId) {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "title": "Task to complete",
                    "completed": true
                }
                """)
        .when()
            .put("/{id}", itemId)
        .then()
            .statusCode(200);
    }
}
