package pl.nbd.managers;

import com.mongodb.client.MongoCollection;
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

    private boolean roomExists(int roomNumber) {
        MongoCollection<Room> collection = roomRepository.read();
        ArrayList<Room> rooms = collection.find().into(new ArrayList<>());
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }
        return false;
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
}
