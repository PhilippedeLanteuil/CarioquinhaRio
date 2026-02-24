package br.com.carioquinha.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bebe")
public class Bebe extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 150)
    public String nome;

    @Column(name = "data_nascimento", nullable = false)
    public LocalDate dataNascimento;

    @Column(name = "nome_mae", nullable = false, length = 150)
    public String nomeMae;

    @Column(name = "nome_pai", length = 150)
    public String nomePai;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "maternidade_id", nullable = false)
    public Maternidade maternidade;

    @Column(name = "mensagem_responsavel", nullable = false, length = 500)
    public String mensagemResponsavel;

    @Lob
    @Column(name = "foto_base64", columnDefinition = "LONGTEXT")
    public String fotoBase64;

    @Column(name = "foto_mime", length = 50)
    public String fotoMime;
}