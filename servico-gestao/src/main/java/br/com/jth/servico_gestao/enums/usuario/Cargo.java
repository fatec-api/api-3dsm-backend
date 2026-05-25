package br.com.jth.servico_gestao.enums.usuario;

public enum Cargo {
    PROFISSIONAL,
    GESTOR,
    ADMINISTRATIVO,
    FINANCEIRO;

    public static Cargo fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Cargo não pode ser nulo/vazio");
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cargo inválido: " + value +
                    ". Valores aceitos: PROFISSIONAL, GESTOR, ADMINISTRATIVO, FINANCEIRO");
        }
    }
}