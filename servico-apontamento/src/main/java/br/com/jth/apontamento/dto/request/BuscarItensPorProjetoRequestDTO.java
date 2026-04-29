package br.com.jth.apontamento.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuscarItensPorProjetoRequestDTO {
    private Long projetoId;
}