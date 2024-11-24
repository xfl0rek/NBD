package pl.nbd.repository;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pl.nbd.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedisRoomRepository extends AbstractRedisRepository implements IRoomRepository {

    private String hashPrefix = "room:";
    private Jsonb jsonb = JsonbBuilder.create();

    public RedisRoomRepository() {
        this.initDbConnection();
    }

    @Override
    public void create(Room room) {
        try {
            String key = hashPrefix + Long.toString(room.getRoomNumber());
            String json = jsonb.toJson(room);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long roomId) {
        try {
            String key = hashPrefix + Long.toString(roomId);
            pool.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Room room) {
        try {
            String key = hashPrefix + Long.toString(room.getRoomNumber());
            String json = jsonb.toJson(room);
            pool.set(key, json);
            pool.expire(key, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Room read(long roomNumber) {
        try {
            String key = hashPrefix + Long.toString(roomNumber);
            String json = pool.get(key);
            return jsonb.fromJson(json, Room.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Room> readAll() {
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

   public void clearCache() {
        try {
            pool.flushAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        clearCache();
        pool.close();
    }
}
