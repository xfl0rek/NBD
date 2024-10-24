package pl.nbd.managers;

import jakarta.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.*;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {
    private static RentRepository rentRepository;
    private static RentManager rentManager;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;
    private static RoomRepository roomRepository;
    private static RoomManager roomManager;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUp() {
        entityManagerFactory = jakarta.persistence.Persistence.createEntityManagerFactory("default");
        rentRepository = new RentRepository(entityManagerFactory);
        rentManager = new RentManager(rentRepository);
        clientRepository = new ClientRepository(entityManagerFactory);
        clientManager = new ClientManager(clientRepository);
        roomRepository = new RoomRepository(entityManagerFactory);
        roomManager = new RoomManager(roomRepository);
    }

    @Test
    void rentRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(10, 1000, 2, 3);
        RoomChildren room = new RoomChildren(10, 1000, 2, 3);
        LocalDateTime date = LocalDateTime.now();

        Client client1 = clientManager.getClient(123456789);
        try {
            rentManager.rentRoom(2, client1, room, date);

            Rent rent = rentManager.getRent(2);
            Rent rent1 = new Rent(2, client1, room, date);


            Assertions.assertEquals(rent, rent1);
        }
        catch (Exception e) {}
    }

    @Test
    void returnRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(11, 1000, 2, 3);
        RoomChildren room = new RoomChildren(10, 1000, 2, 3);
        Client client1 = clientManager.getClient(123456789);
        try {
            rentManager.rentRoom(3, client1, room, LocalDateTime.now());
            Rent rent = rentManager.getRent(1);
            rentManager.returnRoom(3, LocalDateTime.now().plusDays(7));
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
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");
        RoomChildren room = new RoomChildren(12, 1000, 2, 3);
        Client client1 = clientManager.getClient(123456789);
        Client client2 = clientManager.getClient(987654321);
        try {
            rentManager.rentRoom(4, client1, room, LocalDateTime.now());
            assertThrows(Exception.class, () -> {
                rentManager.rentRoom(5, client2, room, LocalDateTime.now());});
        } catch (Exception e) {}


    }

    @Test
    void getRent() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(13, 1000, 2, 3);
        Client client1 = clientManager.getClient(123456789);
        try {
            rentManager.rentRoom(6, client1, roomManager.getRoom(10), LocalDateTime.now());
            Rent expectedRent = new Rent(6, client1, roomManager.getRoom(10), LocalDateTime.now());

            assertEquals(rentManager.getRent(6), expectedRent);
        } catch (Exception e) {}
    }

    @Test
    void concurrentRentTest() {
        Address address1 = new Address("Real", "Madryt", "7");
        Address address2 = new Address("FC", "Barcelona", "10");
        Client client1 = new PremiumClient(123456789, "Cristiano", "Ronaldo", address1);
        Client client2 = new DefaultClient(987654321, "Leo", "Messi", address2);
        long roomId = 14;


        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");
        roomManager.registerRoom(14, 1000, 2, 3);


        Map<String,Object> properties = new HashMap<String,Object>();
        properties.put("javax.persistence.lock.timeout", 0);

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        entityManager2.getTransaction().begin();

        Room room = entityManager2.find(Room.class, roomId, LockModeType.PESSIMISTIC_WRITE, properties);

        assertThrows(RuntimeException.class, () -> {
            rentManager.rentRoom(7, client2, room, LocalDateTime.now());
        });

        Rent rent = new Rent(7, client1, room, LocalDateTime.now());
        entityManager2.persist(rent);
        entityManager2.getTransaction().commit();


        Rent rent1 = new Rent(7, client1, room, LocalDateTime.now());
        Rent rent2 = rentManager.getRent(7);
        assertEquals(rent1, rent2);
    }
}