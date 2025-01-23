package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;

public class RentRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public RentRepository() {
        this.initDBConnection();
    }

    public void create(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try (clientSession) {
            clientSession.startTransaction();

            MongoCollection<Room> roomCollection = getDatabase().getCollection("rooms", Room.class);
            Bson filter = Filters.eq("_id", rent.getRoom().getRoomNumber());
            Bson update = Updates.inc("rented", 1);
            roomCollection.updateOne(clientSession, filter, update);

            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);
            rentCollection.insertOne(clientSession, rent);

            clientSession.commitTransaction();
        } catch (Exception e) {
            if (clientSession.hasActiveTransaction())
                clientSession.abortTransaction();
            throw e;
        }
    }


    public MongoCollection<Rent> readAll() {
        return getDatabase().getCollection("rents", Rent.class);
    }

    public Rent read(long id) {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find(Filters.eq("_id", id)).first();
    }

    public void update(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try (clientSession) {
            clientSession.startTransaction();

            if (rent.getEndTime() != null) {
                MongoCollection<Room> roomCollection = getDatabase().getCollection("rooms", Room.class);
                Bson roomFilter = Filters.eq("_id", rent.getRoom().getRoomNumber());
                Bson update = Updates.inc("rented", -1);
                roomCollection.updateOne(clientSession, roomFilter, update);
            }

            MongoCollection<Rent> rentCollection = getDatabase().getCollection("rents", Rent.class);
            Bson rentFilter = Filters.eq("_id", rent.getId());
            Bson updates = Updates.combine(
                    Updates.set("client", rent.getClient()),
                    Updates.set("room", rent.getRoom()),
                    Updates.set("begintime", rent.getBeginTime()),
                    Updates.set("endtime", rent.getEndTime()),
                    Updates.set("rentcost", rent.getRentCost()),
                    Updates.set("isArchive", rent.isArchive())
            );
            rentCollection.updateOne(clientSession, rentFilter, updates);


            clientSession.commitTransaction();
        } catch (Exception e) {
            if (clientSession.hasActiveTransaction())
                clientSession.abortTransaction();
            throw e;
        }

    }

    public void delete(long id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        collection.deleteOne(query);
    }
}
