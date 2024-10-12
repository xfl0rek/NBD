package pl.nbd.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.*;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {
    private static RentRepository rentRepository;
    private static RentManager rentManager;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;
    private static RoomRepository roomRepository;
    private static RoomManager roomManager;

    @BeforeAll
    public static void setUp() {
        rentRepository = new RentRepository();
        rentManager = new RentManager(rentRepository);
        clientRepository = new ClientRepository();
        clientManager = new ClientManager(clientRepository);
        roomRepository = new RoomRepository();
        roomManager = new RoomManager(roomRepository);
    }

    @Test
    void rentRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(1000, 10, 2, 3);
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        //Room room = roomManager.getRoom(10);
        LocalDateTime date = LocalDateTime.now();
        //LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        try {
            rentManager.rentRoom(1, client1, room, date);
            //rentManager.returnRoom(1, endDate);
            Rent rent = rentManager.getRent(1);
            Rent rent1 = new Rent(1, client1, room, date);
            //rent1.endRent(endDate);
            Assertions.assertEquals(rent, rent1);
        }
        catch (Exception e) {}
    }

    @Test
    void returnRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(1000, 10, 2, 3);
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        try {
            rentManager.rentRoom(1, client1, room, LocalDateTime.now());
            Rent rent = rentManager.getRent(1);
            rentManager.returnRoom(1, LocalDateTime.now().plusDays(7));
            Rent endRent = rentManager.getRent(1);

            assertEquals(endRent.getRentCost(), 7000);
            assertTrue(endRent.isArchive());

        }
        catch (Exception e) {}

    }

    @Test
    void rentOccupiedRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        Address address2 = new Address("FC", "Barcelona", "10");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        Client client2 = clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        try {
            rentManager.rentRoom(1, client1, room, LocalDateTime.now());
            assertThrows(Exception.class, () -> {
                rentManager.rentRoom(2, client2, room, LocalDateTime.now());});
        }
        catch (Exception e) {}



//        (JdbcWriteException.class, () -> {
//            file2.write(sudokuBoard);
//        });
    }

    @Test
    void getRent() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        //RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        roomManager.registerRoom(1000, 10, 2, 3);
        try {
            rentManager.rentRoom(1, client1, roomManager.getRoom(10), LocalDateTime.now());
            Rent expectedRent = new Rent(1, client1, roomManager.getRoom(10), LocalDateTime.now());

            assertEquals(rentManager.getRent(1), expectedRent);
        } catch (Exception e) {}
    }
}