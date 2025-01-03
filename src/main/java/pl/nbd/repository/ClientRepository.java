package pl.nbd.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import pl.nbd.dao.ClientDao;
import pl.nbd.mappers.ClientMapper;
import pl.nbd.mappers.ClientMapperBuilder;
import pl.nbd.model.Client;

public class ClientRepository extends AbstractCassandraRepository {

    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;

    public ClientRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.clientMapper = new ClientMapperBuilder(session).build();
        this.clientDao = clientMapper.clientDao();
    }

    public void makeTable() {
        SimpleStatement createClients =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("personal_id"), DataTypes.BIGINT)
                        .withColumn("first_name", DataTypes.TEXT)
                        .withColumn("last_name", DataTypes.TEXT)
                        .withColumn("archive", DataTypes.BOOLEAN)
                        .withColumn("discriminator", DataTypes.TEXT)
                        .build();
        session.execute(createClients);

    }

    public void create(Client client) {
        clientDao.create(client);
    }

    public Client read(long id) {
        return clientDao.findById(id);
    }

    public void update(Client client) {
        clientDao.update(client);
    }

    public void delete(long id) {
        clientDao.remove(id);
    }
}
