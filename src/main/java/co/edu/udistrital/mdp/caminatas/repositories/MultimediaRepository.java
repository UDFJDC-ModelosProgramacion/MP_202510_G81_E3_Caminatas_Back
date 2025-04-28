package co.edu.udistrital.mdp.caminatas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.caminatas.entities.MultimediaEntity;

public interface MultimediaRepository extends JpaRepository<MultimediaEntity, Long> {
    
}
