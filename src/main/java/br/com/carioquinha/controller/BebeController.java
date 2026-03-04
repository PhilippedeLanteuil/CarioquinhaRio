package br.com.carioquinha.controller;

import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeGetRequest;
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
public class BebeController {

    @Inject BebeService bebeService;

    private static final String HEADER_OPERADOR = "X-Operador";

    @POST
    public Response cadastroNascimento(
            BebeCreateRequest req,
            @HeaderParam(HEADER_OPERADOR) String operador
    ) {
        BebeResponse bebeEntity = bebeService.criar(req, operador);
        return Response.status(Response.Status.CREATED).entity(bebeEntity).build();
    }

    @GET
    public Response listarBebes(@BeanParam BebeGetRequest req){
        List<BebeResponse> listaBebes = bebeService.listarTodosBebes(req.getNome());
        return Response.status(Response.Status.OK).entity(listaBebes).build();

    }

}