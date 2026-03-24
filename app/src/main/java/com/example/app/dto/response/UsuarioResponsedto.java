package com.example.app.dto.response;

import com.example.app.model.entity.UsuarioModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UsuarioResponsedto {
    private String nomeUsuario;
    private String email;
    private String senha;
    private BigDecimal valorHora;
    private UsuarioModel.Cargo cargo;
    private boolean ativo;
    private Timestamp criado_em;

}
