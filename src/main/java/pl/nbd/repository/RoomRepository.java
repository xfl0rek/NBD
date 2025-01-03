package pl.nbd.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import pl.nbd.dao.RoomDao;
import pl.nbd.mappers.RoomMapper;
import pl.nbd.mappers.RoomMapperBuilder;
import pl.nbd.model.Room;

import java.util.List;

public class RoomRepository  {

    private final CqlSession session;
    private final RoomMapper roomMapper;
    private final RoomDao roomDao;

    public RoomRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.roomMapper = new RoomMapperBuilder(session).build();
        this.roomDao = roomMapper.roomDao();

    }

    public void makeTable() {
        SimpleStatement createRooms =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rooms"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("room_number"), DataTypes.BIGINT)
                        .withColumn("base_price", DataTypes.INT)
                        .withColumn("room_capacity", DataTypes.INT)
                        .withColumn("rented", DataTypes.INT)
                        .withColumn("discriminator", DataTypes.TEXT)
                        .withColumn("number_of_children", DataTypes.INT)
                        .withColumn("breakfast_included", DataTypes.BOOLEAN)
                        .build();
        session.execute(createRooms);
    }

    public void create(Room room) {
        roomDao.create(room);
    }

    public Room read(long id) {
        return roomDao.findById(id);
    }

    public void update(Room room) {
        roomDao.update(room);
    }

    public void delete(long roomNumber) {
        roomDao.remove(roomNumber);
    }
}
