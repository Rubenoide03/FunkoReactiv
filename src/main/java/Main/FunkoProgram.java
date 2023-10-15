package Main;

import services.FunkoServices;

public class FunkoProgram {
    FunkoProgram instance;
    private FunkoProgram(){

    }
    public FunkoProgram getInstance(){
        if(instance==null){
            instance=new FunkoProgram();}
        return instance;
    }
    public void init(){
        printConsoleData();

    }
    private void printConsoleData(){
        FunkoServices funkoController= FunkoServices.getInstance();
        System.out.println("Funko mas caro"+funkoController.funkoMasCaro());
        System.out.println("Funkos ordenador por modelo"+funkoController.funkosPorModelo());
    }
}
