package pl.nbd.managers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
        EntityManagerFactory entityManagerFactory = jakarta.persistence.Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        roomRepository = new RoomRepository(entityManager);
        roomManager = new RoomManager(roomRepository);
    }

    @Test
    void registerRoom() {
        roomManager.registerRoom(10, 1000, 2, 3);
        Room expectedRoom = roomManager.getRoom(10);
        RoomChildren room = new RoomChildren(10, 1000, 2, 3);
        assertEquals(expectedRoom, room);

        roomManager.registerRoom(80, 1000, 2, true);
        Room expectedRoom2 = roomManager.getRoom(80);
        RoomRegular roomRegular = new RoomRegular(80, 1000, 2, true);
        assertEquals(expectedRoom2, roomRegular);
    }

    @Test
    void deleteRoom() {
        roomManager.registerRoom(10, 1000, 2, 3);
        roomManager.deleteRoom(10);
        assertNull(roomManager.getRoom(10));
    }

    @Test
    void updateRoomInformation() {
        roomManager.registerRoom(10, 1000, 2, 3);
//        Room expectedRoom = roomManager.getRoom(10);
        roomManager.updateRoomInformation(10, 9999, 3, 3);
        Room expectedRoom = roomManager.getRoom(10);
        assertEquals(expectedRoom.getBasePrice(), 9999);

        roomManager.registerRoom(15, 1000, 2, true);

        roomManager.updateRoomInformation(15, 9999, 2, false);
        Room readRoom =  roomManager.getRoom(15);
        RoomRegular expectedRoom2 = (RoomRegular) readRoom;
        assertEquals(expectedRoom2.isBreakfastIncluded(), false);
    }
}