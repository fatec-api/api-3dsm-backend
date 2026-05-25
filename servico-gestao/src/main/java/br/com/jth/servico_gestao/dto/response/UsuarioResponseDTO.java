package br.com.jth.servico_gestao.dto.response;

import br.com.jth.servico_gestao.enums.usuario.Cargo;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nomeUsuario,
        String email,
        BigDecimal valorHora,
        Set<Cargo> cargos,
        String nivelExperiencia,
        boolean ativo,
        Timestamp criado_em
) {}