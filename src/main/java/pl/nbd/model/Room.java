package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

@BsonDiscriminator("room")
public abstract class Room {
    @BsonId
    private long roomNumber;
    @BsonProperty("baseprice")
    private int basePrice;
    @BsonProperty("roomcapacity")
    private int roomCapacity;

    public Room(@BsonId long roomNumber,
                @BsonProperty("baseprice") int basePrice,
                @BsonProperty("roomcapacity") int roomCapacity) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.roomCapacity = roomCapacity;
    }

    public Room() {

    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber && basePrice == room.basePrice && roomCapacity == room.roomCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, basePrice, roomCapacity);
    }
}
