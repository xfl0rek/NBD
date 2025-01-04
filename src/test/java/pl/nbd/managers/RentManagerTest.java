package pl.nbd.managers;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nbd.model.*;
import pl.nbd.repository.AbstractCassandraRepository;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;
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

    @BeforeEach
    void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        clientRepository = new ClientRepository(session);
        roomRepository = new RoomRepository(session);
        rentRepository = new RentRepository(session);
        clientManager = new ClientManager(clientRepository);
        roomManager = new RoomManager(roomRepository);
        rentManager = new RentManager(rentRepository);
    }

    @AfterEach
    void dropDB() {
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

        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);

        //Rent readRent = rentManager.getRent(1);

        List<Rent> rents = rentManager.findRentsByClientId(1);
        Rent rent1 = rents.get(0);

        assertEquals(rent, rents.get(0));
    }

//    @Test
//    void endRentTest() {
//        Address address = new Address("Laczna", "Lipinki", "43");
//        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
//        Room room = new RoomRegular(1, 100, 2, true);
//        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        roomManager.registerRoom(1, 100, 2, true);
//        rentManager.rentRoom(1, client, room, startDate);
//        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
//        Rent rent = rentManager.getRent(1);
//        assertEquals(3, rent.getRentDays());
//        assertEquals(300, rent.getRentCost());
//
//        Address address2 = new Address("Polna", "Warszawa", "11");
//        Client client2 = new DefaultClient(2, "Jan", "Robak", address2);
//        clientManager.registerClient(2, "Jan", "Robak", address2, "default");
//        assertDoesNotThrow(() -> rentManager.rentRoom(2, client2, room, LocalDateTime.now()));
//    }

//    @Test
//    void rentOccupiedRoomTest() {
//        Address address = new Address("Laczna", "Lipinki", "43");
//        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
//        Room room = new RoomRegular(1, 100, 2, true);
//        Address address2 = new Address("Polna", "Warszawa", "11");
//        Client client2 = new DefaultClient(2, "Jan", "Robak", address2);
//
//        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        clientManager.registerClient(2, "Jan", "Robak", address2, "default");
//        roomManager.registerRoom(1, 100, 2, true);
//        rentManager.rentRoom(1, client, room, startDate);
//        assertThrows(MongoWriteException.class, () -> rentManager.rentRoom(2, client2, room, startDate));
//    }

//    @Test
//    void deleteRentTest() {
//        Address address = new Address("Laczna", "Lipinki", "43");
//        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
//        Room room = new RoomRegular(1, 100, 2, true);
//
//        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        roomManager.registerRoom(1, 100, 2, true);
//        rentManager.rentRoom(1, client, room, startDate);
//        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
//        rentManager.deleteRent(1);
//        assertNull(rentManager.getRent(1));
//    }

//    @Test
//    void updateRentTest() {
//        Address address = new Address("Laczna", "Lipinki", "43");
//        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
//        Room room = new RoomRegular(1, 100, 2, true);
//        Address address2 = new Address("Polna", "Warszawa", "11");
//        Client client2 = new DefaultClient(2, "Jan", "Robak", address2);
//        LocalDateTime startDate = LocalDateTime.now();
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        clientManager.registerClient(2, "Jan", "Robak", address2, "default");
//        roomManager.registerRoom(1, 100, 2, true);
//        rentManager.rentRoom(1, client, room, startDate);
//        rentManager.update(1, client2, room, startDate);
//        assertEquals("Jan", rentManager.getRent(1).getClient().getFirstName());
//    }
}