package pl.nbd.managers;

import com.mongodb.client.MongoCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.*;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {

    public static ClientRepository clientRepository;
    public static RoomRepository roomRepository;
    public static RentRepository rentRepository;
    public static ClientManager clientManager;
    public static RoomManager roomManager;
    public static RentManager rentManager;

    @BeforeAll
    static void setUp() {
        clientRepository = new ClientRepository();
        roomRepository = new RoomRepository();
        rentRepository = new RentRepository();
        clientManager = new ClientManager(clientRepository);
        roomManager = new RoomManager(roomRepository);
        rentManager = new RentManager(rentRepository);
    }

    @AfterEach
    void dropDB() {
        MongoCollection<Rent> rentCollection = rentManager.getAllRents();
        MongoCollection<Room> roomCollection = roomManager.getAllRooms();
        MongoCollection<Client> clientCollection = clientManager.getAllClients();
        rentCollection.drop();
        roomCollection.drop();
        clientCollection.drop();
    }

    @Test
    void rentRoomTest() {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        Rent rent = new Rent(1, client, room, startDate);

        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);

        Rent readRent = rentManager.getRent(1);

        assertEquals(rent, readRent);
    }

    @Test
    void endRentTest() {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        LocalDateTime startDate = LocalDateTime.now();
        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
        Rent rent = rentManager.getRent(1);
        assertEquals(3, rent.getRentDays());
        assertEquals(300, rent.getRentCost());
    }

    @Test
    void rentOccupiedRoomTest() {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        Address address2 = new Address("Polna", "Warszawa", "11");
        Client client2 = new DefaultClient(2, "Jan", "Robak", address2);

        LocalDateTime startDate = LocalDateTime.now();
        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
        clientManager.registerClient(2, "Jan", "Robak", address2, "default");
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        assertThrows(IllegalArgumentException.class, () -> rentManager.rentRoom(2, client2, room, startDate));
    }

    @Test
    void deleteRentTest() {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);

        LocalDateTime startDate = LocalDateTime.now();
        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.returnRoom(1, LocalDateTime.now().plusDays(3));
        rentManager.deleteRent(1);
        assertNull(rentManager.getRent(1));
    }

    @Test
    void updateRentTest() {
        Address address = new Address("Laczna", "Lipinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        Room room = new RoomRegular(1, 100, 2, true);
        Address address2 = new Address("Polna", "Warszawa", "11");
        Client client2 = new DefaultClient(2, "Jan", "Robak", address2);

        LocalDateTime startDate = LocalDateTime.now();
        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
        clientManager.registerClient(2, "Jan", "Robak", address2, "default");
        roomManager.registerRoom(1, 100, 2, true);
        rentManager.rentRoom(1, client, room, startDate);
        rentManager.update(1, client2, room, startDate);
        assertEquals("Jan", rentManager.getRent(1).getClient().getFirstName());
    }

    @Test
    void concurrentRentTest() {

    }
}