package pl.nbd.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "room_type")
@Access(AccessType.FIELD)
public abstract class Room {
    @Column(name = "base_price")
    private int basePrice;
    @Id
    private long roomNumber;
    @Column(name = "room_capacity")
    private int roomCapacity;

    public Room(int basePrice, int roomNumber, int roomCapacity) {
        this.basePrice = basePrice;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
    }

    public Room() {
    }

    public void setBasePrice(int basePrice) {
        if (basePrice > 0) {
            this.basePrice = basePrice;
        }
    }

    public int getBasePrice() {
        return basePrice;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return basePrice == room.basePrice && roomNumber == room.roomNumber && roomCapacity == room.roomCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(basePrice, roomNumber, roomCapacity);
    }
}
