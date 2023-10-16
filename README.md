# FunkoReactiv

Un proyecto hecho de manera reactiva que incorpora el CRUD , lectura de un archivo CSV asi como diferentes queries.

## Autores 
 * Ruben Fernandez



## Tabla de Contenidos

- [Instalación](#Instalación)
- [Configuracion](#Configuracion)


## Instalación

Para instalar este proyecto, se necesitan las siguientes dependencias:
* Java 17(Liberica es la que se usó para el desarrollo)
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
 
Lo que hacemos en el siguiente metodo es buscar por nombre en la base de datos y devolver un objeto de tipo MyFunko usando él .builder() de Lombok
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
````
Lo siguiente son las queries requeridas tales como funkoMasCaro, media de precio etc.
Usando Mono y Flux para hacerlo reactivo.
- Lo primero que hacemos es declarar una Lista de MyFunko y posteriormente leemos el CSV y recoge la informacion en una lista de MyFunko
* Luego en la query de funkoMasCaro usamos mono debido a que solo va a devolver un funko y usamos él .max para que nos devuelva el funko más caro
comparando el precio de cada funko.
* En la query de precioMedio usamos Mono y él .average para que nos devuelva la media de los precios de los funkos.
* En la query de funkosPorModelo usamos Flux porque devuelve mas de un objeto y el .groupingBy para que nos devuelva un Map con los funkos agrupados por modelo.
* En la query de numerodeFunkosPorModelo usamos Flux y el .groupingBy para que nos devuelva un Map con el numero de funkos agrupados por modelo.
* En la query de funkosLanzados2023 usamos Flux y el .filter para que nos devuelva los funkos que tengan la fecha de 2023.
* En la query de funkosStitch usamos Flux y el .filter para que nos devuelva los funkos que contengan la palabra Stitch.
* En la query de numeroFunkosStitch usamos Mono ya que lo que nos va a devolver es un unico valor que es un Double en este caso y el .filter para que nos devuelva el numero de funkos que contengan la palabra Stitch.
* En el metodo readAllCSV lo que hacemos es leer el CSV y recoger la informacion en una lista de MyFunko cuyo split sea comas y posteriormente recopilamos los datos obtenido de el archivo y añadimos los Funkos a la lista 
````
private List<MyFunko> funkos;

        {
            try {
                funkos = readAllCSV("src/main/resources/funkos.csv").collectList().block();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private FunkoServices() {
        super(DatabaseManager.getInstance());
    }


    public static FunkoServices getInstance() {
        if (instance == null) {
            instance = new FunkoServices();
        }
        return instance;
    }

    public Mono<MyFunko> funkoMasCaro() {
        try{
            return Mono.just(funkos.stream().max(Comparator.comparing(MyFunko::precio)).get());
        }catch (Exception e){
            throw new RuntimeException(e);
        }



    }

    public Mono<Double> precioMedio() {
        return Mono.just(funkos.stream().mapToDouble(MyFunko::precio).average().getAsDouble());
    }

    public Flux<Map<ModeloF, List<MyFunko>>> funkosPorModelo() {
        return Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo)));
    }

    public Flux<Map<ModeloF, Long>> numerodeFunkosPorModelo() {
        return Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo, Collectors.counting())));

    }


    public Flux<List<MyFunko>> funkosLanzados2023() {
        return Flux.just(funkos.stream().filter(myFunko -> myFunko.fecha().getYear() == 2023).toList());
    }
    //Numero de funkos de Stitch y listado de ellos
    public Flux<List<MyFunko>> funkosStitch() {
        return Flux.just(funkos.stream().filter(myFunko -> myFunko.nombre().contains("Stitch")).toList());


    }
    public Mono<Long> numeroFunkosStitch() {
        return Mono.just(funkos.stream().filter(myFunko -> myFunko.nombre().contains("Stitch")).count());
    }


    public Flux<MyFunko> readAllCSV(String route_file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(route_file), StandardCharsets.UTF_8);
            List<MyFunko> funkos = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {
                String[] lines_split = lines.get(i).split(",");
                if (lines_split.length == 5) {
                    DecimalFormat df = new DecimalFormat("#.###");
                    UUID cod = UUID.fromString(lines_split[0].substring(0, 35));
                    String nombre = lines_split[1];
                    ModeloF modelo = ModeloF.valueOf(lines_split[2]);
                    double precio = Double.parseDouble(lines_split[3].replace(',', '.'));
                    LocalDate fecha = LocalDate.parse(lines_split[4]);
                    LocalDateTime createdAt = LocalDateTime.now();
                    LocalDateTime updatedAt = LocalDateTime.now();
                    MyFunko myFunko = new MyFunko(cod, nombre, modelo, precio, fecha, createdAt, updatedAt);
                    funkos.add(myFunko);

                }
            }

            return Flux.fromIterable(funkos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
````
* En la clase CurrencyDateUtilFormatter lo que hacemos es formatear los datos de la clase MyFunko con el formato de España.
* Usamos la clase Locale para que nos formatee los datos con el formato de España.
* Creamos una instancia y un contructor privado para que no se pueda instanciar la clase.
* Hacemo un singleton para que solo se pueda instanciar una vez la clase.
* Usamos el metodo formatLocalDate y le pasamos por parametro un LocalDate que es del mismo tipo que fecha de la clase MyFunko 
* DateTimeFormatter para que nos formatee los datos de la clase POJO que esten en LocalDate con un formato LONG y con el Locale de España.
* Usamos el metodo formatLocalDateTime y le pasamos por parametro un LocalDateTime que es del mismo tipo que created_at y updated_at de la clase MyFunko
````
package utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;


public class CurrencyDateUtilFormatter {


    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");
    private static CurrencyDateUtilFormatter instance;

    private CurrencyDateUtilFormatter() {
    }

    public static CurrencyDateUtilFormatter getInstance() {
        if (instance == null) {
            instance = new CurrencyDateUtilFormatter();
        }
        return instance;
    }


    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(SPANISH_LOCALE);
        return dateTimeFormatter.format(date);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(SPANISH_LOCALE);
        return dateTimeFormatter.format(dateTime);
    }

    public static String formatLocalCurrency(double money) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(SPANISH_LOCALE);
        return currencyFormat.format(money);
    }


}


    
````
* Esta es la clase POJO que es un record.
* Usamos el @Builder para que nos cree un objeto de tipo MyFunko con el .builder() de Lombok
* Usamos el @Data para que nos cree los getters y setters de los atributos de la clase
* Usamos el @Override para que nos sobreescriba el metodo toString() y nos devuelva los datos de la clase en un String
* Usamos el CurrencyDateUtilFormatter para que nos formatee los datos de la clase con el formato de España.
* 

````
package models;

import lombok.Builder;
import lombok.Data;
import utils.CurrencyDateUtilFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record MyFunko(UUID cod, String nombre, ModeloF modelo, double precio, LocalDate fecha, LocalDateTime created_at,
                      LocalDateTime updated_at
) {


    @Override
    public String toString() {
        return "MyFunko{" +
                "cod=" + cod +
                ", nombre='" + nombre + '\'' +
                ", modelo=" + modelo +
                ", precio=" + CurrencyDateUtilFormatter.formatLocalCurrency(precio) +
                ", fecha=" + CurrencyDateUtilFormatter.formatLocalDate(fecha)  +
                ", created_at=" + CurrencyDateUtilFormatter.formatLocalDateTime(created_at)  +
                ", updated_at=" + CurrencyDateUtilFormatter.formatLocalDateTime(updated_at) +
                '}';
    }
}
````
Este es el ENUM que usa la CLASE POJO MyFunko incluyendo los cuatro tipos
````
package models;

public enum ModeloF {
    ANIME, DISNEY,MARVEL,OTROS
}

````





    


