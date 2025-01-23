package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator("children")
public class RoomChildren extends Room {
    private int numberOfChildren;

    public RoomChildren(long roomNumber, int basePrice, int roomCapacity, int numberOfChildren) {
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
