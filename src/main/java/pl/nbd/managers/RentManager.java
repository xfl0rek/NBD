package pl.nbd.managers;

import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import pl.nbd.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentManager {
    private final RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public void rentRoom(Client client, Room room, LocalDateTime startDate) {
        List<Rent> rents = rentRepository.getAll();
        for (Rent rent : rents) {
            if (rent.getRoom().equals(room) && rent.getEndTime() == null) {
//                throw new IllegalArgumentException("Room is already rented");
                System.out.println("Room is already rented");
                return;
            }
        }

        rentRepository.createReservation(client, room, startDate);
    }

    public void returnRoom(long reservationID, LocalDateTime endDate) {
        Rent rent = rentRepository.read(reservationID);
        rent.endRent(endDate);
        rentRepository.update(rent);
    }

    public Rent getRent(long reservationID) {
        return rentRepository.read(reservationID);
    }

}
