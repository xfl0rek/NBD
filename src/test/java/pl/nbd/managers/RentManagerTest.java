package pl.nbd.managers;

import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;
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
        EntityManagerFactory entityManagerFactory = jakarta.persistence.Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        rentRepository = new RentRepository(entityManager);
        rentManager = new RentManager(rentRepository);
        clientRepository = new ClientRepository(entityManager);
        clientManager = new ClientManager(clientRepository);
        roomRepository = new RoomRepository(entityManager);
        roomManager = new RoomManager(roomRepository);
    }

    @Test
    void rentRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(1000, 10, 2, 3);
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);

        LocalDateTime date = LocalDateTime.now();

        Client client1 = clientManager.getClient(123456789);
        try {
            rentManager.rentRoom(1, client1, room, date);

            Rent rent = rentManager.getRent(1);
            Rent rent1 = new Rent(1, client1, room, date);


            Assertions.assertEquals(rent, rent1);
        }
        catch (Exception e) {}
    }

    @Test
    void returnRoom() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        roomManager.registerRoom(1000, 10, 2, 3);
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        Client client1 = clientManager.getClient(123456789);
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
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");
        RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        Client client1 = clientManager.getClient(123456789);
        Client client2 = clientManager.getClient(987654321);
        try {
            rentManager.rentRoom(1, client1, room, LocalDateTime.now());
            assertThrows(Exception.class, () -> {
                rentManager.rentRoom(2, client2, room, LocalDateTime.now());});
        } catch (Exception e) {}


    }

    @Test
    void getRent() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        //RoomChildren room = new RoomChildren(1000, 10, 2, 3);
        roomManager.registerRoom(1000, 10, 2, 3);
        Client client1 = clientManager.getClient(123456789);
        try {
            rentManager.rentRoom(1, client1, roomManager.getRoom(10), LocalDateTime.now());
            Rent expectedRent = new Rent(1, client1, roomManager.getRoom(10), LocalDateTime.now());

            assertEquals(rentManager.getRent(1), expectedRent);
        } catch (Exception e) {}
    }

   //Test nie dziala, zawiesza sie w 132 linii, przyczyna - powstaje 2 entity maneger w rentRepository i nie jest rzucany wyjatek tylko mieli
    @Test
    void concurrentRentTest() {
        Address address1 = new Address("Real", "Madryt", "7");
        Address address2 = new Address("FC", "Barcelona", "10");
        Client client1 = new PremiumClient(123456789, "Cristiano", "Ronaldo", address1);
        Client client2 = new DefaultClient(987654321, "Leo", "Messi", address2);

        long roomId = 10;

        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
            EntityManager entityManager = entityManagerFactory.createEntityManager()
        ) {
            clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
            clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");
            roomManager.registerRoom(1000, 10, 2, 3);


            entityManager.getTransaction().begin();

            entityManager.persist(client1);

            Room room = entityManager.find(Room.class, roomId, LockModeType.PESSIMISTIC_WRITE);
            System.out.println(room.getRoomNumber());

            assertThrows(PessimisticLockException.class, () -> {
                rentManager.rentRoom(2, client2, room, LocalDateTime.now());
            });

            Rent rent = new Rent(1, client1, room, LocalDateTime.now());
            entityManager.persist(rent);
            entityManager.getTransaction().commit();


        }
        Room room = roomManager.getRoom(10);
        Rent rent = new Rent(1, client1, room, LocalDateTime.now());
        Rent rent2 = rentManager.getRent(1);
        System.out.println(rent2.getId());
        assertEquals(rent, rent2);
    }
}