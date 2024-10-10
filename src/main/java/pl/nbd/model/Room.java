package pl.nbd.model;


import jakarta.persistence.*;

@Entity
public class Room {
    @Column(name = "base_price")
    private int basePrice;
    @Id
    private int roomNumber;
    @Column(name = "room_capacity")
    private int roomCapacity;
    @Column(name = "is_archive")
    private boolean isArchive = false;

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

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean status) {
        this.isArchive = true;
    }
}
