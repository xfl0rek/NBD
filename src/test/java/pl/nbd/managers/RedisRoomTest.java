package pl.nbd.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.RedisRoomRepository;

import java.util.List;

public class RedisRoomTest {

    @Test
    public void createAndReadRoomFromRedis() {
        Room room = new RoomChildren(1, 100, 4, 2);
        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        redisRoomRepository.save(room);

        Room roomFromRedis = redisRoomRepository.findByRoomNumber(1);
        Assertions.assertEquals(room, roomFromRedis);
    }

    @Test
    public void readAllRoomsFromRedis() {
        Room room1 = new RoomChildren(1, 100, 4, 2);
        Room room2 = new RoomRegular(2, 50, 2, true);
        Room room3 = new RoomChildren(3, 150, 5, 1);

        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        redisRoomRepository.save(room1);
        redisRoomRepository.save(room2);
        redisRoomRepository.save(room3);

        List<Room> rooms = redisRoomRepository.findAll();

        Assertions.assertEquals(3, rooms.size());
        Assertions.assertEquals(room1, rooms.get(0));
        Assertions.assertEquals(room2, rooms.get(1));
        Assertions.assertEquals(room3, rooms.get(2));
    }
}
