package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.LocalDateTime;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("rents_by_room")
public class RentsByRoom extends Rent {
    @CqlName("rent_id")
    private long id;
    private long clientId;
    @CqlName("room_id")
    @PartitionKey
    private long roomNumber;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;
    private boolean isArchive;

    public RentsByRoom() {
    }

    public RentsByRoom(long id, Client client, Room room, LocalDateTime beginTime) {
        super(id, client, room, beginTime);
    }

    public RentsByRoom(long id, Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean isArchive) {
        super(id, client, room, beginTime, endTime, rentCost, isArchive);
    }

    public RentsByRoom(long id, long clientId, long roomNumber, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean isArchive) {
        super(id, clientId, roomNumber, beginTime, endTime, rentCost, isArchive);
    }

    public long getId() {
        return id;
    }

    public long getClientId() {
        return clientId;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getRentCost() {
        return rentCost;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setRoomNumber(long roomId) {
        this.roomNumber = roomId;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }
}
