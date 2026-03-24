package hutt.the.java.keycloak.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hutt.the.java.keycloak.config.Constantes.*;

@RestController
@RequestMapping("/teste")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "🔓 Público - qualquer um acessa";
    }

    @PreAuthorize(USER)
    @GetMapping("/user")
    public String userEndpoint(Authentication auth) {
        return "👤 USER: " + auth.getName();
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/admin")
    public String adminEndpoint(Authentication auth) {
        return "👑 ADMIN: " + auth.getName();
    }

    @PreAuthorize(ADMIN_OR_USER)
    @GetMapping("/user-or-admin")
    public String userOrAdmin(Authentication auth) {
        return "👥 USER ou ADMIN: " + auth.getName();
    }

    @PreAuthorize(ADMIN + " and " + USER)
    @GetMapping("/both")
    public String bothRoles(Authentication auth) {
        return "🔥 Precisa ter USER + ADMIN: " + auth.getName();
    }

    @GetMapping("/debug")
    public Object debug(Authentication auth) {
        return auth.getAuthorities();
    }
}
