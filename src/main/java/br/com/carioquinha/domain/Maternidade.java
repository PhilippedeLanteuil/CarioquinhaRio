package br.com.carioquinha.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "maternidade")
public class Maternidade extends PanacheEntity {

    @Column(nullable = false, length = 150)
    public String nome;
}