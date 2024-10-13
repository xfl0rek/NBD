package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity(name = "RoomChildren")
public class RoomChildren extends Room {
    private int numberOfChildren;

    public RoomChildren(int roomNumber, int basePrice,  int roomCapacity, int numberOfChildren) {
        super(roomNumber, basePrice, roomCapacity);
        this.numberOfChildren = numberOfChildren;
    }

    public RoomChildren() {
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
