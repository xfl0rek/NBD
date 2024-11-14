package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;

public class RentRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public RentRepository() {
        this.initDBConnection();
    }

    public void create(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.insertOne(rent);
    }

    public MongoCollection<Rent> readAll() {
        return getDatabase().getCollection("rents", Rent.class);
    }

    public Rent read(long id) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find(Filters.eq("_id", id)).first();
    }

    public void update(Rent rent) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        BasicDBObject update = new BasicDBObject();
        update.put("_id", rent.getId());
        collection.replaceOne(update, rent);
    }

    public void delete(long id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.deleteOne(query);
    }
}
