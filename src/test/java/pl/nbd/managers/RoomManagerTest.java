package pl.nbd.managers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.mongodb.client.MongoCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.AbstractCassandraRepository;
import pl.nbd.repository.RoomRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoomManagerTest {

    public static RoomRepository roomRepository;
    public static RoomManager roomManager;

//    @BeforeAll
//    static void setUp() {
//        roomRepository = new RoomRepository();
//        roomManager = new RoomManager(roomRepository);
//    }
//
//    @AfterEach
//    void dropDB() {
//        MongoCollection<Room> collection = roomManager.getAllRooms();
//        collection.drop();
//    }

    @Test
    void test() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        CqlSession session = abstractCassandraRepository.getSession();
        roomRepository = new RoomRepository(session);
    }

    @Test
    void registerRoomTest() {
        Room room = new RoomChildren(1, 100, 2, 2);
        Room room2 = new RoomRegular(2, 150, 4, true);
        roomManager.registerRoom(1, 100, 2, 2);
        roomManager.registerRoom(2, 150, 4, true);

        Room readRoom = roomManager.getRoom(1);
        Room readRoom2 = roomManager.getRoom(2);

        assertEquals(room, readRoom);
        assertEquals(room2, readRoom2);
    }

    @Test
    void deleteRoomTest() {
        roomManager.registerRoom(1, 100, 2, true);
        Room room = roomManager.getRoom(1);
        assertNotNull(room);

        roomManager.deleteRoom(1);
        Room room2 = roomManager.getRoom(1);
        assertNull(room2);
    }


    @Test
    void  updateRoomTest() {
        roomManager.registerRoom(1, 100, 2, 2);
        roomManager.registerRoom(2, 150, 4, true);
        Room room = roomManager.getRoom(1);
        Room room2 = roomManager.getRoom(2);
        assertEquals(100, room.getBasePrice());
        assertEquals(4, room2.getRoomCapacity());


        roomManager.updateRoomInformation(1, 200, 2, 2);
        roomManager.updateRoomInformation(2, 150, 3, true);
        Room updatedRoom = roomManager.getRoom(1);
        Room updatedRoom2 = roomManager.getRoom(2);
        assertEquals(200, updatedRoom.getBasePrice());
        assertEquals(3, updatedRoom2.getRoomCapacity());
    }

}