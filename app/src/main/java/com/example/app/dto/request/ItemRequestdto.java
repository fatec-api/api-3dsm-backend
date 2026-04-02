package com.example.app.dto.request;

import com.example.app.model.entity.ItemModel;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ItemRequestdto {

    @NotBlank(message = "Campo de código do item vazio.")
    @Pattern(
            regexp = "^[A-Za-z]{3}\\d{4}$",
            message = "Código fora do padrão, deve conter 3 letras acompanhado de 4 números."
    )
    private String codigo;

    @NotBlank(message = "O campo de descrição não pode estar vazio.")
    @Size(max = 300, message = "Descrição deve ter no máximo 300 caracteres.")
    private String descricao;

    private LocalDate dataAtribuicao;

    @Min(value = 1, message = "A previsão de horas deve ser de pelo menos 1 hora.")
    private Integer previsaoHoras;

    private ItemModel.NivelAtividade nivelAtividade;

    private UUID usuarioId;

    @NotNull(message = "O projeto é obrigatório.")
    private Long projetoId;
}