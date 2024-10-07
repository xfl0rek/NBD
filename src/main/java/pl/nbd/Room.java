package pl.nbd;

import lombok.Getter;

@Getter
public class Room {
    private int basePrice;
    private final int roomNumber;
    private final int roomCapacity;
    private boolean isArchive = false;

    public Room(int basePrice, int roomNumber, int roomCapacity) {
        this.basePrice = basePrice;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
    }

    public void setBasePrice(int basePrice) {
        if (basePrice > 0) {
            this.basePrice = basePrice;
        }
    }

    public void setArchive(boolean status) {
        this.isArchive = true;
    }
}
