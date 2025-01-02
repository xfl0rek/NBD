package pl.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;


public class RoomProvider {
    private final CqlSession session;

    private EntityHelper<RoomChildren> roomChildrenHelper;
    private EntityHelper<RoomRegular> roomRegularHelper;

    public RoomProvider(MapperContext ctx, EntityHelper<RoomChildren> roomChildrenHelper, EntityHelper<RoomRegular> roomRegularHelper) {
        this.session = ctx.getSession();
        this.roomChildrenHelper = roomChildrenHelper;
        this.roomRegularHelper = roomRegularHelper;
    }

    public void create(Room room) {
        session.execute(
                switch (room.getDiscriminator()) {
                    case "children" -> {
                        RoomChildren roomChildren = (RoomChildren) room;
                        yield session.prepare(roomChildrenHelper.insert().build())
                                .bind()
                                .setLong("room_number", roomChildren.getRoomNumber())
                                .setInt("base_price", roomChildren.getBasePrice())
                                .setInt("room_capacity", roomChildren.getRoomCapacity())
                                .setInt("number_of_children", roomChildren.getNumberOfChildren())
                                .setString("discriminator", "children")
                                .setInt("rented", roomChildren.getRented());
                    }
                    case "regular" -> {
                        RoomRegular roomRegular = (RoomRegular) room;
                        yield session.prepare(roomRegularHelper.insert().build())
                                .bind()
                                .setLong("room_number", roomRegular.getRoomNumber())
                                .setInt("base_price", roomRegular.getBasePrice())
                                .setInt("room_capacity", roomRegular.getRoomCapacity())
                                .setBoolean("breakfast_included", roomRegular.isBreakfastIncluded())
                                .setString("discriminator", "regular")
                                .setInt("rented", roomRegular.getRented());
                    }
                    default -> throw new IllegalArgumentException();
                }
        );
    }

    public Room findById(long roomNumber) {
        Select selectRoom = QueryBuilder.selectFrom(CqlIdentifier.fromCql("rooms"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("room_number")).isEqualTo(QueryBuilder.literal(roomNumber)));
        Row row = session.execute(selectRoom.build()).one();
        String discriminator = row.getString("discriminator");
        return switch (discriminator) {
            case "children" -> getChildren(row);
            case "regular" -> getRegular(row);
            default -> throw new IllegalArgumentException();
        };
    }

    private RoomChildren getChildren(Row row) {
        return new RoomChildren(
                row.getLong("room_number"),
                row.getInt("base_price"),
                row.getInt("room_capacity"),
                row.getInt("number_of_children")

        );
    }

    private RoomRegular getRegular(Row row) {
        return new RoomRegular(
                row.getLong("room_number"),
                row.getInt("base_price"),
                row.getInt("room_capacity"),
                row.getBoolean("breakfast_included")
        );
    }

    public void remove(long roomNumber) {
        Delete deleteRoom = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("rooms"))
                .where(Relation.column(CqlIdentifier.fromCql("room_number")).isEqualTo(QueryBuilder.literal(roomNumber)));
        session.execute(deleteRoom.build());
    }
}
