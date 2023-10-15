package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
    private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager DatabaseManager;
    private DatabaseManager databaseManager;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (DatabaseManager == null) {
            DatabaseManager = new DatabaseManager();
        }
        return DatabaseManager;
    }
}

