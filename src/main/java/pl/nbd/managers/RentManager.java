package pl.nbd.managers;

import pl.nbd.model.Client;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;
import pl.nbd.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class RentManager {
    private final RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public void rentRoom(long id, Client client, Room room, LocalDateTime startDate) throws Exception {
        List<Rent> rents = rentRepository.getAll();
        for (Rent rent : rents) {
            if (rent.getRoom().getRoomNumber() == room.getRoomNumber() && rent.getEndTime() == null) {
                throw new Exception("Nie ma.");
            }
        }

        rentRepository.createReservation(id, client, room, startDate);
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
