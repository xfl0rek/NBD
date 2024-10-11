package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity
public class DefaultClient extends Client {
    public DefaultClient(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public DefaultClient() {
    }

    double applyDiscount(double price) {
        return price;
    }
}
