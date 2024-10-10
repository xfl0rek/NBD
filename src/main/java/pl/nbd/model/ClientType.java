package pl.nbd.model;

public abstract class ClientType {
    public ClientType() {
    }

    abstract double applyDiscount(double price);
}
