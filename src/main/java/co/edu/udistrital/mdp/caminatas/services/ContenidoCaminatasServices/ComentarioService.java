package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.ComentarioRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.ComentarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.ComentariosEntities.ComentariosEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.ComentariosEntities.EstadoComentario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.I_ComentariosRepository;
import co.edu.udistrital.mdp.caminatas.repositories.InscripcionesRepositories.I_InscripcionUsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ComentarioService {

    private static final Logger logger = LoggerFactory.getLogger(ComentarioService.class);
    private final I_ComentariosRepository comentarioRepository;
    private final I_InscripcionUsuarioRepository inscripcionRepository;

    public ComentarioService(I_ComentariosRepository comentarioRepository, I_InscripcionUsuarioRepository inscripcionRepository) {
        this.comentarioRepository = comentarioRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    /**
     * Crea un nuevo comentario si el usuario ha participado en la caminata.
     */
    public ComentarioResponseDTO create(ComentarioRequestDTO dto) {
        // Aquí deberías inyectar y usar los repositorios de Usuario y Caminata para obtener las entidades completas
        UsuarioNaturalEntity autor = new UsuarioNaturalEntity();
        autor.setId(dto.getIdAutor());
        CaminataEntity caminata = new CaminataEntity();
        caminata.setId(dto.getIdCaminata());

        // Validar inscripción pagada
        boolean participo = inscripcionRepository.existsByUsuario_IdAndCaminata_IdAndEstadoPagoTrue(autor.getId(), caminata.getId());
        if (!participo) {
            throw new IllegalArgumentException("El usuario no puede comentar esta caminata porque no está inscrito o no ha pagado.");
        }

        ComentariosEntity comentario = new ComentariosEntity();
        comentario.setAutor(autor);
        comentario.setCaminata(caminata);
        comentario.setDescripcionComentario(dto.getDescripcionComentario());
        comentario.setCalificacion(dto.getCalificacion());
        comentario.setEstadoComentario(EstadoComentario.PENDIENTE);

        ComentariosEntity guardado = comentarioRepository.save(comentario);
        return toDto(guardado);
    }

    /**
     * Aprueba un comentario cambiando su estado a APROBADO.
     */
    public ComentarioResponseDTO aprobarComentario(Long id) {
        ComentariosEntity comentario = comentarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
        comentario.setEstadoComentario(EstadoComentario.APROBADO);
        return toDto(comentarioRepository.save(comentario));
    }

    /**
     * Rechaza un comentario cambiando su estado a RECHAZADO.
     */
    public ComentarioResponseDTO rechazarComentario(Long id) {
        ComentariosEntity comentario = comentarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
        comentario.setEstadoComentario(EstadoComentario.RECHAZADO);
        return toDto(comentarioRepository.save(comentario));
    }

    /**
     * Obtiene todos los comentarios como DTO.
     */
    public List<ComentarioResponseDTO> findAllComentariosDTO() {
        return comentarioRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }

    /**
     * Busca un comentario por su ID y lo devuelve como DTO.
     */
    public Optional<ComentarioResponseDTO> findById(Long id) {
        return comentarioRepository.findById(id).map(this::toDto);
    }

    /**
     * Elimina un comentario por su ID.
     */
    public void deleteById(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Comentario no encontrado");
        }
        comentarioRepository.deleteById(id);
        logger.info("🗑️ Comentario eliminado por admin. ID: {}", id);
    }

    // --- Mapeo de entidad a DTO ---
    public ComentarioResponseDTO toDto(ComentariosEntity comentario) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(comentario.getId());
        dto.setDescripcionComentario(comentario.getDescripcionComentario());
        dto.setCalificacion(comentario.getCalificacion());
        dto.setEstadoComentario(comentario.getEstadoComentario() != null ? comentario.getEstadoComentario().name() : null);
        dto.setNombreAutor(comentario.getAutor() != null ? comentario.getAutor().getNombreUsuario() : null);
        dto.setNombreCaminata(comentario.getCaminata() != null ? comentario.getCaminata().getNombreCaminata() : null);
        return dto;
    }
}