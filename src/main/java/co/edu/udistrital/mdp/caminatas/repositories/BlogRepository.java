
package co.edu.udistrital.mdp.caminatas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.BlogEntity;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> 
 {

}
