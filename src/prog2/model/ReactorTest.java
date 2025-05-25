package prog2.model;

import org.junit.jupiter.api.Test;
import prog2.vista.CentralUBException;

import static org.junit.jupiter.api.Assertions.*;

public class ReactorTest {

    @Test
    public void testCalculaOutput() throws CentralUBException {
        Reactor reactor = new Reactor();
        reactor.activa();
        reactor.setTemperatura(500);

        assertEquals(1000.0f, reactor.calculaOutput(50), 0.001);
    }

    @Test
    public void testRevisa() {
        Reactor reactor = new Reactor();
        PaginaIncidencies pagina = new PaginaIncidencies(1);

        reactor.setTemperatura(1001);
        reactor.revisa(pagina);

        assertTrue(pagina.toString().contains("Reactor desactivat per superar " + Reactor.TEMP_MAXIMA + "C (temperatura: " + reactor.getTemperatura() + "C)"));


        assertFalse(reactor.getActivat());
    }
}