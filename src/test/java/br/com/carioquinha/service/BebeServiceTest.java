package br.com.carioquinha.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carioquinha.domain.Bebe;
import br.com.carioquinha.domain.LogAuditoria;
import br.com.carioquinha.domain.Maternidade;
import br.com.carioquinha.dto.BebeCreateRequest;
import br.com.carioquinha.dto.BebeResponse;
import br.com.carioquinha.repository.BebeRepository;
import br.com.carioquinha.repository.LogAuditoriaRepository;
import br.com.carioquinha.repository.MaternidadeRepository;
import jakarta.ws.rs.WebApplicationException;

@ExtendWith(MockitoExtension.class)
class BebeServiceTest {

    @Mock BebeRepository bebeRepository;
    @Mock MaternidadeRepository maternidadeRepository;
    @Mock LogAuditoriaRepository logAuditoriaRepository;

    @InjectMocks BebeService service;

    private BebeCreateRequest reqValida;
    private Maternidade maternidade;

    @BeforeEach
    void setup() {
        maternidade = new Maternidade();
        maternidade.id = 10L;
        maternidade.nome = "Maternidade X";

        reqValida = new BebeCreateRequest();
        reqValida.nome = "  João  ";
        reqValida.dataNascimento = LocalDateTime.of(2024, 1, 2, 10, 5);
        reqValida.nomeMae = "  Maria  ";
        reqValida.nomePai = "  José  ";
        reqValida.maternidadeId = 10L;
        reqValida.mensagemResponsavel = "  Bem-vindo!  ";
        reqValida.fotoBase64 = "base64";
        reqValida.fotoMime = "image/png";
    }

    // ---------- criar(...) / validarObrigatorios ----------

    @Test
    void criar_deveLancar400_quandoReqEhNula() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> service.criar(null, "op1"));
        assertEquals(400, ex.getResponse().getStatus());
        verifyNoInteractions(maternidadeRepository, bebeRepository, logAuditoriaRepository);
    }

    @Test
    void criar_deveLancar400_quandoCamposObrigatoriosAusentes() {
        BebeCreateRequest req = new BebeCreateRequest();
        req.nome = " "; // blank
        req.dataNascimento = LocalDateTime.of(2024, 1, 1, 14, 12);
        req.nomeMae = "Mae";
        req.maternidadeId = 1L;
        req.mensagemResponsavel = "msg";

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> service.criar(req, "op1"));
        assertEquals(400, ex.getResponse().getStatus());
        verifyNoInteractions(maternidadeRepository, bebeRepository, logAuditoriaRepository);
    }

    @Test
    void criar_deveLancar404_quandoMaternidadeNaoEncontrada() {
        when(maternidadeRepository.findById(reqValida.maternidadeId)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> service.criar(reqValida, "op1"));
        assertEquals(404, ex.getResponse().getStatus());

        verify(maternidadeRepository).findById(10L);
        verifyNoInteractions(bebeRepository, logAuditoriaRepository);
    }

    @Test
    void criar_devePersistirBebe_enviarLog_eRetornarResponse() {
        when(maternidadeRepository.findById(reqValida.maternidadeId)).thenReturn(maternidade);

        // Simula Panache persist setando id no objeto
        doAnswer(invocation -> {
            Bebe b = invocation.getArgument(0);
            b.id = 99L;
            return null;
        }).when(bebeRepository).persist(any(Bebe.class));

        BebeResponse resp = service.criar(reqValida, "operador-123");

        // Persistiu bebe
        ArgumentCaptor<Bebe> bebeCaptor = ArgumentCaptor.forClass(Bebe.class);
        verify(bebeRepository).persist(bebeCaptor.capture());
        Bebe bebePersistido = bebeCaptor.getValue();

        assertEquals("João", bebePersistido.nome);                 // trim
        assertEquals("Maria", bebePersistido.nomeMae);             // trim
        assertEquals("José", bebePersistido.nomePai);              // trim
        assertEquals("Bem-vindo!", bebePersistido.mensagemResponsavel); // trim
        assertEquals(maternidade, bebePersistido.maternidade);
        assertEquals(99L, bebePersistido.id);

        // Persistiu log auditoria
        ArgumentCaptor<LogAuditoria> logCaptor = ArgumentCaptor.forClass(LogAuditoria.class);
        verify(logAuditoriaRepository).persist(logCaptor.capture());
        LogAuditoria log = logCaptor.getValue();

        assertEquals(99L, log.idBebe);
        assertEquals("operador-123", log.idOperador);
        assertNotNull(log.dataHora);
        assertEquals("Criacao Bebe", log.acaoRealizada);

        // Response mapeado
        assertEquals(99L, resp.id);
        assertEquals("João", resp.nome);
        assertEquals(reqValida.dataNascimento, resp.dataNascimento);
        assertEquals("Maria", resp.nomeMae);
        assertEquals("José", resp.nomePai);
        assertEquals("Bem-vindo!", resp.mensagemResponsavel);
        assertEquals("base64", resp.fotoBase64);
        assertEquals("image/png", resp.fotoMime);
        assertEquals(10L, resp.maternidadeId);
        assertEquals("Maternidade X", resp.maternidadeNome);
    }

    // ---------- enviarlogAuditoria(...) ----------

    @Test
    void enviarlogAuditoria_deveLancarRuntimeException_quandoRepositorioFalha() {
        Bebe b = new Bebe();
        b.id = 1L;

        doThrow(new RuntimeException("db down"))
                .when(logAuditoriaRepository).persist(any(LogAuditoria.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.enviarlogAuditoria(b, "op1"));

        assertNotNull(ex.getCause()); // ele faz new RuntimeException(e)
        verify(logAuditoriaRepository).persist(any(LogAuditoria.class));
    }

    // ---------- toResponse(...) ----------

    @Test
    void toResponse_deveMapearSemMaternidade_quandoNula() {
        Bebe b = new Bebe();
        b.id = 7L;
        b.nome = "A";
        b.dataNascimento = LocalDateTime.of(2023, 5, 6, 18, 10);
        b.nomeMae = "M";
        b.nomePai = null;
        b.mensagemResponsavel = "msg";
        b.fotoBase64 = null;
        b.fotoMime = null;
        b.maternidade = null;

        BebeResponse r = BebeService.toResponse(b);

        assertEquals(7L, r.id);
        assertEquals("A", r.nome);
        assertEquals(LocalDateTime.of(2023, 5, 6, 18, 10), r.dataNascimento);
        assertEquals("M", r.nomeMae);
        assertNull(r.nomePai);
        assertEquals("msg", r.mensagemResponsavel);
        assertNull(r.maternidadeId);
        assertNull(r.maternidadeNome);
    }

    @Test
    void toResponse_deveMapearMaternidade_quandoPresente() {
        Bebe b = new Bebe();
        b.id = 8L;
        b.nome = "B";
        b.dataNascimento = LocalDateTime.of(2022, 1, 1, 18,15);
        b.nomeMae = "Mae";
        b.mensagemResponsavel = "msg";
        b.maternidade = maternidade;

        BebeResponse r = BebeService.toResponse(b);

        assertEquals(10L, r.maternidadeId);
        assertEquals("Maternidade X", r.maternidadeNome);
    }

    // ---------- criarEntidadeBebe(...) ----------

    @Test
    void criarEntidadeBebe_deveAplicarTrimETernariosParaNullBlank() {
        BebeCreateRequest req = new BebeCreateRequest();
        req.nome = "  Ana  ";
        req.dataNascimento = LocalDateTime.of(2020, 2, 3,12,3);
        req.nomeMae = "  Mae  ";
        req.nomePai = "   "; // blank -> null
        req.mensagemResponsavel = "  ok  ";
        req.fotoBase64 = "   "; // blank -> null
        req.fotoMime = null;     // null -> null

        Bebe b = service.criarEntidadeBebe(req, maternidade);

        assertEquals("Ana", b.nome);
        assertEquals(LocalDateTime.of(2020, 2, 3, 12, 3), b.dataNascimento);
        assertEquals("Mae", b.nomeMae);
        assertNull(b.nomePai);
        assertEquals("ok", b.mensagemResponsavel);
        assertNull(b.fotoBase64);
        assertNull(b.fotoMime);
        assertEquals(maternidade, b.maternidade);
    }
}