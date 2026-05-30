package br.com.jth.apontamento.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuscarItemPorIdRequestDTO {
    private Long itemId;
}
