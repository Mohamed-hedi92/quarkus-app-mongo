package org.acme;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import jakarta.inject.Inject;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    MovieRepository movieRepository;

    @GET
    public Response getAll() {
        return Response.ok(movieRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Movie movie = movieRepository.findById(objectId);
            if (movie != null) {
                return Response.ok(movie).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movie not found with id: " + id).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid movie ID: " + id).build();
        }
    }

    @GET
    @Path("/title/{title}")
    public Response getByTitle(@PathParam("title") String title) {
        return Response.ok(movieRepository.find("title", title).list()).build();
    }

    @GET
    @Path("/category/{category}")
    public Response getByCategory(@PathParam("category") String category) {
        return Response.ok(movieRepository.find("category", category).list()).build();
    }

    @POST
    public Response create(Movie movie) {
        movieRepository.persist(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Movie updatedMovie) {
        try {
            ObjectId objectId = new ObjectId(id);
            Movie movie = movieRepository.findById(objectId);
            if (movie == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Movie not found with id: " + id).build();
            }
            movie.title = updatedMovie.title;
            movie.category = updatedMovie.category;
            movie.duration = updatedMovie.duration;
            movieRepository.update(movie);
            return Response.ok(movie).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid movie ID: " + id).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            boolean deleted = movieRepository.deleteById(objectId);
            if (deleted) {
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Movie not found with id: " + id).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid movie ID: " + id).build();
        }
    }
}