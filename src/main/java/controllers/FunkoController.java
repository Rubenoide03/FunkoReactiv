package controllers;

import models.MyFunko;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FunkoController {
    private static FunkoController instance;
    List<MyFunko> funkos;
    private FunkoController(){

    }
    public static FunkoController getInstance(){
        if(instance==null){
            instance=new FunkoController();}
        return instance;
    }
    public Optional<MyFunko> funkoMasCaro( ){
        return funkos.stream().max(Comparator.comparingDouble(funko -> funko.precio()));


    }





}
