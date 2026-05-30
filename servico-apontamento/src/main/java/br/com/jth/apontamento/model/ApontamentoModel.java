package br.com.jth.apontamento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.jth.apontamento.enums.ApontamentoStatus;
import br.com.jth.apontamento.enums.NivelAtividade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    
    @Column(nullable = false)
    private BigDecimal valorHoraAplicado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApontamentoStatus status = ApontamentoStatus.PENDENTE;

    @Column(length = 500)
    private String justificativaReprovacao;

    @Column
    private String itemDescricao;

    @Column
    private Long projetoId;

    @Column
    private String projetoNome;

    @Enumerated(EnumType.STRING)
    @Column
    private NivelAtividade nivelAtividade;

    @Column
    private String usuarioNome;

    @Column
    private UUID gestorId;

}
