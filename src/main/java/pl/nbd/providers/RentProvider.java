package pl.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import pl.nbd.codec.TimeCodec;
import pl.nbd.dao.ClientDao;
import pl.nbd.model.Client;
import pl.nbd.model.Rent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentProvider {
    private final CqlSession session;
    private final TimeCodec timeCodec = new TimeCodec();

    public static final CqlIdentifier RENT_A_ROOM_NAMESPACE = CqlIdentifier.fromCql("rent_a_room");
    public static final CqlIdentifier RENTS_BY_CLIENT = CqlIdentifier.fromCql("rents_by_client");
    public static final CqlIdentifier RENTS_BY_ROOM = CqlIdentifier.fromCql("rents_by_room");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier ROOM_NUMBER = CqlIdentifier.fromCql("room_number");
    public static final CqlIdentifier START_DATE = CqlIdentifier.fromCql("start_date");
    public static final CqlIdentifier END_DATE = CqlIdentifier.fromCql("end_date");
    public static final CqlIdentifier PRICE = CqlIdentifier.fromCql("price");
    public static final CqlIdentifier ARCHIVE = CqlIdentifier.fromCql("archive");


    public RentProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Rent rent) {
        Insert insertClient = QueryBuilder.insertInto(RENTS_BY_CLIENT)
                .value(CLIENT_ID, literal(rent.getClient().getPersonalID()))
                .value(RENT_ID, literal(rent.getId()))
                .value(ROOM_NUMBER, literal(rent.getRoom().getRoomNumber()))
                .value(START_DATE, literal(rent.getBeginTime(), timeCodec))
                .value(END_DATE, literal(rent.getEndTime(), timeCodec))
                .value(PRICE, literal(rent.getRentCost()))
                .value(ARCHIVE, literal(rent.isArchive()));

        Insert insertRoom = QueryBuilder.insertInto(RENTS_BY_ROOM)
                .value(ROOM_NUMBER, literal(rent.getRoom().getRoomNumber()))
                .value(RENT_ID, literal(rent.getId()))
                .value(CLIENT_ID, literal(rent.getClient().getPersonalID()))
                .value(START_DATE, literal(rent.getBeginTime(), timeCodec))
                .value(END_DATE, literal(rent.getEndTime(), timeCodec))
                .value(PRICE, literal(rent.getRentCost()))
                .value(ARCHIVE, literal(rent.isArchive()));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertClient.build())
                .addStatement(insertRoom.build())
                .build();

        session.execute(batchStatement);
    }


    public List<Rent> findByClientId(long clientId) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_CLIENT)
                .all()
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(clientId)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();

        return convertRowsToRents(rows);
    }

    public List<Rent> findByRoomNumber(long roomNumber) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_ROOM)
                .all()
                .where(Relation.column(ROOM_NUMBER).isEqualTo(literal(roomNumber)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();

        return convertRowsToRents(rows);
    }

    private List<Rent> convertRowsToRents(List<Row> rows) {
        ArrayList<Rent> rents = new ArrayList<>();

        for (Row row : rows) {

            LocalDateTime startDate;
            if (row.isNull(START_DATE)) {
                startDate = null;
            } else {
                startDate = LocalDateTime.ofInstant(row.getInstant(START_DATE), ZoneOffset.UTC);
            }

            LocalDateTime endDate;
            if (row.isNull(END_DATE)) {
                endDate = null;
            } else {
                endDate = LocalDateTime.ofInstant(row.getInstant(END_DATE), ZoneOffset.UTC);
            }

            Rent rent = new Rent(
                    row.getLong(RENT_ID),
                    row.getLong(CLIENT_ID),
                    row.getLong(ROOM_NUMBER),
                    startDate,
                    endDate,
                    row.getDouble(PRICE),
                    row.getBoolean(ARCHIVE)
            );
            rents.add(rent);
        }
        return rents;
    }

    public void update(Rent rent) {
        Update updateClient = QueryBuilder.update(RENTS_BY_CLIENT)
                .setColumn(END_DATE, literal(rent.getEndTime(), timeCodec))
                .setColumn(PRICE, literal(rent.getRentCost()))
                .setColumn(ARCHIVE, literal(rent.isArchive()))
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(rent.getClient().getPersonalID())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getId())));

        Update updateRoom = QueryBuilder.update(RENTS_BY_ROOM)
                .setColumn(END_DATE, literal(rent.getEndTime(), timeCodec))
                .setColumn(PRICE, literal(rent.getRentCost()))
                .setColumn(ARCHIVE, literal(rent.isArchive()))
                .where(Relation.column(ROOM_NUMBER).isEqualTo(literal(rent.getRoom().getRoomNumber())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateClient.build())
                .addStatement(updateRoom.build())
                .build();

        session.execute(batchStatement);
    }

    public void remove(Rent rent) {
        Delete deleteClient = QueryBuilder.deleteFrom(RENTS_BY_CLIENT)
                .where(Relation.column(CLIENT_ID).isEqualTo(literal(rent.getClient().getPersonalID())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getId())));

        Delete deleteRoom = QueryBuilder.deleteFrom(RENTS_BY_ROOM)
                .where(Relation.column(ROOM_NUMBER).isEqualTo(literal(rent.getRoom().getRoomNumber())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteClient.build())
                .addStatement(deleteRoom.build())
                .build();

        session.execute(batchStatement);
    }
}