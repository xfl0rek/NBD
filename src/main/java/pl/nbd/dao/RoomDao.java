package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.*;

import pl.nbd.model.Room;
import pl.nbd.model.RoomChildren;
import pl.nbd.model.RoomRegular;
import pl.nbd.providers.RoomProvider;

@Dao
public interface RoomDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RoomProvider.class, entityHelpers = {RoomChildren.class, RoomRegular.class})
    void create(Room room);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RoomProvider.class, entityHelpers = {RoomChildren.class, RoomRegular.class})
    Room findById(long roomNumber);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RoomProvider.class, entityHelpers = {RoomChildren.class, RoomRegular.class})
    void update(Room room);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RoomProvider.class, entityHelpers = {RoomChildren.class, RoomRegular.class})
    void remove(long roomNumber);
}
