package pl.nbd.repository;

import pl.nbd.model.Room;

import java.util.List;

public class DecoratorRoomRepository implements IRoomRepository {
    private final RoomRepository repository;
    private final RedisRoomRepository redisRoomRepository;

    public DecoratorRoomRepository(RoomRepository repository, RedisRoomRepository redisRoomRepository) {
        this.repository = repository;
        this.redisRoomRepository = redisRoomRepository;
    }
    
    @Override
    public Room read(long roomId) {
        Room room = redisRoomRepository.read(roomId);
        if (room == null) {
            room = repository.read(roomId);
            if (room != null && room.getRented() == 0) {
                redisRoomRepository.create(room);
            }
            return room;
        }
        return room;
    }

    @Override
    public List<Room> readAll() {
        List<Room> rooms = repository.readAll();
        for (Room room : rooms) {
            if (room.getRented() == 0)
                redisRoomRepository.create(room);
        }
        return rooms;
    }

    @Override
    public void create(Room room) {
        repository.create(room);
        redisRoomRepository.create(room);

    }

    @Override
    public void delete(long roomId) {
        redisRoomRepository.delete(roomId);
        repository.delete(roomId);
    }

    @Override
    public void update(Room room) {
        repository.update(room);
        if (room.getRented() == 1) {
            redisRoomRepository.delete(room.getRoomNumber());
        } else {
            redisRoomRepository.update(room);
        }
    }
}
