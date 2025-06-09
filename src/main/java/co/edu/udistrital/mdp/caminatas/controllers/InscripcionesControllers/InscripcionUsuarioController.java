package co.edu.udistrital.mdp.caminatas.controllers.InscripcionesControllers;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InscripcionesRequestDTO.InscripcionUsuarioRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InscripcionesResponsesDTO.InscripcionUsuarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.InscripcionesServices.InscripcionUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inscripciones", description = "Manejo de inscripciones de usuarios a caminatas")
@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionUsuarioController {

    private final InscripcionUsuarioService inscripcionUsuarioService;

    @PreAuthorize("hasAnyRole('NATURAL', 'JURIDICO')")
    @Operation(summary = "Registrar inscripción de un usuario a una caminata")
    @PostMapping
    public ResponseEntity<InscripcionUsuarioResponseDTO> registrar(@Valid @RequestBody InscripcionUsuarioRequestDTO dto) {
        InscripcionUsuarioResponseDTO inscripcion = inscripcionUsuarioService.registrarDesdeDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
    }

    @Operation(summary = "Listar todas las inscripciones")
    @GetMapping
    public ResponseEntity<List<InscripcionUsuarioResponseDTO>> getAll() {
        return ResponseEntity.ok(inscripcionUsuarioService.findAll());
    }

    @Operation(summary = "Obtener inscripción por ID")
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionUsuarioResponseDTO> getById(@PathVariable Long id) {
        return inscripcionUsuarioService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una inscripción por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscripcionUsuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}