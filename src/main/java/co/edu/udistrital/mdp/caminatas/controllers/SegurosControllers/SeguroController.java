package co.edu.udistrital.mdp.caminatas.controllers.SegurosControllers;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.SegurosRequestDTO.SeguroRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.SegurosResponsesDTO.SeguroResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.SegurosServices.SeguroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Seguros", description = "Gestión de seguros para caminatas")
@RestController
@RequestMapping("/seguros")
@RequiredArgsConstructor
public class SeguroController {

    private final SeguroService seguroService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Crear un nuevo seguro (solo SUPER_ADMIN)")
    @PostMapping("/crear")
    public ResponseEntity<SeguroResponseDTO> crear(@Valid @RequestBody SeguroRequestDTO dto) {
        SeguroResponseDTO creado = seguroService.crearSeguro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<SeguroResponseDTO>> getAll() {
        return ResponseEntity.ok(seguroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguroResponseDTO> getById(@PathVariable Long id) {
        return seguroService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seguroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}