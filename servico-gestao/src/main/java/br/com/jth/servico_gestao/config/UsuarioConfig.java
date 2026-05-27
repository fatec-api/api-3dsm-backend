package br.com.jth.servico_gestao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class UsuarioConfig {
    // @Bean
    // public BCryptPasswordEncoder bCryptPasswordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }
    
    // não precisa e causa CORS error: MultipleAllowOriginValues
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/cadastrar/usuario").permitAll()
//                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }


}
