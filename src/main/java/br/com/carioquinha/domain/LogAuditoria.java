package br.com.carioquinha.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoria extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "id_bebe", nullable = false)
    public Long idBebe;

    @Column(name = "data_hora", nullable = false)
    public LocalDateTime dataHora;

    @Column(nullable = false, length = 100)
    public String operador;
}