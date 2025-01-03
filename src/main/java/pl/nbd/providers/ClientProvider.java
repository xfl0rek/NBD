package pl.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.nbd.model.Client;
import pl.nbd.model.DefaultClient;
import pl.nbd.model.PremiumClient;

public class ClientProvider {
    private final CqlSession session;

    private EntityHelper<DefaultClient> defaultClientHelper;
    private EntityHelper<PremiumClient> premiumClientHelper;

    public ClientProvider(MapperContext ctx, EntityHelper<DefaultClient> defaultClientHelper, EntityHelper<PremiumClient> premiumClientHelper) {
        this.session = ctx.getSession();
        this.defaultClientHelper = defaultClientHelper;
        this.premiumClientHelper = premiumClientHelper;
    }

    public void create(Client client) {
        session.execute(
                switch (client.getDiscriminator()) {
                    case "default" -> {
                        DefaultClient defaultClient = (DefaultClient) client;
                        yield session.prepare(defaultClientHelper.insert().build())
                                .bind()
                                .setLong("personal_id", defaultClient.getPersonalID())
                                .setString("first_name", defaultClient.getFirstName())
                                .setString("last_name", defaultClient.getLastName())
                                .setBoolean("archive", defaultClient.isArchive())
                                .setString("discriminator", "default");
                    }
                    case "premium" -> {
                        PremiumClient premiumClient = (PremiumClient) client;
                        yield session.prepare(premiumClientHelper.insert().build())
                                .bind()
                                .setLong("personal_id", premiumClient.getPersonalID())
                                .setString("first_name", premiumClient.getFirstName())
                                .setString("last_name", premiumClient.getLastName())
                                .setBoolean("archive", premiumClient.isArchive())
                                .setString("discriminator", "premium");
                    }
                    default -> {
                        throw new IllegalArgumentException();
                    }
                }
        );
    }

    public Client findById(long clientId) {
        Select selectClient = QueryBuilder.selectFrom(CqlIdentifier.fromCql("clients"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("personal_id")).isEqualTo(QueryBuilder.literal(clientId)));
        try {
            Row row = session.execute(selectClient.build()).one();
            String discriminator = row.getString("discriminator");
            return switch (discriminator) {
                case "default" -> getDefault(row);
                case "premium" -> getPremium(row);
                default -> throw new IllegalArgumentException();
            };
        } catch (NullPointerException e) {
            return null;
        }
    }

    private DefaultClient getDefault(Row row) {
        return new DefaultClient(
                row.getLong("personal_id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getBoolean("archive")
        );
    }

    private PremiumClient getPremium(Row row) {
        return new PremiumClient(
                row.getLong("personal_id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getBoolean("archive")
        );
    }


    public void update(Client client) {
        try {
            session.execute(
                    switch (client.getDiscriminator()) {
                        case "default" -> {
                            DefaultClient defaultClient = (DefaultClient) client;
                            yield session.prepare(defaultClientHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setLong("personal_id", defaultClient.getPersonalID())
                                    .setString("first_name", defaultClient.getFirstName())
                                    .setString("last_name", defaultClient.getLastName())
                                    .setBoolean("archive", defaultClient.isArchive())
                                    .setString("discriminator", "default");

                        }
                        case "premium" -> {
                            PremiumClient premiumClient = (PremiumClient) client;
                            yield session.prepare(premiumClientHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setLong("personal_id", premiumClient.getPersonalID())
                                    .setString("first_name", premiumClient.getFirstName())
                                    .setString("last_name", premiumClient.getLastName())
                                    .setBoolean("archive", premiumClient.isArchive())
                                    .setString("discriminator", "premium");

                        }
                        default -> throw new IllegalArgumentException();
                    }
            );
        } catch (NullPointerException e) {
            System.out.println("Client does not exist");
        }
    }

    public void remove(long clientId) {
        Delete deleteClient = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("clients"))
                .where(Relation.column(CqlIdentifier.fromCql("personal_id")).isEqualTo(QueryBuilder.literal(clientId)));
        session.execute(deleteClient.build());
    }
}
