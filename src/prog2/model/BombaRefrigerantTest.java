package prog2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import prog2.vista.CentralUBException;

public class BombaRefrigerantTest {

    @Test
    public void testCalculaOutput() throws CentralUBException {
        VariableUniforme variableUniforme = new VariableUniforme(123);
        BombaRefrigerant bomba = new BombaRefrigerant(1, variableUniforme);

        // Test bomba activa
        bomba.activa();
        assertEquals(100.0f, bomba.getCapacitat(), 0.001); // ← Usar getCapacitat()

        // Test bomba inactiva
        bomba.desactiva();
        assertEquals(0.0f, bomba.getCapacitat(), 0.001); // ← Usar getCapacitat()
    }

    @Test
    public void testRevisa() {
        VariableUniforme variableUniforme = new VariableUniforme(123); // Misma semilla para reproducibilidad
        BombaRefrigerant bomba = new BombaRefrigerant(1, variableUniforme);
        PaginaIncidencies pagina = new PaginaIncidencies(1);

        bomba.revisa(pagina);


        assertFalse(pagina.toString().contains("Bomba refrigerant 1 fora de servei") ||
                pagina.toString().isEmpty());
    }
}