package pl.nbd.managers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;
import pl.nbd.repository.AbstractCassandraRepository;
import pl.nbd.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    private static CqlSession session;
    private static ClientRepository clientRepository;
    private static ClientManager clientManager;


    @BeforeAll
    public static void setUp() {
        AbstractCassandraRepository abstractCassandraRepository = new AbstractCassandraRepository();
        session = abstractCassandraRepository.getSession();
        clientRepository = new ClientRepository(session);
        clientManager = new ClientManager(clientRepository);
    }

    @AfterEach
    void dropDB() {
        Truncate truncate = QueryBuilder.truncate("clients");
        session.execute(truncate.build());
    }

    @Test
    void registerClientTest() {
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", false);
        Client client2 = new PremiumClient(2, "Jan", "Robak", false);

        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        clientManager.registerClient(2, "Jan", "Robak", "premium", false);

        Client readClient = clientManager.getClient(1);
        Client readClient2 = clientManager.getClient(2);

        Assertions.assertEquals(client, readClient);
        Assertions.assertEquals(client2, readClient2);
    }

    @Test
    void deleteClient() {
        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        Client client = clientManager.getClient(1);
        Assertions.assertNotNull(client);
        clientManager.deleteClient(1);
        Client client1 = clientManager.getClient(1);
        Assertions.assertNull(client1);
    }

    @Test
    void updateClientInformation() {
        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        Client client = clientManager.getClient(1);
        Assertions.assertEquals("Jadwiga", client.getFirstName());

        clientManager.updateClientInformation(1, "Syn", "Hymel", "default", false);
        Client client1 = clientManager.getClient(1);
        Assertions.assertEquals("Syn", client1.getFirstName());
    }

    @Test
    void unregisterClient() {
        clientManager.registerClient(1, "Jadwiga", "Hymel", "default", false);
        Client client = clientManager.getClient(1);
        assertFalse(client.isArchive());

        clientManager.unregisterClient(1);
        Client client1 = clientManager.getClient(1);
        assertTrue(client1.isArchive());

    }
}