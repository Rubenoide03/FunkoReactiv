import IO.CSVReader;

public class Main {
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        csvReader.readAllCSV("src/main/funkos.csv").subscribe(System.out::println);
    }
}