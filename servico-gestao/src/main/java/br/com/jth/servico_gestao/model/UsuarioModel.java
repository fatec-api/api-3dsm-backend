package br.com.jth.servico_gestao.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import br.com.jth.servico_gestao.enums.usuario.Cargo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nomeUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private BigDecimal valorHora;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuario_cargos",
            joinColumns = @JoinColumn(name = "usuario_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private Set<Cargo> cargos = new HashSet<>();

    @Column(nullable = true)
    private String nivelExperiencia;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp criado_em;
}