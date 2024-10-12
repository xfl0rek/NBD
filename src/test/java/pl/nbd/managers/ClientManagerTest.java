package pl.nbd.managers;

import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Address;
import pl.nbd.model.Client;
import pl.nbd.repository.ClientRepository;
import org.junit.jupiter.api.BeforeAll;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    private static ClientRepository clientRepository;
    private static ClientManager clientManager;

    @BeforeAll
    public static void setUp() {
        clientRepository = new ClientRepository();
        clientManager = new ClientManager(clientRepository);
    }

    @Test
    void registerClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        Address address2 = new Address("FC", "Barcelona", "10");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        Client client2 = clientManager.registerClient(987654321, "Leo", "Messi", address2, "default");

        Client readClient1 = clientManager.getClient(123456789);
        Client readClient2 = clientManager.getClient(987654321);

        assertEquals(client1, readClient1);
        assertEquals(client2, readClient2);

    }


    @Test
    void deleteClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        Client readClient1 = clientManager.getClient(123456789);
        Assertions.assertNotNull(readClient1);
        clientManager.deleteClient(123456789);
        Client readClient2 = clientManager.getClient(123456789);
        Assertions.assertNull(readClient2);
    }

    @Test
    void updateClientInformation() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.updateClientInformation(123456789, "Crist", "Ronaldo", address1, "premium");
        Client client2 = clientManager.getClient(123456789);
        Assertions.assertEquals(client2.getFirstName(), "Crist");
    }

    @Test
    void unregisterClient() {
        Address address1 = new Address("Real", "Madryt", "7");
        Client client1 = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address1, "premium");
        clientManager.deleteClient(123456789);
        Client readClient1 = clientManager.getClient(123456789);
        Assertions.assertNull(readClient1);
    }
}