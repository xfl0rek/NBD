package pl.nbd.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

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
}
