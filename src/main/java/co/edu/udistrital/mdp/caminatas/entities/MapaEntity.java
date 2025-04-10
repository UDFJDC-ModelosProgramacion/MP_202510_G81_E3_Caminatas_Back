package co.edu.udistrital.mdp.caminatas.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class MapaEntity extends BaseEntity{
    private String UrlMapa;
    
}
