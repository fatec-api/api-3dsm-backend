package com.example.App.Model.Entity;

import java.math.BigDecimal;
import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "profissionais")
public class ProfissionalModelo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nomeProfissional;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false, unique=true)
    private String senha;

    @Column(nullable=false)
    private BigDecimal valorHora;

    private enum nivelExperiencia {
            Júnior,
            Pleno,
            Sênior
        }

    private enum cargo {
        Profissional,
        Gestor,
        Administrativo
    }

    @Column(nullable=false)
    private boolean ativo = true;

    @Column(nullable=false)
    private Timestamp criado_em;
}
