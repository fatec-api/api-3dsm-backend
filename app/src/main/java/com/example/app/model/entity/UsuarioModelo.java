package com.example.app.model.entity;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioModelo {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable=false)
    private String nomeUsuario;


    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String senha;

    @Column(nullable=false)
    private BigDecimal valorHora;

    private enum NivelExperiencia {
        Júnior,
        Pleno,
        Sênior
    }
    
    private enum Cargo {
        Profissional,
        Gestor,
        Administrativo
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable=true)
    private NivelExperiencia nivelExperiencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Cargo cargo;

    @Column(nullable=false)
    private boolean ativo = true;

    @Column(nullable=false)
    private Timestamp criado_em;
}
