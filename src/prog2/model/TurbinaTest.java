package prog2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurbinaTest {

    @Test
    public void testCalculaOutput() {
        Turbina turbina = new Turbina();


        assertEquals(200.0f, turbina.calculaOutput(100), 0.001);


        assertEquals(0.0f, turbina.calculaOutput(99), 0.001);
    }

    @Test
    public void testCostOperatiu() {
        Turbina turbina = new Turbina();
        assertEquals(20.0f, turbina.getCostOperatiu(), 0.001);
    }
}