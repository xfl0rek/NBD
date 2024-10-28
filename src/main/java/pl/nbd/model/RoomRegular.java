package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator("regular")
public class RoomRegular extends Room {
    private boolean isBreakfastIncluded;

    public RoomRegular(long roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        super(roomNumber, basePrice, roomCapacity);
        this.isBreakfastIncluded = isBreakfastIncluded;
    }

    public RoomRegular() {

    }

    public boolean isBreakfastIncluded() {
        return isBreakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        isBreakfastIncluded = breakfastIncluded;
    }
}
