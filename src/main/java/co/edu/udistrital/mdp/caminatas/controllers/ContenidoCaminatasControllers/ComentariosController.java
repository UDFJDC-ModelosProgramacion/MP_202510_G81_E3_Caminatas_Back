package co.edu.udistrital.mdp.caminatas.controllers.ContenidoCaminatasControllers;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.ComentarioRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.ComentarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
@RequiredArgsConstructor
@Tag(name = "Comentarios", description = "Comentarios y calificaciones sobre caminatas")
public class ComentariosController {

    private final ComentarioService comentarioService;

    @Operation(summary = "Crear un comentario")
    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> create(@Valid @RequestBody ComentarioRequestDTO dto) {
        ComentarioResponseDTO response = comentarioService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los comentarios")
    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.findAllComentariosDTO());
    }

    @Operation(summary = "Obtener un comentario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> getById(@PathVariable Long id) {
        return comentarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<ComentarioResponseDTO> aprobar(@PathVariable Long id) {
        ComentarioResponseDTO response = comentarioService.aprobarComentario(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<ComentarioResponseDTO> rechazar(@PathVariable Long id) {
        ComentarioResponseDTO response = comentarioService.rechazarComentario(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comentarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}