package pl.nbd.managers;

import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.repository.RedisRoomRepository;

public class RedisRoomTest {

    @Test
    public void test() {
        Room room = new Room(1, 100, 4);
        RedisRoomRepository rep = new RedisRoomRepository();

        rep.save(room);
        Room room1 = rep.findByRoomNumber(1);

        System.out.println(room1.getRoomNumber());
        System.out.println(room1.getRoomCapacity());
        System.out.println(room1.getBasePrice());

    }
}
