package pl.nbd.model;

import java.util.Objects;

public abstract class Client {
    private long personalID;
    private String firstName;
    private String lastName;
    private boolean isArchive = false;
    private Address address;

    public Client() {

    }

    public Client(long personalID, String firstName, String lastName, Address address) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public long getPersonalID() {
        return personalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public Address getAddress() {
        return address;
    }

    public void setPersonalID(long personalID) {
        this.personalID = personalID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    abstract double applyDiscount(double price);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return personalID == client.personalID && isArchive == client.isArchive && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(address, client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalID, firstName, lastName, isArchive, address);
    }
}
