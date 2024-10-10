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
    private Long id;
    @Column(name = "first_name")
    private String firstName;
//    private String lastName;
//    private String personalID;
//    private Address address;
//    private ClientType clientType;

    public Client() {
    }

    public Client(String firstName, String lastName, String personalID, Address address, ClientType clientType) {
        this.firstName = firstName;
//        this.lastName = lastName;
//        this.personalID = personalID;
//        this.address = address;
//        this.clientType = clientType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getPersonalID() {
//        return personalID;
//    }
//
//    public void setPersonalID(String personalID) {
//        this.personalID = personalID;
//    }

//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }
//
//    public ClientType getClientType() {
//        return clientType;
//    }
//
//    public void setClientType(ClientType clientType) {
//        this.clientType = clientType;
//    }
//
//    public double applyDiscount(double price) {
//        return clientType.applyDiscount(price);
//    }
}
