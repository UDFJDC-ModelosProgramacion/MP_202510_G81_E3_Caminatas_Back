package co.edu.udistrital.mdp.caminatas.services.CaminatasServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.CaminatasRequestDTO.CaminataRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.CoordenadaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.RutaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.CaminatasResponsesDTO.CaminataResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.GaleriaEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.Coordenadas;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.MapaEntity;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.RutaEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.CaminatasRepositories.I_CaminataRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaminataService {

    private final I_CaminataRepository caminataRepository;

    /**
     * Crea una nueva caminata a partir del DTO.
     */
    public CaminataEntity createFromDTO(CaminataRequestDTO dto) {
        CaminataEntity caminata = toEntity(dto);
        return caminataRepository.save(caminata);
    }

    /**
     * Actualiza una caminata existente con los datos del DTO.
     */
    public Optional<CaminataEntity> updateFromDTO(Long id, CaminataRequestDTO dto) {
        return caminataRepository.findById(id)
                .map(existing -> {
                    updateEntityFromDTO(existing, dto);
                    return caminataRepository.save(existing);
                });
    }

    /**
     * Obtiene todas las caminatas.
     */
    public List<CaminataEntity> findAll() {
        return caminataRepository.findAll();
    }

    /**
     * Busca una caminata por su ID.
     */
    public Optional<CaminataEntity> findById(Long id) {
        return caminataRepository.findById(id);
    }

    /**
     * Elimina una caminata por su ID.
     */
    public void delete(Long id) {
        if (!caminataRepository.existsById(id)) {
            throw new NotFoundException("Caminata no encontrada con ID: " + id);
        }
        caminataRepository.deleteById(id);
    }

    // 🔁 Conversión DTO -> Entity
    private CaminataEntity toEntity(CaminataRequestDTO dto) {
        CaminataEntity caminata = new CaminataEntity();
        updateEntityFromDTO(caminata, dto);
        return caminata;
    }
    //
    public CaminataResponseDTO toResponseDTO(CaminataEntity entity) {
        CaminataResponseDTO dto = new CaminataResponseDTO();

        dto.setId(entity.getId());
        dto.setNombreCaminata(entity.getNombreCaminata());
        dto.setCostoCaminata(entity.getCostoCaminata());
        dto.setPatrocinador(entity.getPatrocinador());
        dto.setFecha(entity.getFecha());
        dto.setHora(entity.getHora());
        dto.setLugar(entity.getLugar());
        dto.setDuracion(entity.getDuracion());
        dto.setDescripcion(entity.getDescripcion());
        dto.setDificultad(entity.getDificultad());
        dto.setItinerario(entity.getItinerario());
        dto.setRecomendaciones(entity.getRecomendaciones());

        // Relacionales
        if (entity.getMapa() != null) {
            dto.setMapa(mapToDTO(entity.getMapa()));
        }
        if (entity.getGaleria() != null) {
            dto.setGaleria(mapGaleriaToDTO(entity.getGaleria()));
        }

        dto.setTotalComentarios(entity.getComentarios() != null ? entity.getComentarios().size() : 0);

        return dto;
    }


    // 🔁 Reutiliza lógica para actualizar una entidad desde DTO
    private void updateEntityFromDTO(CaminataEntity entity, CaminataRequestDTO dto) {
        entity.setNombreCaminata(dto.getNombreCaminata());
        entity.setCostoCaminata(dto.getCostoCaminata());
        entity.setPatrocinador(dto.getPatrocinador());
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setLugar(dto.getLugar());
        entity.setDuracion(dto.getDuracion());
        entity.setDescripcion(dto.getDescripcion());
        entity.setDificultad(dto.getDificultad());
        entity.setItinerario(dto.getItinerario());
        entity.setRecomendaciones(dto.getRecomendaciones());
        // ✅ Mapa
        if (dto.getMapa() != null) {
            MapaEntity mapa = new MapaEntity();
            mapa.setDescripcion(dto.getMapa().getDescripcion());

            // 🔁 Mapear coordenadasGenerales DTO → Entity
            if (dto.getMapa().getCoordenadasGenerales() != null) {
                mapa.setCoordenadasGenerales(
                    dto.getMapa().getCoordenadasGenerales().stream()
                        .map(c -> new Coordenadas(c.getLatitud(), c.getLongitud()))
                        .toList()
                );
            }

            // 🔁 Mapear rutas
            if (dto.getMapa().getRutas() != null) {
                mapa.setRutas(dto.getMapa().getRutas().stream().map(rutaDto -> {
                    RutaEntity ruta = new RutaEntity();
                    ruta.setNombreRuta(rutaDto.getNombreRuta());
                    ruta.setDescripcionRuta(rutaDto.getDescripcionRuta());

                    // 🔁 Mapear coordenadas de la ruta
                    if (rutaDto.getCoordenadas() != null) {
                        ruta.setCoordenadas(
                            rutaDto.getCoordenadas().stream()
                                .map(coord -> new Coordenadas(coord.getLatitud(), coord.getLongitud()))
                                .toList()
                        );
                    }

                    return ruta;
                }).toList());
            }

            entity.setMapa(mapa);
        }

        // ✅ Galería
        if (dto.getGaleria() != null) {
            GaleriaEntity galeria = new GaleriaEntity();
            galeria.setImagenPrincipal(dto.getGaleria().getImagenPrincipal());
            galeria.setImagenesGaleria(dto.getGaleria().getImagenesGaleria());
            entity.setGaleria(galeria);
        }
        
    }
    // 🔁 Conversión de Mapa y Galería a DTO
    private MapaRequestDTO mapToDTO(MapaEntity mapa) {
        MapaRequestDTO dto = new MapaRequestDTO();
        dto.setDescripcion(mapa.getDescripcion());

        if (mapa.getCoordenadasGenerales() != null) {
            dto.setCoordenadasGenerales(
                mapa.getCoordenadasGenerales().stream()
                    .map(c -> new CoordenadaRequestDTO(c.getLatitud(), c.getLongitud()))
                    .toList()
            );
        }

        if (mapa.getRutas() != null) {
            dto.setRutas(
                mapa.getRutas().stream().map(r -> {
                    RutaRequestDTO rDto = new RutaRequestDTO();
                    rDto.setNombreRuta(r.getNombreRuta());
                    rDto.setDescripcionRuta(r.getDescripcionRuta());
                    rDto.setCoordenadas(
                        r.getCoordenadas().stream()
                            .map(coord -> new CoordenadaRequestDTO(coord.getLatitud(), coord.getLongitud()))
                            .toList()
                    );
                    return rDto;
                }).toList()
            );
        }

        return dto;
    }

    private GaleriaRequestDTO mapGaleriaToDTO(GaleriaEntity galeria) {
        GaleriaRequestDTO dto = new GaleriaRequestDTO();
        dto.setImagenPrincipal(galeria.getImagenPrincipal());
        dto.setImagenesGaleria(galeria.getImagenesGaleria());
        return dto;
    }


}
