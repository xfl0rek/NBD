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
        MongoCollection<DefaultClient> collection = clientRepository.read();
        client.setFirstName("Cristiano");
        clientRepository.update(client);
        collection = clientRepository.read();
        ArrayList<DefaultClient> clients = collection.find().into(new ArrayList<>());
        System.out.println(clients.get(0).getFirstName());
        System.out.println(clients.get(0).getAddress().getStreet());
        System.out.println(collection.countDocuments());
        clientRepository.delete(1);
        System.out.println(collection.countDocuments());
    }


}
