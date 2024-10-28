package pl.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator("premium")
public class PremiumClient extends Client {
    public PremiumClient(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public PremiumClient() {

    }

    @Override
    double applyDiscount(double price) {
        return price * 0.85;
    }
}
