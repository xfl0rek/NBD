package pl.nbd.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
@Access(AccessType.FIELD)
public abstract class Client {
    @Id
    private long personalID;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "is_archive")
    private boolean isArchive = false;

    @Embedded
    private Address address;

    public Client() {
    }

    public Client(long personalID, String firstName, String lastName, Address address) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPersonalID() {
        return personalID;
    }


    public Address getAddress() {
        return address;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean status) {
        this.isArchive = true;
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
