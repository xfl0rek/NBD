package pl.nbd.repository;

import com.mongodb.client.MongoCollection;
import pl.nbd.model.Client;

public class ClientRepository extends AbstractMongoRepository {
    @Override
    public void close() throws Exception {

    }

    public ClientRepository() {

    }

    public void create(Client client) {

    }
}
