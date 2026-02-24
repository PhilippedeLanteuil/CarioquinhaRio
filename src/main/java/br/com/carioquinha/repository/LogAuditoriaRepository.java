package br.com.carioquinha.repository;

import br.com.carioquinha.domain.LogAuditoria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogAuditoriaRepository implements PanacheRepository<LogAuditoria> { }