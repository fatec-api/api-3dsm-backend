package com.example.app.model.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projetoUsuario")
public class ProjetoUsuarioModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoModelo projeto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModelo usuario;

    @Column(nullable = false)
    private LocalDate dataVinculo;

    private LocalDate dataDesvinculo;
}
