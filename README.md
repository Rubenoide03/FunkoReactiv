# FunkoReactiv

Un proyecto hecho de manera reactiva que incorpora el CRUD , lectura de un archivo CSV asi como diferentes queries.

## Autores 
 * Ruben Fernandez



## Tabla de Contenidos

- [Instalación](#Instalación)
- [Configuracion](#Configuracion)


## Instalación

Para instalar este proyecto, se necesitan las siguientes dependencias:
* Java 17(Liberica es la que se uso para el desarrollo)
* Un CSV con sus datos separados por ,
* Gradle

## Configuracion
Este es el archivo de configuracion de la base de datos
```
db.username=ruben
db.password=1712
db.name=default
db.loadTables=true
db.stringDB=r2dbc:h2:file:///./funkos_db?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```
[Metodo FindByNombre] 
Lo que hacemos en el siguiente metodo es buscar por nombre en la base de datos y devolver un objeto de tipo MyFunko usando el .builder() de Lombok
Aparte en la parte del .bind(0, "%" + nombre + "%") es para que busque por cualquier nombre que contenga la palabra que le pasemos por parametro
``` 
public static Flux<MyFunko> findByNombre(String nombre) {
            logger.debug("Buscando todos los alumnos por nombre");
            String sql = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
            return Flux.usingWhen(
                    connectionFactory.create(),
                    connection -> Flux.from(connection.createStatement(sql)
                            .bind(0, "%" + nombre + "%")
                            .execute()
                    ).flatMap(result -> result.map((row, rowMetadata) ->
        MyFunko.builder()
                .cod(row.get("id", UUID.class))
                .nombre(row.get("nombre", String.class))
                .modelo(row.get("modelo", ModeloF.class))
                .precio(row.get("precio", Double.class))
                .fecha(row.get("fecha", LocalDate.class))
                .created_at(row.get("created_at", LocalDateTime.class))
                .updated_at(row.get("updated_at", LocalDateTime.class))
                .build()
        )),
        Connection::close);
    }
    ´´´





    


