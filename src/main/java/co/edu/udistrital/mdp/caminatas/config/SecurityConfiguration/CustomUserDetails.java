package co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;

public class CustomUserDetails implements UserDetails {
    private String correo;
    private RolUsuario rol;

    public CustomUserDetails(String correo, RolUsuario rol) {
        this.correo = correo;
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public RolUsuario getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name() ));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

