package pl.nbd.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import pl.nbd.model.Client;

public class ClientRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public ClientRepository() {
        this.initDBConnection();
    }

    public void create(Client client) {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.insertOne(client);

    }

    public MongoCollection<Client> readAll() {
        return getDatabase().getCollection("clients", Client.class);
    }

    public Client read(long id) {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        return collection.find(Filters.eq("_id", id)).first();
    }

    public void update(Client client) {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        BasicDBObject update = new BasicDBObject();
        update.put("_id", client.getPersonalID());
        collection.replaceOne(update, client);
    }

    public void delete(long id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.deleteOne(query);
    }
}
