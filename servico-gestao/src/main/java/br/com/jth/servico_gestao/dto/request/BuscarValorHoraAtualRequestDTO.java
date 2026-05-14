package br.com.jth.servico_gestao.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuscarValorHoraAtualRequestDTO {
    private UUID usuarioUuid;
}