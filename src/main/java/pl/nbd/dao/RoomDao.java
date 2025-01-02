package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.*;

import pl.nbd.model.Room;

import java.util.List;

@Dao
public interface RoomDao {
    @Insert
    void create(Room room);

    @Query("SELECT * FROM rooms")
    Room getAllRooms(long id);
}
