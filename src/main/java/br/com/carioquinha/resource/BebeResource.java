package br.com.carioquinha.resource;

import br.com.carioquinha.domain.Bebe;
import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeResponse;
import br.com.carioquinha.service.BebeService;
import io.quarkus.panache.common.Parameters;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("/bebes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BebeResource {

    @Inject
    BebeService bebeService;

    // Header para identificar operador (requisito pede "operador"; aqui a gente pega do header)
    private static final String HEADER_OPERADOR = "X-Operador";

    @POST
    public Response criar(@HeaderParam(HEADER_OPERADOR) String operador, BebeCreateRequest req) {
        BebeResponse criado = bebeService.criar(req, operador);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @GET
    public List<BebeResponse> consultar(
            @QueryParam("nome") String nome,
            @QueryParam("dataNascimento") String dataNascimento,
            @QueryParam("maternidadeId") Long maternidadeId
    ) {
        // Filtros: parte do nome OU data OU maternidade 
        StringBuilder jpql = new StringBuilder("1=1");
        Parameters params = new Parameters();

        if (nome != null && !nome.isBlank()) {
            jpql.append(" and lower(nome) like :nome");
            params.and("nome", "%" + nome.toLowerCase().trim() + "%");
        }

        if (dataNascimento != null && !dataNascimento.isBlank()) {
            LocalDate dt;
            try {
                dt = LocalDate.parse(dataNascimento.trim());
            } catch (Exception e) {
                throw new WebApplicationException("dataNascimento inválida. Use YYYY-MM-DD", 400);
            }
            jpql.append(" and dataNascimento = :dt");
            params.and("dt", dt);
        }

        if (maternidadeId != null) {
            jpql.append(" and maternidade.id = :mid");
            params.and("mid", maternidadeId);
        }

        // Ordenação A-Z pelo nome 
        List<Bebe> lista = Bebe.find(jpql.toString(), params)
                .list();

        // Panache não ordena automaticamente aqui; então fazemos ordenação por query:
        // Alternativa simples: refazer a query com "order by nome"
        lista = Bebe.find(jpql + " order by nome asc", params).list();

        if (lista.isEmpty()) {
            // Requisito: 404 quando a consulta não retornar registros 
            throw new WebApplicationException(Response.status(404).build());
        }

        List<BebeResponse> resp = new ArrayList<>();
        for (Bebe b : lista) {
            // força acesso ao nome da maternidade
            b.maternidade.nome.toString();
            resp.add(BebeService.toResponse(b));
        }
        return resp;
    }
}