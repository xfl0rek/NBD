package pl.nbd.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
@Access(AccessType.FIELD)
public class Address {
    private String street;
    private String city;
    private String number;

    public Address() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(number, address.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, number);
    }
}
