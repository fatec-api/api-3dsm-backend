package com.example.app.model.entity;

import java.sql.Time;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item")
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false, length = 300)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataAtribuicao;


    @Column(nullable = true)
    private Integer previsaoHoras;

    public enum NivelAtividade {
        ANALISE,
        DESENVOLVIMENTO,
        TESTE
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private NivelAtividade nivelAtividade;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private UsuarioModel usuarioModel;

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoModel projetoModel;
}