package co.com.bancolombia.api.dto;

public record UserDto(String nombre,
                      String apellido,
                      String fechaNacimiento,
                      Integer telefono,
                      String direccion,
                      String correo,
                      Long salario,Long documento
) {
}