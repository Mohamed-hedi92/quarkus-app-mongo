package org.acme;


import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class MovieRepository implements PanacheMongoRepository<Movie> {
    public ObjectId id;
    public String title;
    public String category;
    public int duration;

 }