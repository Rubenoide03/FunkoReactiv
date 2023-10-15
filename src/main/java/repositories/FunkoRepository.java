package repositories;

import database.DatabaseService;
import models.MyFunko;
import repositories.CRUDRepository;

import java.util.LinkedHashMap;

public class FunkoRepository {
    private final DatabaseService databaseService = DatabaseService.getInstance();
    private static FunkoRepository instance;
    private static int MAX_SIZE = 25;
    CRUDRepository crudRepository = new CRUDRepository();
    private LinkedHashMap<Integer, MyFunko> cache = new LinkedHashMap<Integer, MyFunko>(MAX_SIZE, 0.75f, true) {
        protected boolean removeEldestEntry(java.util.Map.Entry<Integer, MyFunko> eldest) {
            return size() > MAX_SIZE;
        }
    };
    public static FunkoRepository getInstance() {
        if (instance == null) {
            instance = new FunkoRepository();
        }
        return instance;
    }
    public void insert(MyFunko funko) {
    crudRepository.insert("INSERT INTO funkos (nombre, modelo, precio, fecha) VALUES ('" + funko.nombre() + "', '" + funko.modelo() + "', " + funko.precio() + ", '" + funko.fecha() + "')");
    }
    public void findAll() {
        crudRepository.select("SELECT * FROM funkos");
    }
    public void update(MyFunko funko) {
        crudRepository.update("UPDATE funkos SET nombre = '" + funko.nombre() + "', modelo = '" + funko.modelo() + "', precio = " + funko.precio() + ", fecha = '" + funko.fecha() + "' WHERE cod = " + funko.cod());
    }
    public void delete(MyFunko funko) {
        crudRepository.delete("DELETE FROM funkos WHERE cod = " + funko.cod());
    }
    public void findByName(MyFunko funko){
        crudRepository.select("SELECT * FROM funkos WHERE nombre = " + funko.nombre());
    }



}
