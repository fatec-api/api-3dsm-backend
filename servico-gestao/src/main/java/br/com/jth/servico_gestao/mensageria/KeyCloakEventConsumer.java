package br.com.jth.servico_gestao.mensageria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.mensageria.evento.KeycloakEventDTO;
import br.com.jth.servico_gestao.model.ItemModel;
import br.com.jth.servico_gestao.model.ProjetoModel;
import br.com.jth.servico_gestao.model.ProjetoUsuarioModel;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.ItemRepository;
import br.com.jth.servico_gestao.repository.ProjetoRepository;
import br.com.jth.servico_gestao.repository.ProjetoUsuarioRepository;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyCloakEventConsumer {

    private final UsuarioRepository usuarioRepository;
    private final ProjetoUsuarioRepository projetoUsuarioRepository;
    private final ItemRepository itemRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_CRIADO_QUEUE)
    public void consumirUsuarioCriado(KeycloakEventDTO evento) {
        log.info("[KEYCLOAK] usuario.criado recebido — id={}, nome={}, email={}, cargo={}",
                evento.getId(), evento.getNomeUsuario(), evento.getEmail(), evento.getCargo());

        if (evento.getId() == null) {
            log.warn("[KEYCLOAK] Evento sem ID, ignorado.");
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(evento.getId());
        } catch (IllegalArgumentException e) {
            log.warn("[KEYCLOAK] ID inválido: '{}', ignorado.", evento.getId());
            return;
        }

        if (usuarioRepository.existsById(id)) {
            log.info("[KEYCLOAK] Usuário {} já existe, ignorando criação.", id);
            return;
        }

        UsuarioModel model = new UsuarioModel();
        model.setId(id);
        model.setNomeUsuario(evento.getNomeUsuario());
        model.setEmail(evento.getEmail());
        model.setAtivo(evento.getAtivo() != null ? evento.getAtivo() : true);
        model.setNivelExperiencia(evento.getNivelExperiencia());

        if (evento.getValorHora() != null) {
            model.setValorHora(new BigDecimal(evento.getValorHora()));
        }

        if (evento.temDadosDeCargo()) {
            model.setCargos(parseCargos(evento.getCargo()));
        }

        usuarioRepository.save(model);
        log.info("[KEYCLOAK] Usuário {} salvo com sucesso.", id);
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_ATUALIZADO_QUEUE)
    public void consumirUsuarioAtualizado(KeycloakEventDTO evento) {
        log.info("[KEYCLOAK] usuario.atualizado recebido — id={}, nome={}, email={}, cargo={}",
                evento.getId(), evento.getNomeUsuario(), evento.getEmail(), evento.getCargo());

        if (evento.getEventType() == null) {
            log.debug("[KEYCLOAK] Sem eventType, ignorado.");
            return;
        }

        if (evento.getId() == null) {
            log.warn("[KEYCLOAK] Evento de atualização sem ID, ignorado.");
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(evento.getId());
        } catch (IllegalArgumentException e) {
            log.warn("[KEYCLOAK] ID inválido: '{}', ignorado.", evento.getId());
            return;
        }

        Optional<UsuarioModel> optional = usuarioRepository.findById(id);

        if (optional.isEmpty()) {
            log.warn("[KEYCLOAK] Usuário {} não encontrado para atualização. " +
                    "O evento de criação pode não ter sido processado ainda.", id);
            return;
        }

        UsuarioModel model = optional.get();

        if (evento.temDadosDeAtributo() && !evento.temDadosDeCargo()) {
            atualizarAtributos(model, evento);
            usuarioRepository.save(model);
            log.info("[KEYCLOAK] Atributos atualizados. ID: {}", id);
            return;
        }

        if (evento.temDadosDeCargo()) {
            Set<Cargo> cargosDoPayload = parseCargos(evento.getCargo());
            Set<Cargo> cargosAtuais = model.getCargos();

            for (Cargo cargoPayload : cargosDoPayload) {
                    if (cargosAtuais.contains(cargoPayload)) {
                        cargosAtuais.remove(cargoPayload);
                        log.info("[KEYCLOAK] Cargo removido de {}: {}", id, cargoPayload);
                    } else {
                        cargosAtuais.add(cargoPayload);
                        log.info("[KEYCLOAK] Cargo adicionado em {}: {}", id, cargoPayload);
                    }
                }

            usuarioRepository.save(model);
        }
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_DELETADO_QUEUE)
    public void consumirUsuarioDeletado(KeycloakEventDTO evento) {
        log.info("[KEYCLOAK] usuario.deletado recebido — id={}", evento.getId());

        if (evento.getEventType() == null) {
            log.debug("[KEYCLOAK] Sem eventType, ignorado.");
            return;
        }

        if (evento.getId() == null) {
            log.warn("[KEYCLOAK] Evento de deleção sem ID, ignorado.");
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(evento.getId());
        } catch (IllegalArgumentException e) {
            log.warn("[KEYCLOAK] ID inválido: '{}', ignorado.", evento.getId());
            return;
        }

        if (!usuarioRepository.existsById(id)) {
            log.warn("[KEYCLOAK] Usuário {} não encontrado para deletar, ignorado.", id);
            return;
        }

        // Reatribui gestor e profissionalAlocado para o usuário default
        UUID defaultId = UUID.fromString("94567772-30ea-457f-8ecd-683fe442e768");
        UsuarioModel usuarioDefault = usuarioRepository.findById(defaultId).orElse(null);

        List<ProjetoModel> projetosComoGestor = projetoRepository.findByGestorId(id);
        for (ProjetoModel projeto : projetosComoGestor) {
            projeto.setGestor(usuarioDefault);
        }
        projetoRepository.saveAll(projetosComoGestor);
        log.info("[KEYCLOAK] Gestor de {} projeto(s) reatribuído para usuário default ({}).", projetosComoGestor.size(), defaultId);

        List<ProjetoModel> projetosComoProfissional = projetoRepository.findByProfissionalAlocadoId(id);
        for (ProjetoModel projeto : projetosComoProfissional) {
            projeto.setProfissionalAlocado(usuarioDefault);
        }
        projetoRepository.saveAll(projetosComoProfissional);
        log.info("[KEYCLOAK] Profissional alocado de {} projeto(s) reatribuído para usuário default ({}).", projetosComoProfissional.size(), defaultId);

        // Remove vínculos em projeto_usuario
        List<ProjetoUsuarioModel> vinculos = projetoUsuarioRepository.findByUsuarioIdAndDataDesvinculoIsNull(id);
        projetoUsuarioRepository.deleteAll(vinculos);
        log.info("[KEYCLOAK] {} vínculo(s) de projeto removidos para usuário {}.", vinculos.size(), id);

        // Remove usuário da lista de responsáveis em item_usuario
        List<ItemModel> itens = itemRepository.findByUsuariosId(id);
        for (ItemModel item : itens) {
            item.getUsuarios().removeIf(u -> u.getId().equals(id));
        }
        itemRepository.saveAll(itens);
        log.info("[KEYCLOAK] Usuário {} removido de {} item(ns).", id, itens.size());

        // Deleta o usuário (usuario_cargos é removido em cascata pelo @ElementCollection)
        usuarioRepository.deleteById(id);
        log.info("[KEYCLOAK] Usuário {} deletado do banco.", id);
    }

    private void atualizarAtributos(UsuarioModel model, KeycloakEventDTO evento) {
        if (evento.getNomeUsuario() != null)      model.setNomeUsuario(evento.getNomeUsuario());
        if (evento.getEmail() != null)            model.setEmail(evento.getEmail());
        if (evento.getAtivo() != null)            model.setAtivo(evento.getAtivo());
        if (evento.getNivelExperiencia() != null) model.setNivelExperiencia(evento.getNivelExperiencia());
        if (evento.getValorHora() != null)        model.setValorHora(parseBigDecimal(evento.getValorHora(), model.getValorHora()));
    }

    private Set<Cargo> parseCargos(List<String> cargosStr) {
        return cargosStr.stream()
                .map(Cargo::fromString)
                .collect(Collectors.toSet());
    }

    private BigDecimal parseBigDecimal(String value, BigDecimal fallback) {
        if (value == null || value.isBlank()) return fallback;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            log.warn("[KEYCLOAK] valorHora inválido: '{}'. Usando fallback: {}", value, fallback);
            return fallback;
        }
    }
}