package database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager implements Closeable {
    private static DatabaseManager controller;
    private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
     String serverURL;
     String serverPort;
        String databaseName;
    private String user;
    private String password;
    private String initScript;
    private String jdbcDriver;
    private String connectionUrl;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private DatabaseManager(){
        logger.debug("Loading database properties");
        initScript();
    }
    public static DatabaseManager getInstance(){
        if(controller == null){
            controller = new DatabaseManager();
        }
        return controller;
    }
    private void initScript(){
        var FileProps = ClassLoader.getSystemResource("database.properties").getFile();
        var props = new Properties();
        try {
            props.load(new FileInputStream(FileProps));
            serverURL = props.getProperty("database.url","localhost");
            serverPort = props.getProperty("database.port","3306");
            databaseName = props.getProperty("database.name","funkos");
            user = props.getProperty("database.user","ruben");
            password = props.getProperty("database.password","1234");
            connectionUrl =props.getProperty("database.connectionUrl","jdbc:sqlite:"+databaseName+";DB_CLOSE_DELAY=-1");
            initScript = props.getProperty("database.initScript",ClassLoader.getSystemResource("Data.sql").getFile());
            logger.debug("Configuración de acceso a la Base de Datos cargada");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
        public void open(){
            try {
                if(connection!=null && !connection.isClosed()){
                    logger.debug("Conexion ya establecida");
                }
                connection = DriverManager.getConnection(connectionUrl,user,password);
                logger.debug("Conexion establecida en"+connectionUrl);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public void close(){
        try {
            if(preparedStatement!=null)
                preparedStatement.close();
            if(connection!=null)
                connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
    }





