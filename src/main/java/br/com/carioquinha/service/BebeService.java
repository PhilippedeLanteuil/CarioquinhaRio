package br.com.carioquinha.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.carioquinha.domain.Bebe;
import br.com.carioquinha.domain.LogAuditoria;
import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.dto.BebeCreateRequest;
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

        Bebe bebe = new Bebe();
        bebe.nome = req.nome.trim();
        bebe.dataNascimento = req.dataNascimento;
        bebe.nomeMae = req.nomeMae.trim();
        bebe.nomePai = (req.nomePai == null || req.nomePai.isBlank()) ? null : req.nomePai.trim();
        bebe.maternidade = maternidade;
        bebe.mensagemResponsavel = req.mensagemResponsavel.trim();
        bebe.fotoBase64 = (req.fotoBase64 == null || req.fotoBase64.isBlank()) ? null : req.fotoBase64;
        bebe.fotoMime = (req.fotoMime == null || req.fotoMime.isBlank()) ? null : req.fotoMime;

        bebeRepository.persist(bebe);

        LogAuditoria log = new LogAuditoria();
        log.idBebe = bebe.id;
        log.dataHora = LocalDateTime.now();
        log.operador = (operador == null || operador.isBlank()) ? "web" : operador.trim();
        logAuditoriaRepository.persist(log);

        return toResponse(bebe);
    }

    public List<BebeResponse> consultar(String nome, String dataNascimento, Long maternidadeId) {
        StringBuilder where = new StringBuilder("1=1");
        Parameters params = new Parameters();

        if (nome != null && !nome.isBlank()) {
            where.append(" and lower(nome) like :nome");
            params.and("nome", "%" + nome.toLowerCase().trim() + "%");
        }

        if (dataNascimento != null && !dataNascimento.isBlank()) {
            LocalDate dt;
            try {
                dt = LocalDate.parse(dataNascimento.trim());
            } catch (Exception e) {
                throw new WebApplicationException("dataNascimento inválida. Use YYYY-MM-DD", 400);
            }
            where.append(" and dataNascimento = :dt");
            params.and("dt", dt);
        }

        if (maternidadeId != null) {
            where.append(" and maternidade.id = :mid");
            params.and("mid", maternidadeId);
        }

        // Ordenação A-Z por nome
        List<Bebe> lista = bebeRepository.find(where + " order by nome asc", params).list();

        if (lista.isEmpty()) {
            // regra do teste: 404 quando não encontrar
            throw new WebApplicationException(404);
        }

        return lista.stream().map(BebeService::toResponse).toList();
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

    public static BebeResponse toResponse(Bebe b) {
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
}