package br.com.jth.apontamento.model;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "apontamentos_horas")
public class ApontamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_item", nullable = false)
    private Long itemId;

    @Column(name = "id_usuario", nullable = false)
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

    @Column(length = 300)
    private String observacao;

    @Column(nullable = false)
    private Double horasLiquidas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApontamentoStatus status = ApontamentoStatus.PENDENTE;

    @Column(length = 500)
    private String justificativaReprovacao;

}
