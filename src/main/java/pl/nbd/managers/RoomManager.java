package pl.nbd.managers;

import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.repository.RoomRepository;

public class RoomManager {
    private final RoomRepository roomRepository;

    public RoomManager(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public void registerRoom(int roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
        if (roomRepository.read(roomNumber) == null) {
            Room room = new RoomChildren(roomNumber, basePrice, roomCapacity, numberOfChildren);
            roomRepository.create(room);
        }
    }

    public void registerRoom(int roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        if (roomRepository.read(roomNumber) == null) {
            Room room = new RoomRegular(roomNumber, basePrice, roomCapacity, isBreakfastIncluded);
            roomRepository.create(room);
        }
    }

    public Room getRoom(long roomNumber) {
        return roomRepository.read(roomNumber);
    }

    public void deleteRoom(long roomNumber) {
        if (roomRepository.read(roomNumber) != null) {
            roomRepository.delete(roomRepository.read(roomNumber));
        }
    }

    public void updateRoomInformation(long roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        if (roomRepository.read(roomNumber) != null) {
            RoomRegular existingRoom = (RoomRegular) roomRepository.read(roomNumber);
            existingRoom.setBasePrice(basePrice);
            existingRoom.setRoomCapacity(roomCapacity);
            existingRoom.setBreakfastIncluded(isBreakfastIncluded);
            roomRepository.update(existingRoom);
        }
    }
    public void updateRoomInformation(long roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
        if (roomRepository.read(roomNumber) != null) {
            RoomChildren existingRoom = (RoomChildren) roomRepository.read(roomNumber);
            existingRoom.setBasePrice(basePrice);
            existingRoom.setRoomCapacity(roomCapacity);
            existingRoom.setNumberOfChildren(numberOfChildren);
            roomRepository.update(existingRoom);

        }
    }



//    public List<Room> getAvailableRooms() {
//        return roomRepository.getAll();
//    }
}
