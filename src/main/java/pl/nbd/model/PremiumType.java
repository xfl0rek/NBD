package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity(name = "premium_type")
public class PremiumType extends Client {
    public PremiumType(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public PremiumType() {
    }

    double applyDiscount(double price) {
        return price * 0.85;
    }
}
