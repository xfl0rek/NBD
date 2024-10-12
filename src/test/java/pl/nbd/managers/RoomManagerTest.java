package pl.nbd.managers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.RoomRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoomManagerTest {
    private static RoomRepository roomRepository;
    private static RoomManager roomManager;

    @BeforeAll
    public static void setUp() {
        roomRepository = new RoomRepository();
        roomManager = new RoomManager(roomRepository);
    }

    @Test
    void registerRoom() {
        roomManager.registerRoom(1000, 10, 2, 3);
        Room expectedRoom = roomManager.getRoom(10);
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        assertEquals(expectedRoom, room);

        roomManager.registerRoom(1000, 80, 2, true);
        Room expectedRoom2 = roomManager.getRoom(80);
        RoomRegular roomRegular = new RoomRegular(1000, 80, 2, true);
        assertEquals(expectedRoom2, roomRegular);
    }

    @Test
    void deleteRoom() {
        roomManager.registerRoom(1000, 10, 2, 3);
        roomManager.deleteRoom(10);
        assertNull(roomManager.getRoom(10));
    }

    @Test
    void updateRoomInformation() {
        roomManager.registerRoom(1000, 10, 2, 3);
        Room expectedRoom = roomManager.getRoom(10);
        roomManager.updateRoomInformation(10, 9999, 3, 3);
        assertNotEquals(roomManager.getRoom(10), expectedRoom);

        roomManager.registerRoom(1000, 15, 2, true);
        Room expectedRoom2 = roomManager.getRoom(15);
        roomManager.updateRoomInformation(15, 9999, 2, false);
        assertNotEquals(roomManager.getRoom(15), expectedRoom2);
    }
}