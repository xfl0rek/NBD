package pl.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.Objects;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("clients")
public class Client {

    @PartitionKey
    @CqlName("personal_id")
    private long personalID;

    private String discriminator;
    private String firstName;
    private String lastName;
    private boolean isArchive;

    public Client() {

    }

    public Client(long personalID, String firstName, String lastName, boolean isArchive, String discriminator) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isArchive = isArchive;
        this.discriminator = discriminator;
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

    public String getDiscriminator() {
        return discriminator;
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

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    double applyDiscount(double price) {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return personalID == client.personalID && isArchive == client.isArchive && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalID, firstName, lastName, isArchive);
    }
}
