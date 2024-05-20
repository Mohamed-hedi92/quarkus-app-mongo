package org.acme;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;



@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class MovieResource {

    @Inject MovieRepository movieRepository;

    @GET
    public Response getAll(){
        List<Movie> movies= movieRepository.listAll();
        return Response.ok(movies).build();
    }

    //method to fetch the movie by id
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") ObjectId id){
        return movieRepository.findByIdOptional(id)
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response updatedMovie(@PathParam("id") String id, Movie updatedMovie) {
        try {
            ObjectId movieId = new ObjectId(id);
            // Überprüfe, ob der Film mit der gegebenen ID existiert
            Movie existingMovie = movieRepository.findById(movieId);
            if (existingMovie == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Film nicht gefunden").build();
            }
            // Aktualisiere die Details des vorhandenen Films mit den Details des aktualisierten Films
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setCategory(updatedMovie.getCategory());
            existingMovie.setDuration(updatedMovie.getDuration());
            // Speichere den aktualisierten Film
            movieRepository.update(existingMovie);
            return Response.ok(existingMovie).build(); // Rückgabe des aktualisierten Films mit Statuscode 200 (OK)
        } catch (IllegalArgumentException e) {
            // ID ist ungültig, Statuscode 400 (Bad Request) zurückgeben
            return Response.status(Response.Status.BAD_REQUEST).entity("Ungültige Film-ID").build();
        } catch (Exception e) {
            // Ein allgemeiner Fehler ist aufgetreten, Statuscode 500 (Internal Server Error) zurückgeben
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ein Fehler ist aufgetreten").build();
        }

    }

    //method to fetch the movie by Title

    @GET
    @Path("title/{title}")
    public Response getByTitle(@PathParam("title") String title){
        return movieRepository.find("title",title)
                .singleResultOptional()
                .map(movie-> Response.ok(movie).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    // fetch movies by category
    @GET
    @Path("category/{category}")
    public Response getByCategory(@PathParam("category") String category){
        List<Movie> movies= movieRepository.list("category",category);
        return Response.ok(movies).build();

    }

    // method to create new movie
    @POST
    public Response create(Movie movie){
        movieRepository.persist(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    // method to delete a movie by id
/*    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") ObjectId id){
        boolean deleted = movieRepository.deleteById(id);
        return deleted ? Response.noContent().build()
                : Response.status(NOT_FOUND).build();
    }*/

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieById(@PathParam("id") String id) {
        try {
            ObjectId movieId = new ObjectId(id);
            boolean deleted = movieRepository.deleteById(movieId);

            if (deleted) {
                return Response.noContent().build(); // Erfolgreich gelöscht, Statuscode 204 zurückgeben
            } else {
                return Response.status(Response.Status.NOT_FOUND).build(); // Film nicht gefunden, Statuscode 404 zurückgeben
            }
        } catch (IllegalArgumentException e) {
            // ID ist ungültig, Statuscode 400 (Bad Request) zurückgeben
            return Response.status(Response.Status.BAD_REQUEST).entity("Ungültige Film-ID").build();
        } catch (Exception e) {
            // Ein allgemeiner Fehler ist aufgetreten, Statuscode 500 (Internal Server Error) zurückgeben
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Ein Fehler ist aufgetreten").build();
        }
    }
}


