package prog2.model;

import prog2.vista.CentralUBException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SistemaRefrigeracio implements InComponent, Serializable {
    private boolean _activat;
    private List<BombaRefrigerant> _bombes;


    public SistemaRefrigeracio() {
        this._activat = false;
        this._bombes = new ArrayList<>();
    }

    // Metode per afegir bombes
    public void afegirBomba(BombaRefrigerant bomba) {
        _bombes.add(bomba);
    }

    // Getter per a la llista de bombes (necessari per a Dades)
    public List<BombaRefrigerant> getBombes() {
        return _bombes;
    }

    @Override
    public void activa() throws CentralUBException {
        boolean algunaActivable = false;
        for (BombaRefrigerant bomba : _bombes) {
            try {
                if (!bomba.getForaDeServei()) {
                    bomba.activa();
                    algunaActivable = true;
                }
            } catch (CentralUBException e) {
                System.err.println("Error activant bomba " + bomba.getId() + ": " + e.getMessage());
            }
        }

        if (_bombes.isEmpty()) {

            _activat = false;
        } else {
            _activat = algunaActivable;
            if (!_activat) {
                throw new CentralUBException("Error: No s'ha pogut activar cap bomba del sistema de refrigeracio.");
            }
        }
    }

    @Override
    public void desactiva() {
        for (BombaRefrigerant bomba : _bombes) {
            bomba.desactiva();
        }
        _activat = false;
    }

    @Override
    public boolean getActivat() {
        return _activat;
    }

    @Override
    public float getCostOperatiu() {
        float costTotal = 0;
        for (BombaRefrigerant bomba : _bombes) {
            costTotal += bomba.getCostOperatiu();
        }
        return costTotal;
    }

    @Override
    public float calculaOutput(float input) {
        float capacitatTotal = 0;
        for (BombaRefrigerant bomba : _bombes) {
            capacitatTotal += bomba.getCapacitat();
        }
        return Math.min(input, capacitatTotal); // La refrigeracio no pot excedir la calor generada
    }

    @Override
    public void revisa(PaginaIncidencies p) {
        for (BombaRefrigerant bomba : _bombes) {
            bomba.revisa(p);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Sistema de Refrigeracio - Activat: " + _activat + "\n");
        if (_bombes.isEmpty()) {
            sb.append("  No hi ha bombes configurades.\n");
        } else {
            for (BombaRefrigerant bomba : _bombes) {
                sb.append("- ").append(bomba.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}