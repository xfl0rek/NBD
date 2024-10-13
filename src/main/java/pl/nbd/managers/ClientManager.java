package pl.nbd.managers;

import pl.nbd.model.Address;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;
import pl.nbd.repository.ClientRepository;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void registerClient(long personalID, String firstName, String lastName, Address address, String type) {
        if (clientRepository.read(personalID) == null) {
            if (type.equals("default")) {
                Client newClient = new DefaultClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            } else if (type.equals("premium")) {
                Client newClient = new PremiumClient(personalID, firstName, lastName, address);
                clientRepository.create(newClient);
            }
        }
        Client existingClient = clientRepository.read(personalID);
        existingClient.setFirstName(firstName);
        existingClient.setLastName(lastName);
        existingClient.setAddress(address);
    }

    public Client getClient(long personalID) {
        return clientRepository.read(personalID);
    }

    public void deleteClient(long personalID) {
        if (clientRepository.read(personalID) != null) {
            clientRepository.delete(clientRepository.read(personalID));
        }
    }

    public void updateClientInformation(long personalID, String firstName, String lastName, Address address, String type) {
        if (clientRepository.read(personalID) != null) {
            if (type.equals("default")) {
                Client existingClient = clientRepository.read(personalID);
                existingClient.setFirstName(firstName);
                existingClient.setLastName(lastName);
                existingClient.setAddress(address);
                clientRepository.update(existingClient);
            } else if (type.equals("premium")) {
                Client existingClient = clientRepository.read(personalID);
                existingClient.setFirstName(firstName);
                existingClient.setLastName(lastName);
                existingClient.setAddress(address);
                clientRepository.update(existingClient);
            }
        }
    }

    public void unregisterClient(Client client) {
        if (client != null) {
            client.setArchive(true);
            clientRepository.update(client);
        }
    }
}
