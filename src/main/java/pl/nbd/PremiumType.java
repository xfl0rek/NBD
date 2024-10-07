package pl.nbd;

public class PremiumType extends ClientType {
    public PremiumType() {
    }

    double applyDiscount(double price) {
        return price * 0.85;
    }
}
