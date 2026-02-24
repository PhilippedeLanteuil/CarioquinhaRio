package br.com.carioquinha.service;

import br.com.carioquinha.dto.MaternidadeResponse;
import br.com.carioquinha.repository.MaternidadeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MaternidadeService {

    @Inject MaternidadeRepository maternidadeRepository;

    public List<MaternidadeResponse> listar() {
        return maternidadeRepository.listAll().stream()
                .map(m -> new MaternidadeResponse(m.id, m.nome))
                .toList();
    }
}