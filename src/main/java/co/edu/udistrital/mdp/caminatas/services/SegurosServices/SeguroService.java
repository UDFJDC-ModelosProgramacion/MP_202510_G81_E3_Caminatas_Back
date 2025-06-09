package co.edu.udistrital.mdp.caminatas.services.SegurosServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.SegurosRequestDTO.SeguroRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.SegurosResponsesDTO.SeguroResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.SeguroEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroAdicionalEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroBasicoObligatorioEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroJuridicoEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroAdicionalRepository;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroBasicoObligatorioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroJuridicoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeguroService {

    private final I_SeguroAdicionalRepository seguroAdicionalRepository;
    private final I_SeguroBasicoObligatorioRepository seguroBasicoRepository;
    private final I_SeguroJuridicoRepository seguroJuridicoRepository;

    public SeguroResponseDTO crearSeguro(SeguroRequestDTO dto) {
        SeguroEntity creado;
        switch (dto.getTipoSeguro()) {
            case BASICO -> {
                var seguro = new SeguroBasicoObligatorioEntity();
                seguro.setDescripcionSeguro(dto.getDescripcionSeguro());
                seguro.setCostoSeguro(dto.getCostoSeguro());
                creado = seguroBasicoRepository.save(seguro);
            }
            case JURIDICO -> {
                var seguro = new SeguroJuridicoEntity();
                seguro.setDescripcionSeguro(dto.getDescripcionSeguro());
                seguro.setCostoSeguro(dto.getCostoSeguro());
                creado = seguroJuridicoRepository.save(seguro);
            }
            case ADICIONAL -> {
                var seguro = new SeguroAdicionalEntity();
                seguro.setDescripcionSeguro(dto.getDescripcionSeguro());
                seguro.setCostoSeguro(dto.getCostoSeguro());
                seguro.setDetalleAdicional(dto.getDetalleAdicional());
                creado = seguroAdicionalRepository.save(seguro);
            }
            default -> throw new IllegalArgumentException("Tipo de seguro no válido.");
        }
        return toResponseDTO(creado);
    }

    public List<SeguroResponseDTO> findAll() {
        List<SeguroResponseDTO> todos = new ArrayList<>();
        seguroBasicoRepository.findAll().forEach(s -> todos.add(toResponseDTO(s)));
        seguroAdicionalRepository.findAll().forEach(s -> todos.add(toResponseDTO(s)));
        seguroJuridicoRepository.findAll().forEach(s -> todos.add(toResponseDTO(s)));
        return todos;
    }

    public Optional<SeguroResponseDTO> findById(Long id) {
        Optional<? extends SeguroEntity> base = seguroBasicoRepository.findById(id);
        if (base.isPresent()) return base.map(this::toResponseDTO);

        Optional<? extends SeguroEntity> adicional = seguroAdicionalRepository.findById(id);
        if (adicional.isPresent()) return adicional.map(this::toResponseDTO);

        return seguroJuridicoRepository.findById(id).map(this::toResponseDTO);
    }

    public void delete(Long id) {
        if (seguroBasicoRepository.existsById(id)) {
            seguroBasicoRepository.deleteById(id);
        } else if (seguroAdicionalRepository.existsById(id)) {
            seguroAdicionalRepository.deleteById(id);
        } else if (seguroJuridicoRepository.existsById(id)) {
            seguroJuridicoRepository.deleteById(id);
        } else {
            throw new NotFoundException("Seguro no encontrado con ID: " + id);
        }
    }

    public SeguroResponseDTO toResponseDTO(SeguroEntity entity) {
        SeguroResponseDTO dto = new SeguroResponseDTO();
        dto.setId(entity.getId());
        dto.setDescripcionSeguro(entity.getDescripcionSeguro());
        dto.setCostoSeguro(entity.getCostoSeguro());
        dto.setTipoSeguro(entity.getTipoSeguro().name());

        // Solo para ADICIONAL
        if (entity instanceof SeguroAdicionalEntity adicional) {
            dto.setDetalleAdicional(adicional.getDetalleAdicional());
        }
        return dto;
    }
}
