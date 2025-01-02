//package pl.nbd.managers;
//
//import com.mongodb.client.MongoCollection;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import pl.nbd.model.Address;
//import pl.nbd.model.Client;
//import pl.nbd.model.DefaultClient;
//import pl.nbd.model.PremiumClient;
//import pl.nbd.repository.ClientRepository;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ClientManagerTest {
//
//    private static ClientRepository clientRepository;
//    private static ClientManager clientManager;
//
//
//    @BeforeAll
//    public static void setUp() {
//        clientRepository = new ClientRepository();
//        clientManager = new ClientManager(clientRepository);
//    }
//
//    @AfterEach
//    public void dropCollection() {
//        MongoCollection<Client> collection = clientManager.getAllClients();
//        collection.drop();
//    }
//
//    @Test
//    void registerClientTest() {
//        Address address = new Address("Laczna", "Lipniki", "43");
//        Address address2 = new Address("Dluga", "Warszawa", "11");
//        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
//        Client client2 = new PremiumClient(2, "Jan", "Robak", address2);
//
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        clientManager.registerClient(2, "Jan", "Robak", address2, "premium");
//
//        Client readClient = clientManager.getClient(1);
//        Client readClient2 = clientManager.getClient(2);
//
//        Assertions.assertEquals(client, readClient);
//        Assertions.assertEquals(client2, readClient2);
//    }
//
//    @Test
//    void deleteClient() {
//        Address address = new Address("Laczna", "Lipniki", "43");
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        Client client = clientManager.getClient(1);
//        Assertions.assertNotNull(client);
//        clientManager.deleteClient(1);
//        Client client1 = clientManager.getClient(1);
//        Assertions.assertNull(client1);
//    }
//
//    @Test
//    void updateClientInformation() {
//        Address address = new Address("Laczna", "Lipniki", "43");
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        Client client = clientManager.getClient(1);
//        Assertions.assertEquals("Jadwiga", client.getFirstName());
//
//        clientManager.updateClientInformation(1, "Syn", "Hymel", address, "default");
//        Client client1 = clientManager.getClient(1);
//        Assertions.assertEquals("Syn", client1.getFirstName());
//    }
//
//    @Test
//    void unregisterClient() {
//        Address address = new Address("Laczna", "Lipniki", "43");
//        clientManager.registerClient(1, "Jadwiga", "Hymel", address, "default");
//        Client client = clientManager.getClient(1);
//        assertFalse(client.isArchive());
//
//        clientManager.unregisterClient(1);
//        Client client1 = clientManager.getClient(1);
//        assertTrue(client1.isArchive());
//
//    }
//}