package br.com.carioquinha.dto;

import java.time.LocalDate;

public class BebeResponse {
    public Long id;
    public String nome;
    public LocalDate dataNascimento;
    public String nomeMae;
    public String maternidadeNome;
    public String mensagemResponsavel;
    public String fotoBase64;
    public String fotoMime;
}