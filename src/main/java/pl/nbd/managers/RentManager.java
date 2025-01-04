package pl.nbd.managers;


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

//    private boolean rentExists(int id) {
//        return rentRepository.findById(id) != null;
//    }

//    public Rent getRent(int id) {
//        return rentRepository.findById(id);
//    }

//    public MongoCollection<Rent> getAllRents() {
//        return rentRepository.readAll();
//    }

    public void rentRoom(int id, Client client, Room room, LocalDateTime startDate) {
        //if (!rentExists(id)) {
            Rent rent = new Rent(id, client, room, startDate);
            rentRepository.create(rent);
        //}
    }

//    public void returnRoom(int id, LocalDateTime endDate) {
//        //Rent rent = rentRepository.findById(id);
//        if (rent != null) {
//            rent.endRent(endDate);
//            rentRepository.update(rent);
//        //}
//    }

    public List<Rent> findRentsByClientId(long clientId) {
        return rentRepository.findByClientId(clientId);
    }

    public List<Rent> findRentsByRoomNumber(long roomNumber) {
        return rentRepository.findByRoomNumber(roomNumber);
    }

//    public void deleteRent(int id) {
//        Rent rent = rentRepository.read(id);
//        if (rent != null) {
//            rentRepository.delete(id);
//        }
//    }

//    public void update(int id, Client client, Room room, LocalDateTime startDate) {
//        Rent rent = rentRepository.read(id);
//        if (rent != null) {
//            rent.setClient(client);
//            rent.setRoom(room);
//            rent.setBeginTime(startDate);
//            rentRepository.update(rent);
//        }
//    }
}
