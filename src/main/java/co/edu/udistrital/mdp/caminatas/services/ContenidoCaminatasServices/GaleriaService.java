package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.GaleriaResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.GaleriaEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.I_GaleriaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GaleriaService {

    private final I_GaleriaRepository galeriaRepository;

    public GaleriaResponseDTO save(GaleriaRequestDTO dto) {
        GaleriaEntity galeria = new GaleriaEntity();
        galeria.setImagenPrincipal(dto.getImagenPrincipal());
        galeria.setImagenesGaleria(new ArrayList<>(dto.getImagenesGaleria()));
        return toResponseDTO(galeriaRepository.save(galeria));
    }

    public List<GaleriaResponseDTO> findAll() {
        return galeriaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<GaleriaResponseDTO> findById(Long id) {
        return galeriaRepository.findById(id).map(this::toResponseDTO);
    }

    public void delete(Long id) {
        if (!galeriaRepository.existsById(id)) {
            throw new NotFoundException("Galería no encontrada con ID: " + id);
        }
        galeriaRepository.deleteById(id);
    }

    // --- Mapeo de entidad a DTO ---
    public GaleriaResponseDTO toResponseDTO(GaleriaEntity entity) {
        GaleriaResponseDTO dto = new GaleriaResponseDTO();
        dto.setId(entity.getId());
        dto.setImagenPrincipal(entity.getImagenPrincipal());
        dto.setImagenesGaleria(entity.getImagenesGaleria());
        return dto;
    }
}