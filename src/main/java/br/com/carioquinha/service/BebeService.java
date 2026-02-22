package br.com.carioquinha.service;

import br.com.carioquinha.domain.Bebe;
import br.com.carioquinha.domain.LogAuditoria;
import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;

@ApplicationScoped
public class BebeService {

    @Transactional
    public BebeResponse criar(BebeCreateRequest req, String operador) {
        validarObrigatorios(req);

        Maternidade maternidade = Maternidade.findById(req.maternidadeId);
        if (maternidade == null) {
            throw new WebApplicationException("Maternidade não encontrada", 404);
        }

        Bebe bebe = new Bebe();
        bebe.nome = req.nome.trim();
        bebe.dataNascimento = req.dataNascimento;
        bebe.nomeMae = req.nomeMae.trim();
        bebe.nomePai = (req.nomePai == null || req.nomePai.isBlank()) ? null : req.nomePai.trim();
        bebe.maternidade = maternidade;
        bebe.mensagemResponsavel = req.mensagemResponsavel.trim();
        bebe.fotoBase64 = (req.fotoBase64 == null || req.fotoBase64.isBlank()) ? null : req.fotoBase64;
        bebe.fotoMime = (req.fotoMime == null || req.fotoMime.isBlank()) ? null : req.fotoMime;

        bebe.persist();

        // LOG AUTOMÁTICO (mesma transação) 
        LogAuditoria log = new LogAuditoria();
        log.idBebe = bebe.id;
        log.dataHora = LocalDateTime.now();
        log.operador = (operador == null || operador.isBlank()) ? "web" : operador.trim();
        log.persist();

        return toResponse(bebe);
    }

    private void validarObrigatorios(BebeCreateRequest req) {
        // Obrigatórios: Nome do bebê, Data, Mãe, Maternidade, Mensagem 
        if (req == null
                || req.nome == null || req.nome.isBlank()
                || req.dataNascimento == null
                || req.nomeMae == null || req.nomeMae.isBlank()
                || req.maternidadeId == null
                || req.mensagemResponsavel == null || req.mensagemResponsavel.isBlank()) {
            throw new WebApplicationException("Campos obrigatórios ausentes", 400);
        }
    }

    public static BebeResponse toResponse(Bebe b) {
        BebeResponse r = new BebeResponse();
        r.id = b.id;
        r.nome = b.nome;
        r.dataNascimento = b.dataNascimento;
        r.nomeMae = b.nomeMae;
        r.maternidadeNome = (b.maternidade != null) ? b.maternidade.nome : null;
        r.mensagemResponsavel = b.mensagemResponsavel;
        r.fotoBase64 = b.fotoBase64;
        r.fotoMime = b.fotoMime;
        return r;
    }
}