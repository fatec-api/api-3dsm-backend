package br.com.jth.servico_gestao.mensageria.evento;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record UsuarioEventDTO(
        UUID id,
        String nomeUsuario,
        String email,
        Set<Cargo> cargos,
        BigDecimal valorHora,
        boolean ativo
) {}