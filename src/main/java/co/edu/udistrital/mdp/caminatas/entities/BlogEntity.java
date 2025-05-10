package co.edu.udistrital.mdp.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class BlogEntity extends BaseEntity{
    private String nombre;
    private String autor;
    private String descripcion;
    private String imagen;
    private String video;
    private String comentario;
}