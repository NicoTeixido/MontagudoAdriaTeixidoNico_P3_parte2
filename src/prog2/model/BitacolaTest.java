package prog2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BitacolaTest {

    @Test
    public void testAfegeixPagina() {
        Bitacola bitacola = new Bitacola();
        PaginaIncidencies pagina = new PaginaIncidencies(1);

        bitacola.afegeixPagina(pagina);
        List<PaginaIncidencies> incidencias = bitacola.getIncidencies();

        assertEquals(1, incidencias.size());
        assertEquals(pagina, incidencias.get(0));
    }

    @Test
    public void testToString() {
        Bitacola bitacola = new Bitacola();
        PaginaIncidencies pagina = new PaginaIncidencies(1);
        pagina.afegeixIncidencia("Test incidencia");
        bitacola.afegeixPagina(pagina);

        String result = bitacola.toString();
        assertTrue(result.contains("Test incidencia"));
    }
}