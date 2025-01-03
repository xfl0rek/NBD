package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;
import pl.nbd.providers.ClientProvider;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class, entityHelpers = {DefaultClient.class, PremiumClient.class})
    void create(Client client);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = ClientProvider.class, entityHelpers = {DefaultClient.class, PremiumClient.class})
    Client findById(long personalId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class, entityHelpers = {DefaultClient.class, PremiumClient.class})
    void update(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientProvider.class, entityHelpers = {DefaultClient.class, PremiumClient.class})
    void remove(long personalId);
}
