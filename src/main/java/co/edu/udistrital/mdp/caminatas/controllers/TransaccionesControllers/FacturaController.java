package co.edu.udistrital.mdp.caminatas.controllers.TransaccionesControllers;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO.FacturaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO.FacturaResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.TransaccionesServices.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facturas", description = "Gestión de facturación para inscripciones de caminatas")
@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @Operation(summary = "Generar factura para una inscripción", 
        description = "Genera una factura con seguros aplicables para el usuario autenticado")
    @ApiResponse(responseCode = "201", description = "Factura generada exitosamente")
    @PreAuthorize("hasRole('NATURAL') or hasRole('JURIDICO')")
    @PostMapping("/generar")
    public ResponseEntity<FacturaResponseDTO> generarFactura(
            @Valid @RequestBody FacturaRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        FacturaResponseDTO response = facturaService.generarFactura(dto, userDetails.getUsername());      
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Obtener todas las facturas")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> getAll() {
        return ResponseEntity.ok(facturaService.findAllDTO());
    }

    @Operation(summary = "Obtener una factura por ID")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'NATURAL', 'JURIDICO')")
    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Long id) {
        return facturaService.findByIdDTO(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una factura por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
