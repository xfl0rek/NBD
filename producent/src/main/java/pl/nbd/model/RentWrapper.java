package pl.nbd.model;

public class RentWrapper {
    private Rent rent;
    private String rentalName;

    public RentWrapper(Rent rent, String rentalName) {
        this.rent = rent;
        this.rentalName = rentalName;
    }

    public Rent getRent() {
        return rent;
    }

    public String getRentalName() {
        return rentalName;
    }
}
