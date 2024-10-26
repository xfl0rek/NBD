package pl.nbd.model;

import java.util.Objects;

public abstract class Room {
    private long roomNumber;
    private int basePrice;
    private int roomCapacity;

    public Room(long roomNumber, int basePrice, int roomCapacity) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.roomCapacity = roomCapacity;
    }

    public Room() {

    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber && basePrice == room.basePrice && roomCapacity == room.roomCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, basePrice, roomCapacity);
    }
}
