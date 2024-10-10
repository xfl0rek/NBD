package pl.nbd.model;

public class Address {
    private String street;
    private String city;
    private String number;

    public Address(String street, String city, String number) {
        this.street = street;
        this.city = city;
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
