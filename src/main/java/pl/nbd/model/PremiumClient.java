package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity
public class PremiumClient extends Client {
    public PremiumClient(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public PremiumClient() {
    }

    double applyDiscount(double price) {
        return price * 0.85;
    }
}
