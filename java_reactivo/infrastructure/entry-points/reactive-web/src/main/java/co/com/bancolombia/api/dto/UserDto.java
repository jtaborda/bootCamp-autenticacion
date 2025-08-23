package co.com.bancolombia.api.dto;

import java.time.LocalDate;
import java.util.Date;

public record UserDto(String nombre,
                      String apellido,
                      String fechaNacimiento,
                      Integer telefono,
                      String direccion,
                      String correo,
                      Long salario
) {
}