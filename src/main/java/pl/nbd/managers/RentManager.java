package pl.nbd.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import pl.nbd.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RentManager {
    private RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        if (rentRepository == null) {
            throw new NullPointerException("RentRepository is null");
        } else {
            this.rentRepository = rentRepository;
        }
    }

    private boolean rentExists(int id) {
        MongoCollection<Rent> collection = rentRepository.readAll();
        ArrayList<Rent> rents = collection.find().into(new ArrayList<>());
        for (Rent rent : rents) {
            if (rent.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Rent getRent(int id) {
        return rentRepository.read(id);
    }

    public MongoCollection<Rent> getAllRents() {
        return rentRepository.readAll();
    }

    public void rentRoom(int id, Client client, Room room, LocalDateTime startDate) {
        if (!rentExists(id)) {
            MongoCollection<Rent> rentCollection = rentRepository.readAll();
            for (Rent rent : rentCollection.find(Filters.eq("room", room)).into(new ArrayList<>())) {
                if (rent.getEndTime() == null) {
                    throw new IllegalArgumentException("Room is already rented");
                }
            }
            Rent rent = new Rent(id, client, room, startDate);
            rentRepository.create(rent);
        }
    }

    public void returnRoom(int id, LocalDateTime endDate) {
        MongoCollection<Rent> collection = rentRepository.readAll();
        Rent rent = collection.find(Filters.eq("_id", id)).first();
        if (rent != null) {
            rent.endRent(endDate);
            rentRepository.update(rent);
        }
    }
}
