package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.CoordenadaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.RutaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.CoordenadaResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.MapaResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.RutaResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.Coordenadas;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.MapaEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.RutaEntity;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.MapaRutasRepositories.I_MapaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapaService {

    private final I_MapaRepository mapaRepository;

    public MapaResponseDTO save(MapaRequestDTO dto) {
        validarCantidadRutas(dto.getRutas());

        MapaEntity mapa = new MapaEntity();
        mapa.setDescripcion(dto.getDescripcion());

        if (dto.getCoordenadasGenerales() != null) {
            mapa.setCoordenadasGenerales(convertirCoordenadas(dto.getCoordenadasGenerales()));
        }

        mapa.setRutas(convertirRutas(dto.getRutas()));

        return toResponseDTO(mapaRepository.save(mapa));
    }

    public List<MapaResponseDTO> findAll() {
        return mapaRepository.findAll().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public Optional<MapaResponseDTO> findById(Long id) {
        return mapaRepository.findById(id).map(this::toResponseDTO);
    }

    public void delete(Long id) {
        if (!mapaRepository.existsById(id)) {
            throw new IllegalArgumentException("Mapa no encontrado con ID: " + id);
        }
        mapaRepository.deleteById(id);
    }

    // ---------- Métodos auxiliares privados ------------------

    private void validarCantidadRutas(List<RutaRequestDTO> rutas) {
        if (rutas == null || rutas.isEmpty() || rutas.size() > 5) {
            throw new IllegalArgumentException("El mapa debe tener entre 1 y 5 rutas.");
        }
    }

    private List<Coordenadas> convertirCoordenadas(List<CoordenadaRequestDTO> coordenadasDTO) {
        return coordenadasDTO.stream()
            .map(c -> new Coordenadas(c.getLatitud(), c.getLongitud()))
            .toList();
    }

    private List<RutaEntity> convertirRutas(List<RutaRequestDTO> rutasDTO) {
        return rutasDTO.stream().map(r -> {
            RutaEntity ruta = new RutaEntity();
            ruta.setNombreRuta(r.getNombreRuta());
            ruta.setDescripcionRuta(r.getDescripcionRuta());
            ruta.setCoordenadas(convertirCoordenadas(r.getCoordenadas()));
            return ruta;
        }).toList();
    }

    // --- Mapeo de entidad a DTO ---
    private MapaResponseDTO toResponseDTO(MapaEntity entity) {
        MapaResponseDTO dto = new MapaResponseDTO();
        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcion());
        
        // Coordenadas generales
        if (entity.getCoordenadasGenerales() != null) {
            dto.setCoordenadasGenerales(entity.getCoordenadasGenerales().stream()
                .map(this::toCoordenadaResponseDTO)
                .toList());
        }

        // Rutas
        if (entity.getRutas() != null) {
            dto.setRutas(entity.getRutas().stream()
                .map(this::toRutaResponseDTO)
                .toList());
        }

        return dto;
    }

    private CoordenadaResponseDTO toCoordenadaResponseDTO(Coordenadas entity) {
        CoordenadaResponseDTO dto = new CoordenadaResponseDTO();
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        return dto;
    }

    private RutaResponseDTO toRutaResponseDTO(RutaEntity entity) {
        RutaResponseDTO dto = new RutaResponseDTO();
        dto.setNombreRuta(entity.getNombreRuta());
        dto.setDescripcionRuta(entity.getDescripcionRuta());
        dto.setCoordenadas(entity.getCoordenadas().stream()
            .map(this::toCoordenadaResponseDTO)
            .toList());
        return dto;
    }
}