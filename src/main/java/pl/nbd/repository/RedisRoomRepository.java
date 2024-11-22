package pl.nbd.repository;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pl.nbd.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisRoomRepository extends AbstractRedisRepository {

    private String hashPrefix = "room:";
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisRoomRepository() {
        this.initDbConnection();
    }

    public void save(Room room) {
        try {
            String key = hashPrefix + Long.toString(room.getRoomNumber());
            String json = jsonb.toJson(room);
            System.out.println(json);
            pool.set(key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Room findByRoomNumber(long roomNumber) {
        try {
            String key = hashPrefix + Long.toString(roomNumber);
            String json = pool.get(key);
            return jsonb.fromJson(json, Room.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Room> findAll() {
        try {
            List<Room> rooms = new ArrayList<>();
            Set<String> keys = pool.keys(hashPrefix + "*");
            for (String key : keys) {
                String json = pool.get(key);
                Room room = jsonb.fromJson(json, Room.class);
                rooms.add(room);
            }
            return rooms;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
