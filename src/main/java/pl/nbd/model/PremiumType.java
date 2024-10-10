package pl.nbd.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("premium_type")
public class PremiumType extends ClientType {
    public PremiumType() {
    }

    double applyDiscount(double price) {
        return price * 0.85;
    }
}
