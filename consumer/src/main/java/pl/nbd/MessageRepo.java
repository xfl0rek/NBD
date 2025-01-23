package pl.nbd;

import org.bson.Document;

public class MessageRepo extends AbstractMongoRepository {
    public MessageRepo() {
        initDBConnection();
    }

    public void saveMessage(String message) {
        Document doc = new Document().append("rent", message);
        getDatabase().getCollection("messages").insertOne(doc);
    }

    public void close() {
        getMongoClient().close();
    }

}
