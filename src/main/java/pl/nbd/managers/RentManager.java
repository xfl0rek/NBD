package pl.nbd.managers;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.BsonNull;
import org.bson.conversions.Bson;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import pl.nbd.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            Bson filter = Filters.and(
                    Filters.eq("endtime", BsonNull.VALUE),
                    Filters.eq("room", room.getRoomNumber())
                    );
            //FindIterable<Rent> documents = rentCollection.find(filter);
            Bson projection = Projections.excludeId();
            AggregateIterable<Rent> aggregate = rentCollection.aggregate(List.of(
                    Aggregates.match(filter),
                    Aggregates.group("$room", Accumulators.sum("rented", 1))
            ));

//            for (Rent rent : rentCollection.find(Filters.eq("room", room)).into(new ArrayList<>())) {
//                if (rent.getEndTime() == null) {
//                    throw new IllegalArgumentException("Room is already rented");
//                }
//            }

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

    public void deleteRent(int id) {
        Rent rent = rentRepository.read(id);
        if (rent != null) {
            rentRepository.delete(id);
        }
    }

    public void update(int id, Client client, Room room, LocalDateTime startDate) {
        Rent rent = rentRepository.read(id);
        if (rent != null) {
            rent.setClient(client);
            rent.setRoom(room);
            rent.setBeginTime(startDate);
        }
    }
}
