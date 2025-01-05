package pl.nbd.managers;


import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentManager {
    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private RoomRepository roomRepository;

    public RentManager(RentRepository rentRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        if (rentRepository == null) {
            throw new NullPointerException("RentRepository is null");
        } else {
            this.rentRepository = rentRepository;
        }
        if (clientRepository == null) {
            throw new NullPointerException("ClientRepository is null");
        } else {
            this.clientRepository = clientRepository;
        }
        if (roomRepository == null) {
            throw new NullPointerException("RoomRepository is null");
        } else {
            this.roomRepository = roomRepository;
        }
    }

    private boolean rentExists(int id) {
        return !rentRepository.findByClientId(id).isEmpty();
    }

//    public Rent getRent(int id) {
//        return rentRepository.findById(id);
//    }

//    public MongoCollection<Rent> getAllRents() {
//        return rentRepository.readAll();
//    }

    public void rentRoom(int id, Client client, Room room, LocalDateTime startDate) {
        if (!rentExists(id)) {
            if (room.getRented() == 1) {
                throw new IllegalArgumentException("Room is already rented");
            }
            room.setRented(1);
            roomRepository.update(room);
            Rent rent = new Rent(id, client, room, startDate);
            rentRepository.create(rent);
        }
    }

    public void returnRoom(int id, LocalDateTime endDate) {
        Rent rent = findRentsByClientId(id).get(0);
        if (rent != null) {
            rent.getRoom().setRented(0);
            roomRepository.update(rent.getRoom());
            rent.endRent(endDate);
            rentRepository.update(rent);
        }
    }

    public List<Rent> findRentsByClientId(long clientId) {
        List<Rent> rents = rentRepository.findByClientId(clientId);
        for (Rent rent : rents) {
            rent.setClient(clientRepository.read(rent.getClientId()));
            rent.setRoom(roomRepository.read(rent.getRoomNumber()));
        }

        return rents;
    }

    public List<Rent> findRentsByRoomNumber(long roomNumber) {
        return rentRepository.findByRoomNumber(roomNumber);
    }

    public void deleteRent(int id) {
        Rent rent = findRentsByClientId(id).get(0);
        if (rent != null) {
            rent.getRoom().setRented(0);
            roomRepository.update(rent.getRoom());
            rentRepository.delete(rent);
        }
    }

    public void update(int id, Client client, Room room, LocalDateTime startDate, LocalDateTime endDate) {
        Rent rent = rentRepository.findByClientId(id).get(0);
        if (rent != null) {
            rent.setClient(client);
            rent.setRoom(room);
            rent.setBeginTime(startDate);
            rent.setEndTime(endDate);
            rentRepository.update(rent);
        }
    }
}
