package services;

import models.ModeloF;
import models.MyFunko;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunkoServices {
    private static FunkoServices instance;
    List<MyFunko> funkos;

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
    public Flux<Map<ModeloF,List<MyFunko>>> funkosPorModelo(){
        return Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo)));
    }
    //numero de funkos por modelos
    public Flux<Map<ModeloF,Long>> numerodeFunkosPorModelo(){
        return Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo,Collectors.counting())));

    }
    /*


     */
    public Flux<List<MyFunko>> funkosLanzados2023(){
        return Flux.just(funkos.stream().filter(myFunko -> myFunko.fecha().getYear()==2023).toList());
    }

    public Flux<Map<List<MyFunko>, Long>> numStitchList() {
        return Flux.just(funkos.stream()
                .filter(myFunko -> myFunko.nombre().contains("Stitch"))
                .collect(Collectors.groupingBy(MyFunko::nombre, Collectors.counting())));
    }


}
