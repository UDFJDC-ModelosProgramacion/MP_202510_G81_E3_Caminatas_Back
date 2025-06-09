package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.CoordenadaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.RutaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.CoordenadaResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.RutaResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.Coordenadas;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.RutaEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.MapaRutasRepositories.I_RutaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final I_RutaRepository rutaRepository;

    public RutaResponseDTO save(RutaRequestDTO dto) {
        RutaEntity ruta = new RutaEntity();
        ruta.setNombreRuta(dto.getNombreRuta());
        ruta.setDescripcionRuta(dto.getDescripcionRuta());

        List<Coordenadas> coordenadas = buildCoordenadas(dto.getCoordenadas());
        if (coordenadas == null || coordenadas.size() < 2) {
            throw new IllegalArgumentException("Una ruta debe tener al menos dos coordenadas.");
        }

        ruta.setCoordenadas(coordenadas);
        return toResponseDTO(rutaRepository.save(ruta));
    }

    public List<RutaResponseDTO> findAll() {
        return rutaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<RutaResponseDTO> findById(Long id) {
        return rutaRepository.findById(id).map(this::toResponseDTO);
    }

    public void delete(Long id) {
        if (!rutaRepository.existsById(id)) {
            throw new NotFoundException("Ruta no encontrada con ID: " + id);
        }
        rutaRepository.deleteById(id);
    }

    // --- Métodos de mapeo ---
    private RutaResponseDTO toResponseDTO(RutaEntity entity) {
        RutaResponseDTO dto = new RutaResponseDTO();
        dto.setId(entity.getId());
        dto.setNombreRuta(entity.getNombreRuta());
        dto.setDescripcionRuta(entity.getDescripcionRuta());
        dto.setCoordenadas(entity.getCoordenadas().stream()
                .map(this::toCoordenadaResponseDTO)
                .toList());
        return dto;
    }

    private CoordenadaResponseDTO toCoordenadaResponseDTO(Coordenadas entity) {
        CoordenadaResponseDTO dto = new CoordenadaResponseDTO();
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        return dto;
    }

    private List<Coordenadas> buildCoordenadas(List<CoordenadaRequestDTO> coordenadaDTOs) {
        return coordenadaDTOs.stream()
                .map(dto -> new Coordenadas(dto.getLatitud(), dto.getLongitud()))
                .toList();
    }
}