package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractEntity implements Serializable {
    @BsonProperty("_id")
    private final long id;

    public long getId() {
        return id;
    }

    public AbstractEntity(long id) {
        this.id = id;
    }
}
