package br.com.carioquinha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDateTime;

public class BebeGetRequest {
    @QueryParam("nome")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}