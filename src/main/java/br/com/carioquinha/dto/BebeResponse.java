package br.com.carioquinha.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BebeResponse {
    public Long id;
    public String nome;
    public LocalDateTime dataNascimento;

    public String nomeMae;
    public String nomePai;

    public Long maternidadeId;
    public String maternidadeNome;

    public String mensagemResponsavel;

    public String fotoBase64;
    public String fotoMime;
}