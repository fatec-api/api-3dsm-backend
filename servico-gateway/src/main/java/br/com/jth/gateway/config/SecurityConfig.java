package br.com.jth.gateway.config;

import br.com.jth.gateway.utils.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        
                        // --- EXCEÇÃO ---
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/apontamento/usuario/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/apontamento/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/apontamento/").authenticated()
                        
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/usuario/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/usuarios/**").authenticated()
                        
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/alocacoes/projeto/**").authenticated()
                        
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/itens/usuario/**").authenticated()
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/itens/projeto/**").authenticated()
                        
                        .pathMatchers(org.springframework.http.HttpMethod.GET, "/projetos/**").authenticated()

                        // --- REGRA GERAL ---
                        .pathMatchers("/gestao/**").hasAnyRole("GESTOR","FINANCEIRO") 
                        
                        .pathMatchers("/apontamento/**").hasAnyRole("GESTOR","FINANCEIRO") 

                        .pathMatchers("/auditoria/**").hasAnyRole("GESTOR","FINANCEIRO") 
                        
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

