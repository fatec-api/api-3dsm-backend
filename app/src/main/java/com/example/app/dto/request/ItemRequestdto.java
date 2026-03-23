package com.example.app.dto.request;


import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.internal.constraintvalidators.hv.pl.NIPValidator;

import java.sql.Time;
import java.time.LocalDate;

@Data
public class ItemRequestdto {
    @NotBlank
    private String código;

    @NotBlank
    private String descricao;

    @NotBlank
    private LocalDate dataAtribuicao;

    @NotBlank
    private Time previsaoHoras;

    @NotBlank
    private ItemModel.NivelAtividade nivelAtividade;

    @NotBlank
    private UsuarioModel  usuarioModel;

    @NotBlank
    private ProjetoModel projetoModel;





}
