package co.edu.udistrital.mdp.caminatas.controllers.ContenidoCaminatasControllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.MapaResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices.MapaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mapas")
@RequiredArgsConstructor
@Tag(name = "Mapas", description = "Gestión de mapas y sus rutas asociadas")
public class MapaController {

    private final MapaService mapaService;

    @Operation(summary = "Crear un nuevo mapa con rutas")
    @ApiResponse(responseCode = "201", description = "Mapa creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o número de rutas fuera de rango")
    @PostMapping
    public ResponseEntity<MapaResponseDTO> crearMapa(@Valid @RequestBody MapaRequestDTO dto) {
        MapaResponseDTO creado = mapaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Obtener todos los mapas")
    @GetMapping
    public ResponseEntity<List<MapaResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(mapaService.findAll());
    }

    @Operation(summary = "Obtener un mapa por ID")
    @ApiResponse(responseCode = "200", description = "Mapa encontrado")
    @ApiResponse(responseCode = "404", description = "Mapa no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<MapaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return mapaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un mapa por ID")
    @ApiResponse(responseCode = "204", description = "Mapa eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Mapa no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mapaService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}