package org.acme;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import static com.google.common.util.concurrent.Runnables.doNothing;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class MovieResourceTest {

    @Inject
    MovieRepository movieRepository; // Du musst sicherstellen, dass movieRepository richtig konfiguriert ist.

    // Test für Get Methode
    @Test
    @Order(2)
    void getAll() {
        given()
                .when()
                .get("/movies")
                .then()

                .body("id", hasItems("663a32d1920a8639e6a18bb1","663a35fc920a8639e6a18bb2","663b62d8877b5d3792041c60","6641bd8a4bd70837cf11971e"))
                .body("title", hasItems("fractured1", "titanic4", "titanic4","Titanic: Special Edition"))
                .body("category", hasItems("romantic","Horror","Horror","Romance/Drama"))
                .body("duration", hasItems(0, 120))
                .statusCode(Response.Status.OK.getStatusCode());
    }


    @Test
    @Order(1)
    public void testCreate() {

        Movie movie = new Movie();
        movie.setTitle("Titanic");
        movie.setCategory("Romance");
        movie.setDuration(120);


        given()
                .contentType(ContentType.JSON)
                .body(movie)
                .when()
                .post("/movies")
                .then()
                .statusCode(201) // Erwarteter Statuscode CREATED
                .body("title", equalTo("Titanic")); // Erwarteter Filmname
    }

    @Test
    @Order(1)
    public void testDeleteById() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Titanic");
        movie.setCategory("Romance");
        movie.setDuration(120);
        movieRepository.persist(movie);
        // Act & Assert
        given()
                .when()
                .delete("/movies/"+ movie.getId().toHexString())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()); // Erwarteter Statuscode NO_CONTENT
    }


    @Test
    @Order(1)
    public void testUpdateMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Titanic");
        movie.setCategory("Romance");
        movie.setDuration(120);
        movieRepository.persist(movie);

        // Neuer Titel und Kategorie für das Update
        String newTitle = "Titanic: Special Edition";
        String newCategory = "Romance/Drama";

        // Act & Assert
        given()
                .contentType("application/json")
                .body("{\"title\":\"" + newTitle + "\", \"category\":\"" + newCategory + "\", \"duration\":120}")
                .when()
                .put("/movies/"+ movie.getId().toHexString())
                .then()
                .statusCode(Response.Status.OK.getStatusCode()) // Erwarteter Statuscode OK
                .body("title", equalTo(newTitle)) // Überprüfung des neuen Titels
                .body("category", equalTo(newCategory)); // Überprüfung der neuen Kategorie
    }
}


