package br.com.jth.servico_gestao.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.jth.servico_gestao.enums.item.NivelAtividade;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private NivelAtividade nivelAtividade;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_usuario",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<UsuarioModel> usuarios = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private ProjetoModel projetoModel;
}