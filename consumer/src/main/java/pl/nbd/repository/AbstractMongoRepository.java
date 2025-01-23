package pl.nbd.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import org.bson.BsonType;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"
    );

    private MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray()
    );

    private CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .register(Room.class, RoomRegular.class, RoomChildren.class)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build()
    );

    private MongoClient mongoClient;
    private MongoDatabase hotel;

    protected void initDBConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.
                        getDefaultCodecRegistry(),
                        pojoCodecRegistry))
                .build();

        mongoClient = MongoClients.create(settings);
        hotel = mongoClient.getDatabase("hotel");
        if (!getDatabase().listCollectionNames().into(new ArrayList<>()).contains("rooms")) {
            createRoomsCollection();
        }
    }

    private void createRoomsCollection() {
        Bson isRentedType = Filters.type("rented", BsonType.INT32);
        Bson isRentedMin = Filters.gte("rented", 0);
        Bson isRentedMax = Filters.lte("rented", 1);
        Bson isRented = Filters.and(isRentedType, isRentedMin, isRentedMax);

        ValidationOptions validationOptions = new ValidationOptions()
                .validator(isRented);

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        getDatabase().createCollection("rooms", createCollectionOptions);
    }

    public MongoDatabase getDatabase() {
        return hotel;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
