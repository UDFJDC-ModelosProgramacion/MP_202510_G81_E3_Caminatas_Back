package co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 💡 Para usar @PreAuthorize con roles
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    //    Roles de usuario: SUPER_ADMIN, ADMIN_COMENTARIOS, NATURAL, JURIDICO
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // ✅ Activa CORS
            // ⚠️ Desactivar H2 para producción, solo para desarrollo
            //.csrf(csrf -> csrf.ignoringRequestMatchers("/api/h2-console/**").disable())
            //.headers(headers -> headers.frameOptions().disable())
            .csrf(csrf -> csrf.disable())
            //.addFilterBefore(rateLimitingFilter, JwtAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Permitir login y registro
                .requestMatchers("/usuarios/naturales/login", "/usuarios/naturales").permitAll()
                .requestMatchers("/usuarios/juridicos/login", "/usuarios/juridicos").permitAll()
                .requestMatchers("/usuarios/admin-comentarios/login").permitAll()
                .requestMatchers("/admin/super/login").permitAll()
                // Permitir verificación de cuentas
                .requestMatchers("/usuarios/naturales/verificacion/confirmar").permitAll()
                .requestMatchers("/usuarios/juridicos/verificacion/confirmar").permitAll()
                .requestMatchers("/usuarios/admin-comentarios/verificacion/confirmar").permitAll()

                // Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // H2 Console (para desarrollo)
                .requestMatchers("/h2-console/**").permitAll()

                // Rutas GET públicas como galerías, blogs, caminatas:
                .requestMatchers(HttpMethod.GET, "/galerias/**", "/blogs/**", "/caminatas/**", "/mapas/**", "/rutas/**").permitAll()
                .requestMatchers("/comentarios").permitAll()
                .requestMatchers("/galerias").permitAll()
                .requestMatchers("/blogs").permitAll()
                .requestMatchers("/caminatas").permitAll()
                .requestMatchers("/mapas").permitAll()
                .requestMatchers("/rutas").permitAll()
                // Permitir acceso a inscripciones de caminatas
                .requestMatchers("/inscripciones").permitAll()

                // El resto, requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // ⚠️ Ajusta según tu frontend
        //configuration.setAllowedOriginPatterns(List.of("*")); // o usa dominios reales cuando tengas hosting

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Si usas cookies o JWT con auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}




