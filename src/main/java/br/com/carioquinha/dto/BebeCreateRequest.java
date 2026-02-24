package br.com.carioquinha.dto;

import java.time.LocalDate;

public class BebeCreateRequest {
    public String nome;
    public LocalDate dataNascimento;
    public String nomeMae;
    public String nomePai; // opcional
    public Long maternidadeId;
    public String mensagemResponsavel;

    // opcional
    public String fotoBase64;
    public String fotoMime;
}