package co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import jakarta.servlet.FilterChain;
import org.springframework.lang.NonNull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ") &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String correo = jwtUtil.getUsernameFromToken(token);
                RolUsuario rol = jwtUtil.getRoleFromToken(token);

                CustomUserDetails userDetails = new CustomUserDetails(correo, rol);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // No repitas la validación (ya fue hecha arriba)
                logger.warn("❌ Token JWT inválido o expirado");
            }
        }

        filterChain.doFilter(request, response);
    }
}
