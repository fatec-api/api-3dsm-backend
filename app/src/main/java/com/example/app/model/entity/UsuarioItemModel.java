package com.example.app.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario_item")
public class UsuarioItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private ItemModel item;

    @Column(nullable = false)
    private LocalDate dataVinculo;

    @Column(nullable = true)
    private LocalDate dataDesvinculo;

    @PrePersist
    public void prePersist() {
        this.dataVinculo = LocalDate.now();
    }
}