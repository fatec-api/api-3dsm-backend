package com.example.app.dto.response;


import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemResponsedto {

    private String código;
    private String descricao;
    private LocalDateTime dataAtribuicao;
    private Time previsaoHoras;
    private ItemModel.NivelAtividade nivelAtividade;
    private UsuarioModel usuarioModel;
    private ProjetoModel projetoModel;

}
