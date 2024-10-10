package pl.nbd.model;

//import jakarta.persistence.Access;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Inheritance;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
@Access(AccessType.FIELD)

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long personalID;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Embedded
    private Address address;
    @Embedded
    private ClientType clientType;

    public Client() {
    }

    public Client(String firstName, String lastName, Address address, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clientType = clientType;
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

    public void setAddress(Address address) {
        this.address = address;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public double applyDiscount(double price) {
        return clientType.applyDiscount(price);
    }
}
