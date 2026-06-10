package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class MovieResourceTest {

    @BeforeEach
    void cleanDatabase() {
        Movie.deleteAll();
    }

    @Test
    void testCreateMovie() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Inception\",\"category\":\"Sci-Fi\",\"duration\":148}")
                .when()
                .post("/movies")
                .then()
                .statusCode(201)
                .body("title", is("Inception"),
                        "category", is("Sci-Fi"),
                        "duration", is(148));
    }

    @Test
    void testGetAllMovies() {
        createMovie("Inception", "Sci-Fi", 148);
        createMovie("Titanic", "Romance", 195);

        given()
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void testGetMovieById() {
        Movie movie = createMovie("Inception", "Sci-Fi", 148);

        given()
                .when()
                .get("/movies/" + movie.id.toString())
                .then()
                .statusCode(200)
                .body("title", is("Inception"));
    }

    @Test
    void testGetMovieByIdNotFound() {
        given()
                .when()
                .get("/movies/" + new ObjectId().toString())
                .then()
                .statusCode(404);
    }

    @Test
    void testGetMovieByInvalidId() {
        given()
                .when()
                .get("/movies/not-a-valid-id")
                .then()
                .statusCode(400);
    }

    @Test
    void testGetMovieByTitle() {
        createMovie("Inception", "Sci-Fi", 148);
        createMovie("Titanic", "Romance", 195);

        given()
                .when()
                .get("/movies/title/Inception")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].title", is("Inception"));
    }

    @Test
    void testGetMovieByCategory() {
        createMovie("Inception", "Sci-Fi", 148);
        createMovie("Matrix", "Sci-Fi", 136);
        createMovie("Titanic", "Romance", 195);

        given()
                .when()
                .get("/movies/category/Sci-Fi")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void testUpdateMovie() {
        Movie movie = createMovie("Inception", "Sci-Fi", 148);

        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Inception Updated\",\"category\":\"Thriller\",\"duration\":150}")
                .when()
                .put("/movies/" + movie.id.toString())
                .then()
                .statusCode(200)
                .body("title", is("Inception Updated"),
                        "category", is("Thriller"),
                        "duration", is(150));
    }

    @Test
    void testUpdateMovieNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Updated\",\"category\":\"Drama\",\"duration\":100}")
                .when()
                .put("/movies/" + new ObjectId().toString())
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteMovie() {
        Movie movie = createMovie("Inception", "Sci-Fi", 148);

        given()
                .when()
                .delete("/movies/" + movie.id.toString())
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void testDeleteMovieNotFound() {
        given()
                .when()
                .delete("/movies/" + new ObjectId().toString())
                .then()
                .statusCode(404);
    }

    private Movie createMovie(String title, String category, int duration) {
        Movie movie = new Movie();
        movie.title = title;
        movie.category = category;
        movie.duration = duration;
        movie.persist();
        return movie;
    }
}