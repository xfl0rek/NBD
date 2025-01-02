package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rooms")
public class RoomRegular extends Room {

    @CqlName("breakfast_included")
    private boolean isBreakfastIncluded;

    public RoomRegular(long roomNumber, int basePrice, int roomCapacity, boolean isBreakfastIncluded) {
        super(roomNumber, basePrice, roomCapacity, "regular");
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
