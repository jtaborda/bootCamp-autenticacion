package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;

public record CreateUserDto(
        @NotBlank(message = "El nombre no puede estar vacío")
         String nombre,
        @NotBlank(message = "El apellido no puede estar vacío")
         String apellido,
         String fechaNacimiento,
         Integer telefono,
         String direccion,
        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El correo no tiene un formato válido")
         String correo,
        @NotNull(message = "El salario no puede ser nulo")
        @Min(value = 0, message = "El salario no puede ser menor a 0")
        @Max(value = 15000000, message = "El salario no puede ser mayor a 15,000,000")
        Long salario,
              Long documento
) {
}