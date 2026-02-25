package br.com.carioquinha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class BebeCreateRequest {
    public String nome;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime dataNascimento;
    public String nomeMae;
    public String nomePai; // opcional
    public Long maternidadeId;
    public String mensagemResponsavel;

    // opcional
    public String fotoBase64;
    public String fotoMime;
}