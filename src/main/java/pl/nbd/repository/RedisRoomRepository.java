package pl.nbd.repository;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pl.nbd.model.Room;

public class RedisRoomRepository extends AbstractRedisRepository {

    private String hashPrefix = "room:";
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisRoomRepository() {
        this.initDbConnection();
    }

    public void save(Room room) {
        String key = hashPrefix + Long.toString(room.getRoomNumber());
        String json = jsonb.toJson(room);
        System.out.println(json);
        pool.set(key, json);
    }

    public Room findByRoomNumber(long roomNumber) {
        String key = hashPrefix + Long.toString(roomNumber);
        String json =  pool.get(key);
        return jsonb.fromJson(json, Room.class);
    }


}
