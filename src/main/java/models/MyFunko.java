package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
public record MyFunko(UUID cod, String nombre, ModeloF modelo, double precio, LocalDate fecha, LocalDateTime created_at,
                      LocalDateTime updated_at
)  {


}
