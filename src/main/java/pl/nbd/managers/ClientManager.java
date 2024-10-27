package pl.nbd.managers;

import com.mongodb.client.MongoCollection;
import pl.nbd.model.Address;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;
import pl.nbd.repository.ClientRepository;

import java.util.ArrayList;

public class ClientManager {
    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new NullPointerException("clientRepository is null");
        } else {
            this.clientRepository = clientRepository;
        }
    }

    public void registerClient(int personalID, String firstName, String lastName, Address address, String type) {
        MongoCollection<Client> collection;
        collection = clientRepository.read();
        ArrayList<Client> clients = collection.find().into(new ArrayList<>());
        boolean clientExists = false;
        for (Client client : clients) {
            if (client.getPersonalID() == personalID) {
                clientExists = true;
                break;
            }
        }

        if (!clientExists) {
            if (type.equals("default")) {
                Client newClient = new DefaultClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            } else if (type.equals("premium")) {
                Client newClient = new PremiumClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            }
        }
    }
}
