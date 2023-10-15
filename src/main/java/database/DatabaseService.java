package database;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class DatabaseService {
    private static DatabaseService controller;
    private  final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    public static DatabaseService getInstance() {
        if (controller == null) {
            controller = new DatabaseService();
        }
        return controller;
    }




    ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "h2")
            .option(ConnectionFactoryOptions.PROTOCOL, "tcp")
            .option(ConnectionFactoryOptions.HOST, "localhost")
            .option(ConnectionFactoryOptions.PORT, 3306)
            .option(ConnectionFactoryOptions.USER, "ruben")
            .option(ConnectionFactoryOptions.PASSWORD, "1234")
            .option(ConnectionFactoryOptions.DATABASE, "funkos")
            .build();


    ConnectionFactory connectionFactory = ConnectionFactories.get(options);
    Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
    public void executeQuery(String query, String operation) {
        connectionMono.flatMapMany(connection -> connection.createStatement(query).execute())
                .subscribe(result -> logger.info(operation + " La operacion ha sido un exito"));
    }
    public void insert(String query) {
        executeQuery(query, "INSERT");
    }

    public void select(String query) {
        executeQuery(query, "SELECT");
    }

    public void update(String query) {
        executeQuery(query, "UPDATE");
    }

    public void delete(String query) {
        executeQuery(query, "DELETE");
    }

    public void  create(String query) {
        executeQuery(query, "CREATE");
    }






}




