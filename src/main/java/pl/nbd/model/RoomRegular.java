package pl.nbd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "RoomRegular")
public class RoomRegular extends Room {
    private boolean isBreakfastIncluded;

    public RoomRegular() {
    }

    public RoomRegular(int basePrice, int roomNumber, int roomCapacity, boolean isBreakfastIncluded) {
        super(basePrice, roomNumber, roomCapacity);
        this.isBreakfastIncluded = isBreakfastIncluded;
    }

    @Column(name = "breakfast")
    public boolean isBreakfastIncluded() {
        return isBreakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        isBreakfastIncluded = breakfastIncluded;
    }
}
