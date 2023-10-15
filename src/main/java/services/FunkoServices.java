package services;

import models.ModeloF;
import models.MyFunko;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FunkoServices {
    private static FunkoServices instance;
    //read all csv
    private List<MyFunko> funkos = readAllCSV("src/main/resources/funkos.csv").collectList().block();

    private FunkoServices() {

    }

    public static FunkoServices getInstance() {
        if (instance == null) {
            instance = new FunkoServices();
        }
        return instance;
    }

    public Mono<MyFunko> funkoMasCaro() {
        return Mono.just(funkos.stream().max(Comparator.comparing(MyFunko::precio)).get());


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
    }






