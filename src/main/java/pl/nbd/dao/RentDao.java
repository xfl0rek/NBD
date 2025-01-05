package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.nbd.model.Rent;
import pl.nbd.providers.RentProvider;

import java.util.List;

@Dao
public interface RentDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void create(Rent rent);
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void update(Rent rent);

//    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
//    @QueryProvider(providerClass = RentProvider.class)
//    Rent findById(long rentId);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByClientId(long clientId);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByRoomNumber(long roomNumber);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void remove(Rent rent);
}
