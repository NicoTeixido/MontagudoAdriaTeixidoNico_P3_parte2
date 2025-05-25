package prog2.model;

import org.junit.jupiter.api.Test;
import prog2.vista.CentralUBException;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaRefrigeracioTest {

    @Test
    public void testCalculaOutput() throws CentralUBException {
        SistemaRefrigeracio sistema = new SistemaRefrigeracio();
        VariableUniforme variableUniforme = new VariableUniforme(123);

        for (int i = 0; i < 4; i++) {
            BombaRefrigerant bomba = new BombaRefrigerant(i, variableUniforme);
            bomba.activa();
            sistema.afegirBomba(bomba);
        }


        assertEquals(400.0f, sistema.calculaOutput(500), 0.001);
    }

    @Test
    public void testCostOperatiu() throws CentralUBException {
        SistemaRefrigeracio sistema = new SistemaRefrigeracio();
        BombaRefrigerant bomba = new BombaRefrigerant(1, new VariableUniforme(123));
        bomba.activa();
        sistema.afegirBomba(bomba);

        assertEquals(130.0f, sistema.getCostOperatiu(), 0.001);
    }
}