package com.example.app.dto.response;

import com.example.app.model.entity.ItemModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemResponsedto {

    private Long id;
    private String codigo;
    private String descricao;
    private LocalDate dataAtribuicao;
    private Integer previsaoHoras;
    private ItemModel.NivelAtividade nivelAtividade;
    private Long projetoId;
    private String projetoNome;
    private String usuarioNome;
}