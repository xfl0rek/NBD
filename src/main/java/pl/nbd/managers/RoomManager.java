package pl.nbd.managers;

import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.DecoratorRoomRepository;

import java.util.List;

public class RoomManager {
    private DecoratorRoomRepository roomRepository;

    public RoomManager(DecoratorRoomRepository roomRepository) {
        if (roomRepository == null) {
            throw new NullPointerException("RoomRepository is null");
        } else {
            this.roomRepository = roomRepository;
        }
    }

    private boolean roomExists(int roomNumber) {
        List<Room> rooms = roomRepository.readAll();
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }
        return false;
    }

    public Room getRoom(int roomNumber) {
        return roomRepository.read(roomNumber);
    }

    public List<Room> getAllRooms() {
        return roomRepository.readAll();
    }

    public void registerRoom(int roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
        if (!roomExists(roomNumber)) {
            Room room = new RoomChildren(roomNumber, basePrice, roomCapacity, numberOfChildren);
            roomRepository.create(room);
        }
    }

    public void registerRoom(int roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        if (!roomExists(roomNumber)) {
            Room room = new RoomRegular(roomNumber, basePrice, roomCapacity, isBreakfastIncluded);
            roomRepository.create(room);
        }
    }

    public void deleteRoom(int roomNumber) {
        if (roomExists(roomNumber)) {
            roomRepository.delete(roomNumber);
        }
    }

    public void updateRoomInformation(int roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
        if (roomExists(roomNumber)) {
            Room room = new RoomChildren(roomNumber, basePrice, roomCapacity, numberOfChildren);
            roomRepository.update(room);
        }
    }

    public void updateRoomInformation(int roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        if (roomExists(roomNumber)) {
            Room room = new RoomRegular(roomNumber, basePrice, roomCapacity, isBreakfastIncluded);
            roomRepository.update(room);
        }
    }
}
