package pl.nbd.managers;

import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.repository.RedisRoomRepository;

public class RedisRoomTest {

    @Test
    public void test() {
        Room room = new RoomChildren(1, 100, 4, 2);
        RedisRoomRepository rep = new RedisRoomRepository();
        System.out.println(room.getClass());
        rep.save(room);
        RoomChildren room1 = (RoomChildren) rep.findByRoomNumber(1);

        System.out.println(room1.getRoomNumber());
        System.out.println(room1.getRoomCapacity());
        System.out.println(room1.getBasePrice());

    }
}
