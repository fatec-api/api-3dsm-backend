package com.example.App.Model.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projetos")
public class ProjetoModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeProjeto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProjeto tipoProjeto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorOrcamento;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteModelo cliente;

    @ManyToOne
    @JoinColumn(name = "id_gestor")
    private UsuarioModelo gestor;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private Timestamp criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = new Timestamp(System.currentTimeMillis());
    }

    public enum StatusProjeto {
        Andamento,
        Desenvolvimento,
        Concluida
    }

    public enum TipoProjeto {
        Alocacao,
        Hora_Fechada
    }
}