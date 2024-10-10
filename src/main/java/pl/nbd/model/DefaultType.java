package pl.nbd.model;

public class DefaultType extends ClientType {
    public DefaultType() {
    }

    double applyDiscount(double price) {
        return price;
    }


}
