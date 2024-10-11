package pl.nbd.repository;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T> {
    void create(T t);
    T read(long id);
    void update(T t);
    void delete(T t);
}
