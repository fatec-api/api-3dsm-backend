package com.example.app.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import com.example.app.model.entity.ItemModel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequestdto {

    @NotBlank(message = "O campo de título não pode estar vazio.")
    @Size(max = 300, message = "Título deve ter no máximo 300 caracteres.")
    private String titulo;

    @NotBlank(message = "O campo de descrição não pode estar vazio.")
    @Size(max = 300, message = "Descrição deve ter no máximo 300 caracteres.")
    private String descricao;

    private LocalDate dataAtribuicao;

    @Min(value = 0, message = "A previsão de horas não pode ser negativa.") // ✅ era 1, agora 0
    private Integer previsaoHoras;

    private ItemModel.NivelAtividade nivelAtividade;

    private UUID usuarioId;

    @NotNull(message = "O projeto é obrigatório.")
    private Long projetoId;
}