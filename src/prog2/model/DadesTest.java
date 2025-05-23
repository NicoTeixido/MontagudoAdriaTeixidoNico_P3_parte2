package prog2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import prog2.vista.CentralUBException;

public class DadesTest {

    @Test
    public void testFinalitzaDia() throws CentralUBException {
        Dades dades = new Dades();
        dades.activaReactor();
        Bitacola bitacola = dades.finalitzaDia(1000);


        assertEquals(1, bitacola.getIncidencies().size());
        assertEquals(2, dades.mostraEstat().getDia());
    }

    @Test
    public void testCostOperatiuTotal() {
        Dades dades = new Dades();


        assertEquals(45.0f, dades.getCostOperatiuTotal(), 0.001);
    }
}