package IO;

import models.ModeloF;
import models.MyFunko;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




public class CSVReader {

    public Flux<MyFunko> readAllCSV(String route_file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(route_file), StandardCharsets.UTF_8);
            List<MyFunko> myFunkoList = new ArrayList<>();

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
                    myFunkoList.add(myFunko);
                }
            }

            return Flux.fromIterable(myFunkoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
