package pl.nbd.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Rent {
    private long id;
    private long clientId;
    private Client client;
    private long roomNumber;
    private Room room;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;
    private boolean isArchive;

    public Rent(long id, Client client, Room room, LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.beginTime = (beginTime == null) ? LocalDateTime.now() : beginTime;
        this.endTime = null;
        this.rentCost = 0;
        this.isArchive = false;
        this.clientId = client.getPersonalID();
        this.roomNumber = room.getRoomNumber();
    }

    public Rent(long id, Client client, Room room, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean isArchive) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rentCost = rentCost;
        this.isArchive = isArchive;
        this.roomNumber = room.getRoomNumber();
        this.clientId = client.getPersonalID();
    }

    public Rent(long id, long clientId, long roomNumber, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean isArchive) {
        this.id = id;
        this.clientId = clientId;
        this.roomNumber = roomNumber;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rentCost = rentCost;
        this.isArchive = isArchive;
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

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public long getClientId() {
        return clientId;
    }

    public long getRoomNumber() {
        return roomNumber;
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

        if (id != rent.id) return false;

        if (!Objects.equals(client, rent.client)) return false;

        if (!Objects.equals(room, rent.room)) return false;

        if (!Objects.equals(beginTime.truncatedTo(ChronoUnit.MINUTES), rent.beginTime.truncatedTo(ChronoUnit.MINUTES))) return false;

        if (endTime != null && rent.endTime != null) {
            if (!Objects.equals(endTime.truncatedTo(ChronoUnit.MINUTES), rent.endTime.truncatedTo(ChronoUnit.MINUTES))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, room, beginTime, endTime, rentCost, isArchive);
    }
}
