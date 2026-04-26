package br.com.jth.servico_gestao.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.jth.servico_gestao.enums.projeto.StatusProjeto;
import br.com.jth.servico_gestao.enums.projeto.TipoProjeto;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private BigInteger horasRealizadasTotal; // Soma de horasLiquidas de todos os apontamentos Aprovados - ainda não

    @Column
    private BigInteger horasPendentesTotal; // Soma de horasLiquidas de todos os apontamentos Pendentes(que ainda não foram aprovados/reprovados). - ainda não

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

    @ManyToOne
    @JoinColumn(name = "id_profissional_alocado", nullable = true)
    private UsuarioModel profissionalAlocado;

    @OneToMany(mappedBy = "projetoModel", fetch = FetchType.LAZY)
    private List<ItemModel> itens = new ArrayList<>();

    @Transient // campo transient não persiste na coluna, mas é serializado no objeto
    private BigInteger horasPrevistasTotal;

    @Transient
    private Double progressoProjeto;

    @PrePersist
    public void prePersist() {
        this.criadoEm = new Timestamp(System.currentTimeMillis());
    }


}
