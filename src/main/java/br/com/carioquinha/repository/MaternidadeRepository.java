package br.com.carioquinha.repository;

import br.com.carioquinha.domain.Maternidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaternidadeRepository implements PanacheRepository<Maternidade> { }