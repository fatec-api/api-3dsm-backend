package br.com.jth.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import br.com.jth.gateway.utils.KeycloakRoleConverter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String jwkSetUri = "http://keycloak:8080/realms/java-the-hutt/protocol/openid-connect/certs";

        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        jwtDecoder.setJwtValidator(JwtValidators.createDefault());

        return jwtDecoder;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()

                        // --- EXCEÇÃO ---
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/apontamento/apontamento/usuario/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/gestao/itens/usuario/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.PUT, "/gestao/usuarios/atualizar/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/apontamento/apontamentos").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.PATCH, "/apontamento/apontamentos/*/status").hasAnyRole("GESTOR")
                        .pathMatchers(org.springframework.http.HttpMethod.PATCH, "/apontamento/apontamentos/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/gestao/clientes/**").authenticated()
                        
                        .pathMatchers("/gestao/clientes/**").hasAnyRole("GESTOR")

                        // --- REGRA GERAL ---
                        .pathMatchers("/gestao/**").hasAnyRole("GESTOR","PROFISSIONAL","FINANCEIRO")
                        .pathMatchers("/apontamento/**").hasAnyRole("GESTOR","PROFISSIONAL")
                        .pathMatchers("/auditoria/**").hasAnyRole("GESTOR")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
