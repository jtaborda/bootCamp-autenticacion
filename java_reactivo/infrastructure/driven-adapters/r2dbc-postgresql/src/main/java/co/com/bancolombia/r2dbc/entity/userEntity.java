package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class userEntity {
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private Integer telefono;
    private String direccion;
    private Long salario;
    private String correo;
    private Long documento;
    private Long idRol;
    private String password;
}
