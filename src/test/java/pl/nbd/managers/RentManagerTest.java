package pl.nbd.managers;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nbd.model.*;
import pl.nbd.repository.AbstractCassandraRepository;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {

    private static CqlSession session;
    public static ClientRepository clientRepository;
    public static RoomRepository roomRepository;
    public static RentRepository rentRepository;
    public static ClientManager clientManager;
    public static RoomManager roomManager;
    public static RentManager rentManager;

    public static Client client;
    public static Room room;

    @BeforeEach
    void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        clientRepository = new ClientRepository(session);
        roomRepository = new RoomRepository(session);
        rentRepository = new RentRepository(session);
        clientManager = new ClientManager(clientRepository);
        roomManager = new RoomManager(roomRepository);
        rentManager = new RentManager(rentRepository, clientRepository, roomRepository);

        client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        room = new RoomRegular(1, 100, 2, true);

        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        roomManager.registerRoom(1, 100, 2, true);
    }

    @AfterEach
    void dropDB() {
//        Truncate truncate = QueryBuilder.truncate("clients");
//        session.execute(truncate.build());
//        Truncate truncate2 = QueryBuilder.truncate("rooms");
//        session.execute(truncate2.build());
        roomRepository.update(new RoomRegular(1, 100, 2, true));
        Truncate truncate3 = QueryBuilder.truncate("rents_by_client");
        session.execute(truncate3.build());
        Truncate truncate4 = QueryBuilder.truncate("rents_by_room");
        session.execute(truncate4.build());
    }

    @AfterAll
    static void clear() {
        Truncate truncate = QueryBuilder.truncate("clients");
        session.execute(truncate.build());
        Truncate truncate2 = QueryBuilder.truncate("rooms");
        session.execute(truncate2.build());
        Truncate truncate3 = QueryBuilder.truncate("rents_by_client");
        session.execute(truncate3.build());
        Truncate truncate4 = QueryBuilder.truncate("rents_by_room");
        session.execute(truncate4.build());
    }

    @Test
    void rentRoomTest() {
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(1, client, room, startDate);

//        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
//        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);

        List<Rent> rents = rentManager.findRentsByClientId(1);

        assertEquals(rent, rents.get(0));
    }

    @Test
    void endRentTest() {

        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
//        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
        Rent rent = rentManager.findRentsByClientId(1).get(0);
        assertEquals(3, rent.getRentDays());
        assertEquals(300, rent.getRentCost());

        Room room2 = roomManager.getRoom(1);
        Client client2 = new DefaultClient(2, "Jan", "Robak", false);
        clientManager.registerClient(2, "Jan", "Robak", "default", false);
        assertDoesNotThrow(() -> rentManager.rentRoom(2, client2, room2, LocalDateTime.now()));
    }

    @Test
    void rentOccupiedRoomTest() {
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Room room = new RoomRegular(1, 100, 2, true);
        Client client2 = new DefaultClient(2, "Jan", "Robak", false);

        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
//        clientManager.registerClient(2, "Jan", "Robak", "default", false);
//        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        assertThrows(IllegalArgumentException.class, () -> rentManager.rentRoom(2, client2, room, startDate));
    }

    @Test
    void deleteRentTest() {
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Room room = new RoomRegular(1, 100, 2, true);

        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
//        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
        rentManager.deleteRent(1);
        assertTrue(rentManager.findRentsByClientId(1).isEmpty());
    }

    @Test
    void updateRentTest() {
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Room room = new RoomRegular(1, 100, 2, true);
        Client client2 = new DefaultClient(2, "Jan", "Robak", false);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(3);
//        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
//        clientManager.registerClient(2, "Jan", "Robak", "default", false);
//        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.update(1, client, room, startDate, endDate);
        assertEquals(endDate.truncatedTo(ChronoUnit.MINUTES), rentManager.findRentsByClientId(1).get(0).getEndTime().truncatedTo(ChronoUnit.MINUTES));
    }
}