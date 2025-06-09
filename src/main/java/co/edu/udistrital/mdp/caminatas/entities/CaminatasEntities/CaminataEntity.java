package co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.GaleriaEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.ComentariosEntities.ComentariosEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.MapaEntity;

@Data
@Entity
@Table(name = "caminatas")
@EqualsAndHashCode(callSuper = true)
public class CaminataEntity extends BaseEntity {

    @Column(nullable = false)
    private String nombreCaminata;

    @Column(nullable = false)
    private BigDecimal costoCaminata;

    @Column
    private String patrocinador;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false)
    private String lugar;

    @Column(nullable = false)
    private String duracion;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DificultadCaminata dificultad;


    @Column(nullable = false)
    private String itinerario;

    @Column(nullable = false)
    private String recomendaciones;

    // Relaciones

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "mapa_id")
    @JsonManagedReference
    private MapaEntity mapa;

    /*
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id")
    private RutaEntity ruta;
    */

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "galeria_id")
    @JsonManagedReference
    private GaleriaEntity galeria;

    @OneToMany(mappedBy = "caminata", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentariosEntity> comentarios = new ArrayList<>();
}
