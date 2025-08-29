package co.com.bancolombia.api.dto;

public record UserDto(
        Long id,String nombre,
                      String apellido,
                      String fechaNacimiento,
                      Integer telefono,
                      String direccion,
                      String correo,
                      Long salario,
                      Long documento,
                      Long idRol,
                      String nombreRol

) {
}