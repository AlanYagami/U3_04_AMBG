package mx.edu.utez.actividad3_4.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;

    @Column(name = "numero_telefono", nullable = false, length = 15)
    private String numeroTelefono;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;
}