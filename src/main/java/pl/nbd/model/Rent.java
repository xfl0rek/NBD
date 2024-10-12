package pl.nbd.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class Rent {
    @Id
    private long id;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Room room;
    @Column(name = "begin_time")
    private LocalDateTime beginTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "rent_cost")
    private double rentCost;

    @Column(name = "is_archive")
    private boolean isArchive = false;

    public Rent(long id, Client client, Room room, LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.beginTime = (beginTime == null) ? LocalDateTime.now() : beginTime;
        this.endTime = null;
        this.rentCost = 0;
    }

    public Rent() {

    }

    public Client getClient() {
        return client;
    }

    public long getId() {
        return id;
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

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean status) {
        this.isArchive = status;
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Rent rent = (Rent) o;
//        return id == rent.id && Double.compare(rentCost, rent.rentCost) == 0 && isArchive == rent.isArchive && Objects.equals(client, rent.client) && Objects.equals(room, rent.room) && Objects.equals(beginTime, rent.beginTime) && Objects.equals(endTime, rent.endTime);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, client, room, beginTime, endTime, rentCost, isArchive);
//    }
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
        return Objects.hash(id, client, room, beginTime, endTime);
    }

}
