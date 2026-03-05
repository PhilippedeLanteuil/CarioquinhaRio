package br.com.carioquinha.error;

import br.com.carioquinha.dto.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof WebApplicationException wae) {
            int status = wae.getResponse() != null ? wae.getResponse().getStatus() : 500;

            // Para 404 (consulta sem resultados), você pode retornar só o status (sem body)
            if (status == 404 && (wae.getMessage() == null || wae.getMessage().isBlank())) {
                return Response.status(404).build();
            }

            String msg = (wae.getMessage() == null || wae.getMessage().isBlank())
                    ? "Erro"
                    : wae.getMessage();

            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ErrorResponse(msg))
                    .build();
        }

        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(ex)
                .build();
    }
}