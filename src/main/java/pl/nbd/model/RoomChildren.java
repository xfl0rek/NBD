package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity(name = "RoomChildren")
public class RoomChildren extends Room{
    private int numberOfChildren;

    public RoomChildren(int basePrice, int roomNumber, int roomCapacity, int numberOfChildren) {
        super(basePrice, roomNumber, roomCapacity);
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
