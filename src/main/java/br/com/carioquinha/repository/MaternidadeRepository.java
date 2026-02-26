package br.com.carioquinha.repository;

import br.com.carioquinha.domain.Maternidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MaternidadeRepository implements PanacheRepository<Maternidade> {
    public List<Maternidade> findAllMaternidades() {
        return listAll();
    }
}

