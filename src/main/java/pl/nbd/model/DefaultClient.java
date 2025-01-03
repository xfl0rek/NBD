package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("clients")
public class DefaultClient extends Client {
    public DefaultClient(long personalID, String firstName, String lastName, boolean isArchive) {
        super(personalID, firstName, lastName, isArchive, "default");
    }

    public DefaultClient() {

    }

    @Override
    double applyDiscount(double price) {
        return price;
    }
}
