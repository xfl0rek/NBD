package pl.nbd.managers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Address;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;
import pl.nbd.repository.ClientRepository;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        clientRepository = new ClientRepository(entityManager);
        clientManager = new ClientManager(clientRepository);
    }

    @Test
    void registerClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        Address address2 = new Address("FC", "Barcelona", "10");
        Client client1 = new PremiumClient(123456789, "Cristiano", "Ronaldo", address1);
        Client client2 = new DefaultClient(987654321, "Leo", "Messi", address2);

        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");

        Client readClient1 = clientManager.getClient(123456789);
        Client readClient2 = clientManager.getClient(987654321);

        assertEquals(client1, readClient1);
        assertEquals(client2, readClient2);

    }


    @Test
    void deleteClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        Client readClient1 = clientManager.getClient(123456789);
        Assertions.assertNotNull(readClient1);
        clientManager.deleteClient(123456789);
        Client readClient2 = clientManager.getClient(123456789);
        Assertions.assertNull(readClient2);
    }

    @Test
    void updateClientInformation() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.updateClientInformation(123456789, "Crist", "Ronaldo", address1, "premium");
        Client client2 = clientManager.getClient(123456789);
        Assertions.assertEquals(client2.getFirstName(), "Crist");
    }

    @Test
    void unregisterClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.unregisterClient(clientManager.getClient(123456789));
        Client readClient1 = clientManager.getClient(123456789);
        Assertions.assertTrue(readClient1.isArchive());
    }
}