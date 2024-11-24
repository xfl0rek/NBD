package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import pl.nbd.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository extends AbstractMongoRepository implements IRoomRepository {
    @Override
    public void close() throws Exception {

    }

    public RoomRepository() {
        this.initDBConnection();
    }

    @Override
    public void create(Room room) {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        collection.insertOne(room);
    }

    @Override
    public List<Room> readAll() {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public Room read(long id) {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        return collection.find(Filters.eq("_id", id)).first();
    }

    public void update(Room room) {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        BasicDBObject update = new BasicDBObject();
        update.put("_id", room.getRoomNumber());
        collection.replaceOne(update, room);
    }

    @Override
    public void delete(long id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        collection.deleteOne(query);
    }
}
