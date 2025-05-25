package prog2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneradorVaporTest {

    @Test
    public void testCalculaOutput() {
        GeneradorVapor generador = new GeneradorVapor();

        // Prueba con temperatura válida
        assertEquals(452.5f, generador.calculaOutput(500), 0.001);

        // Prueba con temperatura igual a ambiente
        assertEquals(25.0f, generador.calculaOutput(25), 0.001);
    }

    @Test
    public void testCostOperatiu() {
        GeneradorVapor generador = new GeneradorVapor();
        assertEquals(25.0f, generador.getCostOperatiu(), 0.001);
    }
}