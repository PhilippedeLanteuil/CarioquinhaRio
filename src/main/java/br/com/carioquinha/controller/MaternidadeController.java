package br.com.carioquinha.controller;
import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.repository.MaternidadeRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/maternidades")
@Produces(MediaType.APPLICATION_JSON)
public class MaternidadeController {

    @Inject
    MaternidadeRepository maternidadeRepository;

    @GET
    public List<Maternidade> listarTodas() {
        return maternidadeRepository.findAllMaternidades();
    }
}