package com.example.app.model.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "apontamentos_horas")
public class ApontamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private LocalDateTime dataApontamento;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Column
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFim;

    @Column
    private LocalDateTime pausaInicio;

    @Column
    private LocalDateTime pausaFim;

    @Column(length = 300)
    private String observacao;

    @Column(nullable = false)
    private Double horasLiquidas;
}
