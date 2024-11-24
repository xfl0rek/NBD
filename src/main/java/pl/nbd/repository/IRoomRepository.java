package pl.nbd.repository;

import pl.nbd.model.Room;

import java.util.List;

public interface IRoomRepository {
    Room read(long roomId);
    List<Room> readAll();
    void create(Room room);
    void delete(long roomId);
    void update(Room room);
}
