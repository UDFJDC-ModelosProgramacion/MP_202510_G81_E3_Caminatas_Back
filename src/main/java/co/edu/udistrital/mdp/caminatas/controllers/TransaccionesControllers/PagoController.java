package co.edu.udistrital.mdp.caminatas.controllers.TransaccionesControllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO.PagoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO.PagoResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.TransaccionesServices.PagoService;

import java.util.List;

@Tag(name = "Pagos", description = "Gestión de pagos para facturas")
@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @Operation(summary = "Simular o registrar pago", description = "Realiza un pago (modo MOCK = aprobado automático)")
    @PreAuthorize("hasAnyRole('NATURAL', 'JURIDICO')")
    @PostMapping
    public ResponseEntity<PagoResponseDTO> simularPago(@Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pagoService.toResponseDTO(pagoService.simularPago(dto)));
    }

    @Operation(summary = "Pago mock simple", description = "Simula un pago aprobado directamente con monto y método")
    @PostMapping("/mock")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PagoResponseDTO> pagoMock(@RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pagoService.toResponseDTO(
                pagoService.registrarPagoMock(dto.getFacturaId(), dto.getMonto(), dto.getMetodo())
            ));
    }

    @Operation(summary = "Listar todos los pagos activos")
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> getAll() {
        return ResponseEntity.ok(
            pagoService.findAll().stream()
                .map(pagoService::toResponseDTO)
                .toList()
        );
    }

    @Operation(summary = "Buscar pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> getById(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(pagoService::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar (soft delete) un pago por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}