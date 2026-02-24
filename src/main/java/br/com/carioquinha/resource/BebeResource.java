package br.com.carioquinha.resource;

import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeResponse;
import br.com.carioquinha.service.BebeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/bebes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BebeResource {

    @Inject BebeService bebeService;

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
        return bebeService.consultar(nome, dataNascimento, maternidadeId);
    }
}