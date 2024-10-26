package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Rent {
    @BsonId
    private long id;
    @BsonProperty("client")
    private Client client;
    @BsonProperty("room")
    private Room room;
    @BsonProperty("begintime")
    private LocalDateTime beginTime;
    @BsonProperty("endtime")
    private LocalDateTime endTime;
    @BsonProperty("rentcost")
    private double rentCost;
    @BsonProperty("isArchive")
    private boolean isArchive;

    public Rent(@BsonId long id,
                @BsonProperty("client") Client client,
                @BsonProperty("room") Room room,
                @BsonProperty("begintime") LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.beginTime = (beginTime == null) ? LocalDateTime.now() : beginTime;
        this.endTime = null;
        this.rentCost = 0;
    }

    public Rent() {

    }

    public long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Room getRoom() {
        return room;
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public void endRent(LocalDateTime endTime) {
        if (this.endTime == null) {
            if (endTime == null) {
                this.endTime = LocalDateTime.now();
            } else {
                if (endTime.isAfter(beginTime)) {
                    this.endTime = endTime;
                } else {
                    this.endTime = beginTime;
                }
            }
            this.setArchive(true);
            this.rentCost = calculateRentCost();
        }
    }

    public long getRentDays() {
        if (endTime == null) {
            return 0;
        }

        Duration period = Duration.between(beginTime, endTime);
        long days = period.toHours() / 24;

        if (period.toHours() % 24 >= 1) {
            days += 1;
        }

        return days;
    }

    private double calculateRentCost() {
        return Math.round(100 * client.applyDiscount(getRentDays() * room.getBasePrice())) / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return id == rent.id && Double.compare(rentCost, rent.rentCost) == 0 && isArchive == rent.isArchive && Objects.equals(client, rent.client) && Objects.equals(room, rent.room) && Objects.equals(beginTime, rent.beginTime) && Objects.equals(endTime, rent.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, room, beginTime, endTime, rentCost, isArchive);
    }
}
