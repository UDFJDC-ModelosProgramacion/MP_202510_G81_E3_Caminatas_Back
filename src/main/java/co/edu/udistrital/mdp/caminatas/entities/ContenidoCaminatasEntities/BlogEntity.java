package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;

@Data
@Entity
@Table(name = "blogs")
@EqualsAndHashCode(callSuper = true)
public class BlogEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioNaturalEntity autor;

    @ElementCollection
    @CollectionTable(name = "blog_imagenes", joinColumns = @JoinColumn(name = "blog_id"))
    private List<String> imagenes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "blog_videos", joinColumns = @JoinColumn(name = "blog_id"))
    private List<String> videos = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String contenidoTextoBlog;
}


