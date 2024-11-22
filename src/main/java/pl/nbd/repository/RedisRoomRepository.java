package pl.nbd.repository;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;

public class RedisRoomRepository extends AbstractRedisRepository {

    private String hashPrefix = "room:";
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisRoomRepository() {
        this.initDbConnection();
    }

    public void save(Room room) {
        String key = hashPrefix + Long.toString(room.getRoomNumber());
        String json = jsonb.toJson(room);
        pool.jsonSet(key, json);
    }

    public Room findByRoomNumber(long roomNumber) {
        String key = hashPrefix + 1;
        String json =  pool.jsonGet(key);
        return jsonb.fromJson(json, Room.class);
    }


}
