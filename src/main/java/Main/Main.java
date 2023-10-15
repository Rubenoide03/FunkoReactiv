package Main;

import controllers.FunkoController;

public class Main {
    public static void main(String[] args) {
        FunkoController funkoProgram = FunkoController.getInstance();
        funkoProgram.init();

    }
}