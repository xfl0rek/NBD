package pl.nbd.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.DecoratorRoomRepository;
import pl.nbd.repository.RedisRoomRepository;
import pl.nbd.repository.RoomRepository;

import java.util.List;

public class RedisRoomTest {


    @Test
    public void createAndReadRoomFromRedis() {
        Room room = new RoomChildren(1, 100, 4, 2);
        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        redisRoomRepository.create(room);

        Room roomFromRedis = redisRoomRepository.read(1);
        Assertions.assertEquals(room, roomFromRedis);
    }

    @Test
    public void readAllRoomsFromRedis() {
        Room room1 = new RoomChildren(1, 100, 4, 2);
        Room room2 = new RoomRegular(2, 50, 2, true);
        Room room3 = new RoomChildren(3, 150, 5, 1);

        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        redisRoomRepository.create(room1);
        redisRoomRepository.create(room2);
        redisRoomRepository.create(room3);


        List<Room> rooms = redisRoomRepository.readAll();

        Assertions.assertEquals(3, rooms.size());
        Assertions.assertEquals(room1, rooms.get(0));
        Assertions.assertEquals(room2, rooms.get(1));
        Assertions.assertEquals(room3, rooms.get(2));
    }

    @Test
    public void clearCacheTest() {
        Room room1 = new RoomChildren(1, 100, 4, 2);
        Room room2 = new RoomRegular(2, 50, 2, true);
        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        redisRoomRepository.create(room1);
        redisRoomRepository.create(room2);

        redisRoomRepository.clearCache();

        Assertions.assertEquals(0, redisRoomRepository.readAll().size());
    }

    @Test
    public void loseConnectionWithRedis() {
        Room room = new RoomChildren(1, 100, 4, 2);
        RedisRoomRepository redisRoomRepository = new RedisRoomRepository();
        RoomRepository roomRepository = new RoomRepository();
        DecoratorRoomRepository decoratorRoomRepository = new DecoratorRoomRepository(roomRepository, redisRoomRepository);
        decoratorRoomRepository.create(room);
        redisRoomRepository.close();
        Assertions.assertEquals(1, decoratorRoomRepository.readAll().size());
        roomRepository.getDatabase().getCollection("rooms", Room.class).drop();
    }
}
