package pl.nbd;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Rent {

    private UUID id;
    private Client client;
    private Room room;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private double rentCost;

    public Rent(UUID id, Client client, Room room, LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.beginTime = (beginTime == null) ? LocalDateTime.now() : beginTime;
        this.endTime = null;
        this.rentCost = 0;
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
