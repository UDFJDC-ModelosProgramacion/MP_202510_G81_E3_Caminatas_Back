package co.edu.udistrital.mdp.caminatas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.caminatas.entities.CaminataEntity;

public interface CaminataRepository extends JpaRepository<CaminataEntity, Long> {

    
}