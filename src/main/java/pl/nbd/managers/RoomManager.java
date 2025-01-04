package pl.nbd.managers;

import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.RoomRepository;

import java.util.ArrayList;

public class RoomManager {
    private RoomRepository roomRepository;

    public RoomManager(RoomRepository roomRepository) {
        if (roomRepository == null) {
            throw new NullPointerException("RoomRepository is null");
        } else {
            this.roomRepository = roomRepository;
        }
    }

    private boolean roomExists(long roomNumber) {
        return roomRepository.read(roomNumber) != null;
    }

    public Room getRoom(int roomNumber) {
        return roomRepository.read(roomNumber);
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

    public void deleteRoom(long roomNumber) {
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
