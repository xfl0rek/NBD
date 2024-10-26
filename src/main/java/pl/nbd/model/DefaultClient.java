package pl.nbd.model;

public class DefaultClient extends Client {
    public DefaultClient(long personalID, String firstName, String lastName, Address address) {
        super(personalID, firstName, lastName, address);
    }

    public DefaultClient() {

    }

    @Override
    double applyDiscount(double price) {
        return price;
    }
}
