package pl.nbd;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.nbd.model.Address;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.repository.ClientRepository;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("elo");
        Address address = new Address("Laczna", "Linpinki", "43");
        Client client = new DefaultClient(1, "Jadwiga", "Hymel", address);
        System.out.println("elo2");
        ClientRepository clientRepository = new ClientRepository();
        System.out.println("elo3");
        clientRepository.create(client);
        MongoCollection<Client> collection = clientRepository.read();
        client.setFirstName("Cristiano");
        clientRepository.update(client);
        System.out.println(collection.countDocuments());
        clientRepository.delete(1);
        System.out.println(collection.countDocuments());
    }


}
