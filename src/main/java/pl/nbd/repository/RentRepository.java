package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import pl.nbd.model.Rent;

public class RentRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public RentRepository() {
        this.initDBConnection();
    }

    public void create(Rent rent) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.insertOne(rent);
    }

    public void update(Rent rent) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rooms", Rent.class);
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
