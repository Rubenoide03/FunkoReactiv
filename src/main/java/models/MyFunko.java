package models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public record MyFunko(UUID cod, String nombre, ModeloF modelo, double precio, LocalDate fecha, LocalDateTime created_at,
                      LocalDateTime updated_at
) {



}
