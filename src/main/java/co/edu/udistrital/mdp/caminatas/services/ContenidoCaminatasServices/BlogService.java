package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.BlogRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.BlogResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.BlogEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.I_BlogRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioNaturalRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final I_BlogRepository blogRepository;
    private final I_UsuarioNaturalRepository usuarioNaturalRepository;

    public BlogResponseDTO create(BlogRequestDTO dto) {
        BlogEntity blog = toEntity(dto);
        return toResponseDTO(blogRepository.save(blog));
    }

    public BlogResponseDTO update(Long id, BlogRequestDTO dto) {
        BlogEntity existing = blogRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Blog no encontrado con ID: " + id));
        updateEntityFromDTO(existing, dto);
        return toResponseDTO(blogRepository.save(existing));
    }

    public BlogResponseDTO getById(Long id) {
        return blogRepository.findById(id)
            .map(this::toResponseDTO)
            .orElseThrow(() -> new NotFoundException("Blog no encontrado con ID: " + id));
    }

    public List<BlogResponseDTO> getAll() {
        return blogRepository.findAll().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public void delete(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new NotFoundException("Blog no encontrado con ID: " + id);
        }
        blogRepository.deleteById(id);
    }

    // --- Métodos de mapeo ---

    private BlogEntity toEntity(BlogRequestDTO dto) {
        UsuarioNaturalEntity autor = usuarioNaturalRepository.findById(dto.getAutorId())
            .orElseThrow(() -> new NotFoundException("Autor no encontrado con ID: " + dto.getAutorId()));
        BlogEntity blog = new BlogEntity();
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setAutor(autor);
        blog.setImagenes(dto.getImagenes() != null ? dto.getImagenes() : new ArrayList<>());
        blog.setVideos(dto.getVideos() != null ? dto.getVideos() : new ArrayList<>());
        blog.setContenidoTextoBlog(dto.getContenidoTextoBlog());
        return blog;
    }

    private void updateEntityFromDTO(BlogEntity blog, BlogRequestDTO dto) {
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setContenidoTextoBlog(dto.getContenidoTextoBlog());
        blog.setImagenes(dto.getImagenes() != null ? dto.getImagenes() : new ArrayList<>());
        blog.setVideos(dto.getVideos() != null ? dto.getVideos() : new ArrayList<>());
        if (dto.getAutorId() != null) {
            UsuarioNaturalEntity autor = usuarioNaturalRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new NotFoundException("Autor no encontrado con ID: " + dto.getAutorId()));
            blog.setAutor(autor);
        }
    }

    public BlogResponseDTO toResponseDTO(BlogEntity entity) {
        BlogResponseDTO dto = new BlogResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setContenidoTextoBlog(entity.getContenidoTextoBlog());
        dto.setImagenes(entity.getImagenes());
        dto.setVideos(entity.getVideos());

        BlogResponseDTO.AutorDTO autorDTO = new BlogResponseDTO.AutorDTO();
        UsuarioNaturalEntity autor = entity.getAutor();
        autorDTO.setId(autor.getId());
        autorDTO.setNombreUsuario(autor.getNombreUsuario());
        autorDTO.setCorreo(autor.getCorreo());
        dto.setAutor(autorDTO);

        return dto;
    }
}