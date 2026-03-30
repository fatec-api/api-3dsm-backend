package com.example.app.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private BigDecimal valorHora;

    public enum Cargo {
        Profissional,
        Gestor,
        Administrativo
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp criado_em;
}