package co.edu.udistrital.mdp.caminatas.repositories.NotificationRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.NotificationEntities.NotificacionEntity;

@Repository
public interface I_NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {
    List<NotificacionEntity> findByUsuarioCorreoOrderByFechaEnvioDesc(String correo);
    List<NotificacionEntity> findByUsuarioCorreoAndLeidaFalseOrderByFechaEnvioDesc(String correo);
    List<NotificacionEntity> findByUsuarioCorreo(String correo);

}

