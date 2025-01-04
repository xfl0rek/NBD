package pl.nbd.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import pl.nbd.model.Rent;
import pl.nbd.model.Room;

public class RentRepository extends AbstractCassandraRepository {

    private final CqlSession session;
    private final RentMapper rentMapper;
    private final RentDao rentDao;

    public RentRepository() {
        this.session = getSession();
        makeTable();
        this.rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.rentDao();
    }

    private void makeTable() {
        SimpleStatement createRentsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("room_number", DataTypes.BIGINT)
                        .withColumn("start_date", DataTypes.TIMESTAMP)
                        .withColumn("end_date", DataTypes.TIMESTAMP)
                        .withColumn("price", DataTypes.DECIMAL)
                        .withColumn("archive", DataTypes.BOOLEAN)
                        .build();
        session.execute(createRentsByClient);

        SimpleStatement createRentsByRoom =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_room"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("room_number"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("client_id", DataTypes.BIGINT)
                        .withColumn("start_date", DataTypes.TIMESTAMP)
                        .withColumn("end_date", DataTypes.TIMESTAMP)
                        .withColumn("price", DataTypes.DECIMAL)
                        .withColumn("archive", DataTypes.BOOLEAN)
                        .build();
        session.execute(createRentsByRoom);
    }

    public void create(Rent rent) {

    }


//    public MongoCollection<Rent> readAll() {
//
//    }

    public Rent read(long id) {

    }

    public void update(Rent rent) {

    }

    public void delete(long id) {
    }
}
