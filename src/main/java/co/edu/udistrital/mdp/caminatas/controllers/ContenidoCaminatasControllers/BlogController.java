package co.edu.udistrital.mdp.caminatas.controllers.ContenidoCaminatasControllers;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.BlogRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.BlogResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@Tag(name = "Blogs", description = "Gestión de blogs relacionados con caminatas")
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "Crear un nuevo blog")
    @PostMapping
    public ResponseEntity<BlogResponseDTO> create(@Valid @RequestBody BlogRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.create(dto));
    }

    @Operation(summary = "Obtener todos los blogs")
    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAll() {
        return ResponseEntity.ok(blogService.getAll());
    }

    @Operation(summary = "Obtener un blog por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getById(id));
    }

    @Operation(summary = "Actualizar un blog por ID")
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BlogRequestDTO dto) {
        return ResponseEntity.ok(blogService.update(id, dto));
    }

    @Operation(summary = "Eliminar un blog por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}