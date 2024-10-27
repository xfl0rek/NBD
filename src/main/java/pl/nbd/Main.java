package pl.nbd;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.nbd.model.*;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;
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
        MongoCollection<DefaultClient> collection;
        collection = clientRepository.read();
        ArrayList<DefaultClient> clients = collection.find().into(new ArrayList<>());
        System.out.println(clients.get(0).getFirstName());
        System.out.println(clients.get(0).getAddress().getStreet());
        System.out.println(collection.countDocuments());
        client.setFirstName("Cristiano");
        clientRepository.update(client);
        ArrayList<DefaultClient> clients2 = collection.find().into(new ArrayList<>());
        System.out.println(clients2.get(0).getFirstName());
        System.out.println(clients2.get(0).getAddress().getStreet());
        System.out.println(collection.countDocuments());
        clientRepository.delete(1);
        System.out.println(collection.countDocuments());

        // Room

        Room room = new RoomRegular(1, 1000, 2, true);
        RoomRepository roomRepository = new RoomRepository();
        roomRepository.create(room);
        MongoCollection<RoomRegular> collection1;
        collection1 = roomRepository.read();
        ArrayList<RoomRegular> rooms = collection1.find().into(new ArrayList<>());
        System.out.println(rooms.get(0).getBasePrice());
        System.out.println(collection1.countDocuments());
        room.setBasePrice(9999);
        roomRepository.update(room);
        ArrayList<RoomRegular> rooms2 = collection1.find().into(new ArrayList<>());
        System.out.println(rooms2.get(0).getBasePrice());
        System.out.println(collection1.countDocuments());
        roomRepository.delete(1);
        System.out.println(collection1.countDocuments());

        // Rent

        Client newClient = new DefaultClient(123456789, "Syn", "Hymel", address);
        Room room2 = new RoomRegular(9, 1000, 2, true);
        Rent rent = new Rent(1, newClient, room2, LocalDateTime.now());
        RentRepository rentRepository = new RentRepository();
        rentRepository.create(rent);
        MongoCollection<Rent> collection2;
        collection2 = rentRepository.read();
        ArrayList<Rent> rents = collection2.find().into(new ArrayList<>());
        System.out.println(rents.get(0).getRentCost());
        System.out.println(collection2.countDocuments());
        rent.endRent(LocalDateTime.now().plusDays(7));
        System.out.println(rent.getEndTime());
        rentRepository.update(rent);
        ArrayList<Rent> rents1 = collection2.find().into(new ArrayList<>());
        System.out.println(rents1.get(0).getRentCost());
        System.out.println(rents1.get(0).getRentDays());
        System.out.println(rents1.get(0).getEndTime());
        rentRepository.delete(1);
        System.out.println(collection2.countDocuments());
    }
}
