package pl.nbd;

public class Premium extends ClientType {
    public Premium() {
    }

    double applyDiscount(double price) {
        return price * 0.85;
    }
}
