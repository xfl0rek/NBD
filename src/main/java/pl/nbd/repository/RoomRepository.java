package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import pl.nbd.model.Room;
import pl.nbd.model.RoomRegular;

public class RoomRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public RoomRepository() {
        this.initDBConnection();
    }

    public void create(Room room) {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        collection.insertOne(room);
    }

    public MongoCollection<Room> read() {
        return getDatabase().getCollection("rooms", Room.class);
    }

    public void update(Room room) {
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        BasicDBObject update = new BasicDBObject();
        update.put("_id", room.getRoomNumber());
        collection.replaceOne(update, room);
    }

    public void delete(long id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        MongoCollection<Room> collection = getDatabase().getCollection("rooms", Room.class);
        collection.deleteOne(query);
    }
}
