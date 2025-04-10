package co.edu.udistrital.mdp.caminatas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.caminatas.entities.MapaEntity;

public interface MapaRepository extends JpaRepository<MapaEntity, Long> {

    
}