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
db.stringDB=r2dbc:h2:file:///./funkos_db?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE```



    


