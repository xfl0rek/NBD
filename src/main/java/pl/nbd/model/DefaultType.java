package pl.nbd.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("default_type")
public class DefaultType extends ClientType {
    public DefaultType() {
    }

    double applyDiscount(double price) {
        return price;
    }


}
