package com.example.app.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projetos")
public class ProjetoModel {

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
    @JoinColumn(name = "id_cliente", nullable = true)
    private ClienteModel cliente;

    @ManyToOne
    @JoinColumn(name = "id_gestor")
    private UsuarioModel gestor;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private Timestamp criadoEm;

  
    @ManyToMany
    @JoinTable(
        name = "projeto_profissionais", 
        joinColumns = @JoinColumn(name = "id_projeto"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<UsuarioModel> equipe = new ArrayList<>(); 
    

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