package com.example.app.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ItemRequestdto {

    @NotBlank(message = "Campo de código do item vazio.")
    private String código;

    @NotBlank(message = "O campo de descrição não pode estar vazio.")
    private String descricao;

    @NotNull(message = "Data é obrigatória.")
    private LocalDate dataAtribuicao;

    @NotNull(message = "A previsão de horas é obrigatória.")
    @DecimalMin(value = "0.01", message = "A previsão de horas deve ser maior que zero.")
    private Time previsaoHoras;

    @NotNull
    private String nivelAtividade;

    @NotNull
    private UUID usuarioId;

    @NotNull
    private Long projetoId;
}