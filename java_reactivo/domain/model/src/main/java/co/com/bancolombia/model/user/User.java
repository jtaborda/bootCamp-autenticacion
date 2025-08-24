package co.com.bancolombia.model.user;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User
{

    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private Integer telefono;
    private String direccion;
    private String correo;
    private Long salario;
    private Long documento;
}
