package br.com.jth.apontamento.config;

public class Constantes {
    // Roles
    public static final String USER = "hasAuthority('USER')";
    public static final String ADMIN = "hasAuthority('ADMIN')";
    public static final String ADMIN_OR_USER = "hasAuthority('ADMIN') or #userId == authentication.name";
}

