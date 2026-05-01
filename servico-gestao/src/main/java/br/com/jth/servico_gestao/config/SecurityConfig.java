package br.com.jth.servico_gestao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//       JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
//       authenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
//
//       http.sessionManagement(sessionManagementConfigurer ->
//               sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//               .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//               .csrf(AbstractHttpConfigurer::disable)
//               .authorizeHttpRequests(auth -> auth
//                       .requestMatchers("/public", "/public/**").permitAll() // add rotas que são públicas
//                       .requestMatchers("/private", "/private/**").authenticated() // add rotas que precisa estar autenticado
//                       .anyRequest().authenticated());
//
//       http.oauth2ResourceServer(rsc -> rsc
//               .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(authenticationConverter)));
//
//       return http.build();
//    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:5173"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
//        src.registerCorsConfiguration("/**", config);
//        return src;
//    }
//}