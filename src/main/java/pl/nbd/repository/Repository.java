package pl.nbd.repository;

import java.util.List;

public interface Repository<T> {
    void create(T t);
    T read(long id);
    void update(T t);
    void delete(T t);
//    List<T> getAll();
}
