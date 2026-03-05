package br.com.carioquinha.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.com.carioquinha.domain.Bebe;
import br.com.carioquinha.domain.LogAuditoria;
import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeGetRequest;
import br.com.carioquinha.dto.BebeResponse;
import br.com.carioquinha.repository.BebeRepository;
import br.com.carioquinha.repository.LogAuditoriaRepository;
import br.com.carioquinha.repository.MaternidadeRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;


@ApplicationScoped
public class BebeService {

    @Inject BebeRepository bebeRepository;
    @Inject MaternidadeRepository maternidadeRepository;
    @Inject LogAuditoriaRepository logAuditoriaRepository;

    @Transactional
    public BebeResponse criar(BebeCreateRequest req, String operador) {
        validarObrigatorios(req);

        Maternidade maternidade = maternidadeRepository.findById(req.maternidadeId);
        if (maternidade == null) {
            throw new WebApplicationException("Maternidade não encontrada", 404);
        }
        Bebe bebe = criarEntidadeBebe(req, maternidade);
        bebeRepository.persist(bebe);

        enviarlogAuditoria(bebe, operador);

        return toResponse(bebe);
    }
    @Transactional
    public List<BebeResponse> listarTodosBebes(String nome, String dataNascimento, Long maternidadeId) {

        String query = "1=1";
        Parameters params = new Parameters();

        if (nome != null && !nome.isBlank()) {
            query += " and nome like :nome";
            params.and("nome", "%" + nome + "%");
        }

        if (dataNascimento != null) {
            LocalDate parsed = LocalDate.parse(dataNascimento);

            query += " and dataNascimento = :dataNascimento";
            params.and("dataNascimento", parsed);
        }

        if (maternidadeId != null) {
            query += " and maternidade.id = :maternidadeId";
            params.and("maternidadeId", maternidadeId);
        }

        List<Bebe> bebes = Bebe.find(query, params).list();

        return bebes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    public void enviarlogAuditoria(Bebe bebe, String operador){
        try {
            LogAuditoria log = new LogAuditoria();
            log.idBebe = bebe.id;
            log.idOperador = operador;
            log.dataHora = LocalDateTime.now();
            log.acaoRealizada = "Criacao Bebe";
            logAuditoriaRepository.persist(log);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private void validarObrigatorios(BebeCreateRequest req) {
        if (req == null
                || req.nome == null || req.nome.isBlank()
                || req.dataNascimento == null
                || req.nomeMae == null || req.nomeMae.isBlank()
                || req.maternidadeId == null
                || req.mensagemResponsavel == null || req.mensagemResponsavel.isBlank()) {
            throw new WebApplicationException("Campos obrigatórios ausentes", 400);
        }
    }

    public BebeResponse toResponse(Bebe b) {
        BebeResponse r = new BebeResponse();
        r.id = b.id;
        r.nome = b.nome;
        r.dataNascimento = b.dataNascimento;
        r.nomeMae = b.nomeMae;
        r.nomePai = b.nomePai;
        r.mensagemResponsavel = b.mensagemResponsavel;
        r.fotoBase64 = b.fotoBase64;
        r.fotoMime = b.fotoMime;

        if (b.maternidade != null) {
            r.maternidadeId = b.maternidade.id;
            r.maternidadeNome = b.maternidade.nome;
        }
        return r;
    }
    public Bebe criarEntidadeBebe(BebeCreateRequest req, Maternidade maternidade){
        Bebe bebe = new Bebe();
        bebe.nome = req.nome.trim();
        bebe.dataNascimento = req.dataNascimento;
        bebe.nomeMae = req.nomeMae.trim();
        bebe.nomePai = (req.nomePai == null || req.nomePai.isBlank()) ? null : req.nomePai.trim();
        bebe.maternidade = maternidade;
        bebe.mensagemResponsavel = req.mensagemResponsavel.trim();
        bebe.fotoBase64 = (req.fotoBase64 == null || req.fotoBase64.isBlank()) ? null : req.fotoBase64;
        bebe.fotoMime = (req.fotoMime == null || req.fotoMime.isBlank()) ? null : req.fotoMime;
        return bebe;
    }
}