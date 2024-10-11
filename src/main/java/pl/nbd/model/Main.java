package pl.nbd.model;

import pl.nbd.managers.ClientManager;
import pl.nbd.repository.ClientRepository;
import pl.nbd.repository.RentRepository;
import pl.nbd.repository.RoomRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository  = new ClientRepository();
        RoomRepository roomRepository = new RoomRepository();
        RentRepository rentRepository = new RentRepository();
        ClientManager clientManager = new ClientManager(clientRepository);

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
            System.out.println("Ununięto klienta.");
        }

        clientManager.updateClientInformation(123456789, "Vinicius", "Junior", address, "default");
        System.out.println("Zaktualizowano info o kliencie: " + client.getFirstName() + " " + client.getLastName());

        clientManager.unregisterClient(client);
        System.out.println("Zarchiwizowano klienta: " + client.isArchive());

        Room room = new RoomRegular(1000, 9, 2, true);
        Room room2 = new RoomChildren(1000, 10, 2, 3);

        roomRepository.create(room);
        roomRepository.create(room2);

        Room readRoom = roomRepository.read(room.getRoomNumber());
        System.out.println("Odczytano pokój: " + readRoom.getRoomNumber());

        room.setRoomCapacity(3);
        roomRepository.update(room);
        Room updatedRoom = roomRepository.read(room.getRoomNumber());
        System.out.println("Zaktualizowano pokój o numerze " + updatedRoom.getRoomNumber() + " z pojemnością " + updatedRoom.getRoomCapacity());

        roomRepository.delete(room2);
        Room deletedRoom = roomRepository.read(room2.getRoomNumber());
        if (deletedRoom == null) {
            System.out.println("Pokój o numerze " + room2.getRoomNumber() + " został usunięty.");
        }

        Rent rent = new Rent(random.nextLong(), client, room, LocalDateTime.now());
        Rent rent1 = new Rent(random.nextLong(), client, room, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().plus(Duration.ofHours(168));
        rent.endRent(endTime);
        rent1.endRent(endTime);

        rentRepository.create(rent);
        rentRepository.create(rent1);

        Rent readRent = rentRepository.read(rent.getId());
        System.out.println("Odczytano wynajem o ID: " + readRent.getId() + ", liczba dni: " + readRent.getRentDays() + ", koszt wynajmu: " + readRent.getRentCost());

        rent.setRentCost(1500);
        rentRepository.update(rent);
        Rent updatedRent = rentRepository.read(rent.getId());
        System.out.println("Zaktualizowano wynajem o ID: " + updatedRent.getId() + ", nowy koszt: " + updatedRent.getRentCost());

        rentRepository.delete(rent1);
        Rent deletedRent = rentRepository.read(rent1.getId());
        if (deletedRent == null) {
            System.out.println("Wynajem o ID: " + rent1.getId() + " został usunięty.");
        }
    }
}
