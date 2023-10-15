package repositories;

import database.DatabaseService;

public class CRUDRepository {
    DatabaseService databaseService = DatabaseService.getInstance();
    public void insert(String query) {
        databaseService.executeQuery(query, "INSERT");
    }

    public void select(String query) {
        databaseService.executeQuery(query, "SELECT");
    }

    public void update(String query) {
        databaseService.executeQuery(query, "UPDATE");
    }

    public void delete(String query) {
        databaseService.executeQuery(query, "DELETE");
    }

    public void  create(String query) {
        databaseService.executeQuery(query, "CREATE");
    }
}
