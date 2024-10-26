package pl.nbd;

import com.mongodb.client.MongoCollection;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.repository.ClientRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("elo");

        Client client = new DefaultClient(2, "Jan", "Kowalski", null);
        System.out.println("elo2");
        ClientRepository clientRepository = new ClientRepository();
        System.out.println("elo3");
        clientRepository.create(client);
        MongoCollection<Client> collection = clientRepository.getCollection();
        System.out.println(collection.countDocuments());
    }


}
