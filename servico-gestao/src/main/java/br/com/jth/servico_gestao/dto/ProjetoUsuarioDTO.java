
package br.com.jth.servico_gestao.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjetoUsuarioDTO {
    @NotNull
    private Long projetoId;

    @NotNull
    private List<UUID> usuarioId;
}
