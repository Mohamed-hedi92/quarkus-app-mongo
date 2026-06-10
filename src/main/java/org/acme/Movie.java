package org.acme;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(collection = "movies")
public class Movie extends PanacheMongoEntity {

    public String title;
    public String category;
    public int duration;

    public Movie() {
    }

    public Movie(String title, String category, int duration) {
        this.title = title;
        this.category = category;
        this.duration = duration;
    }
}