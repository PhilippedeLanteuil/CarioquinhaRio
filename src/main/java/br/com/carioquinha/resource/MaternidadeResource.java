package br.com.carioquinha.resource;

import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.dto.MaternidadeResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/maternidades")
@Produces(MediaType.APPLICATION_JSON)
public class MaternidadeResource {

    @GET
    public List<MaternidadeResponse> listar() {
        return Maternidade.<Maternidade>listAll().stream()
                .map(m -> new MaternidadeResponse(m.id, m.nome))
                .toList();
    }
}