package br.com.carioquinha.dto;

import jakarta.ws.rs.QueryParam;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class BebeGetRequest {

    @QueryParam("nome")
    private String nome;

    @QueryParam("dataNascimento")
    private String dataNascimento;

    @QueryParam("maternidadeId")
    private Long maternidadeId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Long getMaternidadeId() {
        return maternidadeId;
    }

    public void setMaternidadeId(Long maternidadeId) {
        this.maternidadeId = maternidadeId;
    }
}