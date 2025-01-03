package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rooms")
public class RoomChildren extends Room {
    @CqlName("number_of_children")
    private int numberOfChildren;

    public RoomChildren(long roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
        super(roomNumber, basePrice, roomCapacity, "children");
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
