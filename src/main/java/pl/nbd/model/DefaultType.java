package pl.nbd.model;

import jakarta.persistence.Entity;

@Entity
public class DefaultType extends Client {
    public DefaultType(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public DefaultType() {
    }
}
