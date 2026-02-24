package br.com.carioquinha.repository;

import br.com.carioquinha.domain.Bebe;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BebeRepository implements PanacheRepository<Bebe> { }