package co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioAdministradorComentariosEntity;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.I_UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@RequiredArgsConstructor
public class SuperAdminInitializer {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminInitializer.class);
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${superadmin.email}")
    private String superAdminCorreo;

    @Value("${superadmin.password}")
    private String securePassword;

    @Bean
    public CommandLineRunner initSuperAdmin(I_UsuarioRepository usuarioRepository) {
        return args -> {
            if (!usuarioRepository.existsByCorreoIgnoreCase(superAdminCorreo)) {
                UsuarioAdministradorComentariosEntity admin = new UsuarioAdministradorComentariosEntity();
                admin.setNombreUsuario("superadmin");
                admin.setCorreo(superAdminCorreo);
                admin.setCedula(999999999L);
                admin.setTelefono(1234567890L);
                admin.setRol(RolUsuario.SUPER_ADMIN);
                admin.setPasswordHash(passwordEncoder.encode(securePassword));

                usuarioRepository.save(admin);
                logger.info("✅ SuperAdministrador creado.");
            } else {
                logger.info("ℹ️ SuperAdministrador ya existe. No se creó uno nuevo.");
            }
        };
    }
}

