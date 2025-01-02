package pl.nbd.mappers;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.dao.RoomDao;

@Mapper
public interface RoomMapper {
    @DaoFactory
    RoomDao roomDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    RoomDao roomDao();
}
