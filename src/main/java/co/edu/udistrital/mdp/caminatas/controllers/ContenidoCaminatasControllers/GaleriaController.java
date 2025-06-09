package co.edu.udistrital.mdp.caminatas.controllers.ContenidoCaminatasControllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.GaleriaResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices.GaleriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/galerias")
@RequiredArgsConstructor
@Tag(name = "Galerías", description = "Operaciones sobre galerías de caminatas")
public class GaleriaController {

    private final GaleriaService galeriaService;

    @Operation(summary = "Crear nueva galería")
    @PostMapping
    public ResponseEntity<GaleriaResponseDTO> create(@Valid @RequestBody GaleriaRequestDTO dto) {
        GaleriaResponseDTO galeria = galeriaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(galeria);
    }

    @Operation(summary = "Listar todas las galerías")
    @GetMapping
    public ResponseEntity<List<GaleriaResponseDTO>> findAll() {
        return ResponseEntity.ok(galeriaService.findAll());
    }

    @Operation(summary = "Buscar galería por ID")
    @GetMapping("/{id}")
    public ResponseEntity<GaleriaResponseDTO> findById(@PathVariable Long id) {
        return galeriaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar galería por ID", description = "Solo accesible para usuarios con rol SUPER_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        galeriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}