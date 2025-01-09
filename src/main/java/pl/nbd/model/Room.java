package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.Objects;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rooms")
public class Room {
    @PartitionKey
    @CqlName("room_number")
    private long roomNumber;

    private String discriminator;
    private int basePrice;
    private int roomCapacity;
    private int rented = 0;

    public Room(long roomNumber, int basePrice, int roomCapacity, String discriminator) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.roomCapacity = roomCapacity;
        this.discriminator = discriminator;
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

    public String getDiscriminator() {
        return discriminator;
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

    public int getRented() {
        return rented;
    }

    public void setRented(int rented) {
        this.rented = rented;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
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
