package pl.nbd.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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

    private boolean clientExists(int personalID) {
        MongoCollection<Client> collection;
        collection = clientRepository.readAll();
        ArrayList<Client> clients = collection.find().into(new ArrayList<>());
        for (Client client : clients) {
            if (client.getPersonalID() == personalID) {
                return true;
            }
        }
        return false;
    }

    public Client getClient(int personalID) {
        return this.clientRepository.read(personalID);
    }

    public MongoCollection<Client> getAllClients() {
        return clientRepository.readAll();
    }

    public void registerClient(int personalID, String firstName, String lastName, Address address, String type) {
        if (!clientExists(personalID)) {
            if (type.equals("default")) {
                Client newClient = new DefaultClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            } else if (type.equals("premium")) {
                Client newClient = new PremiumClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            }
        }
    }

    public void deleteClient(int personalID) {
        if (clientExists(personalID)) {
            clientRepository.delete(personalID);
        }
    }

    public void updateClientInformation(int personalID, String firstName, String lastName, Address address, String type) {
        if (clientExists(personalID)) {
            Client client;
            if (type.equals("default")) {
                client = new DefaultClient(personalID, firstName, lastName, address);
            } else if (type.equals("premium")) {
                client = new PremiumClient(personalID, firstName, lastName, address);
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
            clientRepository.update(client);
        }
    }

    public void unregisterClient(int personalID) {
        MongoCollection<Client> collection = clientRepository.readAll();
        Client client = collection.find(Filters.eq("_id", personalID)).first();
        if (client != null) {
            client.setArchive(true);
            clientRepository.update(client);
        }
    }
}
