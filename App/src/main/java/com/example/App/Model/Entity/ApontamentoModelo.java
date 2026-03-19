package com.example.App.Model.Entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apontamentos_horas")
public class ApontamentoModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private ItemModelo item;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModelo usuario;

    @Column(nullable = false)
    private LocalDateTime dataApontamento;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFim;

    private LocalTime pausaInicio;
    private LocalTime pausaFim;

    @Column(length = 300)
    private String observacao;

    @Column(nullable = false)
    private Double horasLiquidas;
}