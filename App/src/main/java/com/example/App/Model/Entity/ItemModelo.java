package com.example.App.Model.Entity;

import java.sql.Time;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.example.App.Model.Entity.UsuarioModelo;


@Data
@Entity
@Table(name = "item")
public class ItemModelo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=10)
    private String código;

    @Column(nullable=false, length=300)
    private String descricao;

    @Column(nullable=false)
    private LocalDate dataAtribuicao;

    @Column(nullable=false)
    private Time previsaoHoras;

    private enum NivelAtividade {
        Analise,
        Desenvolvimento,
        teste
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=true)
    private NivelAtividade nivelAtividade;
    
    @ManyToOne
    @JoinColumn(name= "id_usuario")
    private UsuarioModelo usuarioModelo;



}
