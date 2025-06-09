package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;

@Data
@Entity
@Table(name = "galerias")
@EqualsAndHashCode(callSuper = true)
public class GaleriaEntity extends BaseEntity {

    @OneToOne(mappedBy = "galeria")
    @JsonBackReference
    private CaminataEntity caminata;

    @Column(nullable = false)
    private String imagenPrincipal;

    @ElementCollection
    @CollectionTable(name = "imagenes_galeria", joinColumns = @JoinColumn(name = "galeria_id"))
    private List<String> imagenesGaleria = new ArrayList<>();
}

