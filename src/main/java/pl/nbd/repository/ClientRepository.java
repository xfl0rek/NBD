package pl.nbd.repository;

import com.mongodb.client.MongoCollection;
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
//        MongoCollection<Client> collection = getDatabase().getCollection(getCollectionName(), Client.class);
        collection.insertOne(client);

    }

    public MongoCollection<Client> getCollection() {
        return getDatabase().getCollection("clients", Client.class);
    }


}
