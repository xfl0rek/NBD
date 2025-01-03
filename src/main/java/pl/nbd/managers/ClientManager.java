package pl.nbd.managers;

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
        return clientRepository.read(personalID) != null;
    }

    public Client getClient(int personalID) {
        return this.clientRepository.read(personalID);
    }

//    public MongoCollection<Client> getAllClients() {
//        return clientRepository.readAll();
//    }

    public void registerClient(int personalID, String firstName, String lastName, String type, boolean isArchive) {
        if (!clientExists(personalID)) {
            if (type.equals("default")) {
                Client newClient = new DefaultClient(personalID, firstName, lastName, isArchive);
                clientRepository.create(newClient);
            } else if (type.equals("premium")) {
                Client newClient = new PremiumClient(personalID, firstName, lastName, isArchive);
                clientRepository.create(newClient);
            }
        }
    }

    public void deleteClient(int personalID) {
        if (clientExists(personalID)) {
            clientRepository.delete(personalID);
        }
    }

    public void updateClientInformation(int personalID, String firstName, String lastName, String type, boolean isArchive) {
        if (clientExists(personalID)) {
            Client client;
            if (type.equals("default")) {
                client = new DefaultClient(personalID, firstName, lastName, isArchive);
            } else if (type.equals("premium")) {
                client = new PremiumClient(personalID, firstName, lastName, isArchive);
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
            clientRepository.update(client);
        }
    }

    public void unregisterClient(int personalID) {
        Client client = clientRepository.read(personalID);
        if (client != null) {
            client.setArchive(true);
            clientRepository.update(client);
        }
    }
}
