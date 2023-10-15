package controllers;
import services.FunkoServices;

public class FunkoController {
    private static FunkoController instance;
    private FunkoServices funkoServices;

    private FunkoController() {
        funkoServices = FunkoServices.getInstance();
    }

    public static FunkoController getInstance() {
        if (instance == null) {
            instance = new FunkoController();
        }
        return instance;
    }

    public void funkoMasCaro() {
        funkoServices.funkoMasCaro().subscribe(System.out::println);
    }

    public void precioMedio() {
        funkoServices.precioMedio().subscribe(System.out::println);
    }

    public void funkosPorModelo() {
        funkoServices.funkosPorModelo().subscribe(System.out::println);
    }

    public void numerodeFunkosPorModelo() {
        funkoServices.numerodeFunkosPorModelo().subscribe(System.out::println);
    }

    public void funkosLanzados2023() {
        funkoServices.funkosLanzados2023().subscribe(System.out::println);
    }

    public void numStitchList() {
        funkoServices.numStitchList().subscribe(System.out::println);
    }



}
