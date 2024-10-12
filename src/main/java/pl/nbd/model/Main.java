package pl.nbd.model;

import jakarta.persistence.criteria.CriteriaQuery;
import pl.nbd.managers.ClientManager;
import pl.nbd.managers.RentManager;
import pl.nbd.managers.RoomManager;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository  = new ClientRepository();
        RoomRepository roomRepository = new RoomRepository();
        RentRepository rentRepository = new RentRepository();
        ClientManager clientManager = new ClientManager(clientRepository);
        RoomManager roomManager = new RoomManager(roomRepository);
        RentManager rentManager = new RentManager(rentRepository);

        //Rejestracja klientow
        Address address = new Address("Real", "Madryt", "9");
        Random random = new Random();
        Client client = clientManager.registerClient(123456789, "Cristiano", "Ronaldo", address, "premium");
        Client client1 = clientManager.registerClient(987654321, "Leo", "Messi", address, "default");

        System.out.println("Zarejestrowano klientów:");
        System.out.println(client.getFirstName() + " " + client.getLastName());
        System.out.println(client1.getFirstName() + " " + client1.getLastName());

        System.out.println(clientManager.getClient(123456789));

        clientManager.deleteClient(987654321);
        if (clientManager.getClient(987654321) == null) {
            System.out.println("Usunieto klienta.");
        }

        clientManager.updateClientInformation(123456789, "Vinicius", "Junior", address, "default");
        System.out.println("Zaktualizowano info o kliencie: " + client.getFirstName() + " " + client.getLastName());

        clientManager.unregisterClient(client);
        System.out.println("Zarchiwizowano klienta: " + client.isArchive());

        //Rejestracja pokoi
        roomManager.registerRoom(1000, 9, 2, true);
        roomManager.registerRoom(1000, 10, 2, 3);





        Room room9 = roomManager.getRoom(9);
        Room room10 = roomManager.getRoom(10);
        System.out.println("Odczytano pokój: " + room9.getRoomNumber() +", pojemność: " + room9.getRoomCapacity());

        roomManager.updateRoomInformation(9, 1500, 3, true);
        Room updatedRoom = roomManager.getRoom(9);
        System.out.println("Zaktualizowano pokój o numerze " + updatedRoom.getRoomNumber() + " z pojemnością " + updatedRoom.getRoomCapacity());

        roomManager.deleteRoom(10);
        Room deletedRoom = roomManager.getRoom(10);
        if (deletedRoom == null) {
            System.out.println("Pokój o numerze 10" + " został usunięty.");
        }





        //Wynajem
        room9 = roomRepository.lockRoom(9);
        rentManager.rentRoom(client, room9, LocalDateTime.now());
        System.out.println("123");
        Rent rent = rentManager.getRent(1);
        rentManager.rentRoom(client1, room9, LocalDateTime.now());
        Rent rent1 = rentManager.getRent(2);
        if (rent1 == null) {
            System.out.println("Nie można wynająć pokoju.");
        }
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rentManager.returnRoom(1, endTime);


        Rent readRent = rentManager.getRent(1);
        System.out.println("Odczytano wynajem o ID: " + readRent.getId() + ", liczba dni: " + readRent.getRentDays() + ", koszt wynajmu: " + readRent.getRentCost());
//
//        rent.setRentCost(1500);
//        rentRepository.update(rent);
//        Rent updatedRent = rentRepository.read(rent.getId());
//        System.out.println("Zaktualizowano wynajem o ID: " + updatedRent.getId() + ", nowy koszt: " + updatedRent.getRentCost());
//
//        rentRepository.delete(rent1);
//        Rent deletedRent = rentRepository.read(rent1.getId());
//        if (deletedRent == null) {
//            System.out.println("Wynajem o ID: " + rent1.getId() + " został usunięty.");
//        }
    }
}
