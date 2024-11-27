package pl.nbd.managers;

import org.openjdk.jmh.annotations.*;
import pl.nbd.model.Room;
import pl.nbd.repository.DecoratorRoomRepository;
import pl.nbd.repository.RedisRoomRepository;
import pl.nbd.repository.RoomRepository;

@State(Scope.Benchmark)
public class BenchmarkTest {
    private RedisRoomRepository redisRoomRepository;
    private RoomRepository roomRepository;
    private DecoratorRoomRepository decoratorRoomRepository;
    private RoomManager roomManager;

    private int numberOfRooms;

    @Setup
    public void init() {
        redisRoomRepository = new RedisRoomRepository();
        roomRepository = new RoomRepository("benchmarktestdb");
        decoratorRoomRepository = new DecoratorRoomRepository(roomRepository, redisRoomRepository);
        roomManager = new RoomManager(decoratorRoomRepository);
        numberOfRooms = 10;
        for (int i = 1; i <= numberOfRooms; i++) {
            roomManager.registerRoom(i, 100, 2, 2);
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromCache() {
        for (int i = 1; i <= numberOfRooms; i++) {
            roomManager.getRoom(i);
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromMongoUsingManager() {
        redisRoomRepository.clearCache();
        for (int i = 1; i <= numberOfRooms; i++) {
            roomManager.getRoom(i);
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Fork(value = 2)
    public void readFromMongoUsingRepository() {
        for (int i = 1; i <= numberOfRooms; i++) {
            roomRepository.read(i);
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @TearDown
    public void cleanUp() {
        roomRepository.getDatabase().getCollection("rooms", Room.class).drop();
        redisRoomRepository.clearCache();
    }
}
