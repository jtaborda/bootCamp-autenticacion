package co.com.bancolombia.api.dto;


public record EditUserDto(String nombre,
                          String apellido,
                          String fechaNacimiento,
                          Integer telefono,
                          String direccion,
                          String correo,
                          Long salario) {
}