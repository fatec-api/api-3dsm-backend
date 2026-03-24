package hutt.the.java.keycloak.config;

public class Constantes {
    // Roles
    public static final String USER = "hasRole('USER')";
    public static final String ADMIN = "hasRole('ADMIN')";
    public static final String ADMIN_OR_USER = "hasAnyRole('USER', 'ADMIN')";
    }
