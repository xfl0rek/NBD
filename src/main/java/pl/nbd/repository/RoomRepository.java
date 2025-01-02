package pl.nbd.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import pl.nbd.dao.RoomDao;
import pl.nbd.mappers.RoomMapper;
import pl.nbd.model.Room;

import java.util.List;

public class RoomRepository  {
//    @Override
//    public void close() throws Exception {
//
//    }

    private final CqlSession session;
    private final RoomMapper roomMapper;
    private final RoomDao roomDao;

    public RoomRepository(CqlSession session) {
        this.session = session;
        this.roomMapper = new RoomMapperBuilder(session).build();
        this.roomDao = roomMapper.roomDao();
    }

    public void create(Room room) {
        roomDao.create(room);
    }

    public List<Room> readAll(long id) {
        return roomDao.getAllRooms(id);
    }

//    public Room read(long id) {
//
//    }

    public void update(Room room) {

    }

    public void delete(long id) {
    }
}
