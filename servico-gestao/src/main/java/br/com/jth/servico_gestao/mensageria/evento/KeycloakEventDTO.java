package br.com.jth.servico_gestao.mensageria.evento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakEventDTO {

    private String eventType;
    private String id;
    private String nomeUsuario;
    private String email;
    private Boolean ativo;
    private String nivelExperiencia;
    private String valorHora;
    private List<String> cargo;

    public boolean temDadosDeAtributo() {
        return nomeUsuario != null || email != null || ativo != null
                || nivelExperiencia != null || valorHora != null;
    }

    public boolean temDadosDeCargo() {
        return cargo != null && !cargo.isEmpty();
    }

    public boolean eEventoVazio() {
        return !temDadosDeAtributo() && !temDadosDeCargo();
    }
}