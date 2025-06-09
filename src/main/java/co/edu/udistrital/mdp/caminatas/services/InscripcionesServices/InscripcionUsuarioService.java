package co.edu.udistrital.mdp.caminatas.services.InscripcionesServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InscripcionesRequestDTO.InscripcionUsuarioRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InscripcionesResponsesDTO.InscripcionUsuarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities.InscripcionUsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.CaminatasRepositories.I_CaminataRepository;
import co.edu.udistrital.mdp.caminatas.repositories.InscripcionesRepositories.I_InscripcionUsuarioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioNaturalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscripcionUsuarioService {

    private final I_InscripcionUsuarioRepository inscripcionUsuarioRepository;
    private final I_UsuarioNaturalRepository usuarioNaturalRepository;
    private final I_CaminataRepository caminataRepository;

    public List<InscripcionUsuarioResponseDTO> findAll() {
        return inscripcionUsuarioRepository.findAll().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public Optional<InscripcionUsuarioResponseDTO> findById(Long id) {
        return inscripcionUsuarioRepository.findById(id).map(this::toResponseDTO);
    }

    public void delete(Long id) {
        if (!inscripcionUsuarioRepository.existsById(id)) {
            throw new NotFoundException("Inscripción no encontrada");
        }
        inscripcionUsuarioRepository.deleteById(id);
    }

    public InscripcionUsuarioResponseDTO registrarDesdeDTO(InscripcionUsuarioRequestDTO dto) {
        if (inscripcionUsuarioRepository.existsByUsuario_IdAndCaminata_Id(dto.getIdUsuario(), dto.getIdCaminata())) {
            throw new ConflictException("⚠ El usuario ya tiene una inscripción en esta caminata.");
        }

        UsuarioNaturalEntity usuario = usuarioNaturalRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        CaminataEntity caminata = caminataRepository.findById(dto.getIdCaminata())
            .orElseThrow(() -> new NotFoundException("Caminata no encontrada"));

        InscripcionUsuarioEntity inscripcion = new InscripcionUsuarioEntity();
        inscripcion.setUsuario(usuario);
        inscripcion.setCaminata(caminata);
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion() != null ? dto.getFechaInscripcion() : LocalDate.now());
        inscripcion.setEstadoPago(false);

        return toResponseDTO(inscripcionUsuarioRepository.save(inscripcion));
    }

    public InscripcionUsuarioResponseDTO toResponseDTO(InscripcionUsuarioEntity inscripcion) {
        InscripcionUsuarioResponseDTO dto = new InscripcionUsuarioResponseDTO();
        dto.setId(inscripcion.getId());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setEstadoPago(inscripcion.getEstadoPago());

        if (inscripcion.getUsuario() != null) {
            dto.setUsuarioId(inscripcion.getUsuario().getId());
            dto.setNombreUsuario(inscripcion.getUsuario().getNombreUsuario());
        }

        if (inscripcion.getCaminata() != null) {
            dto.setCaminataId(inscripcion.getCaminata().getId());
            dto.setNombreCaminata(inscripcion.getCaminata().getNombreCaminata());
        }

        return dto;
    }
}