package prog2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bitacola implements InBitacola, Serializable {
    // CRITICAL FIX: Removed 'static' keyword. Each Bitacola instance should have its own list of pages.
    private List<PaginaBitacola> _pagines;

    // Constructor
    // Removed 'dia' parameter as it's not used here and PaginaBitacola already tracks the day.
    public Bitacola() {
        this._pagines = new ArrayList<>(); // Inicializacion directa
    }

    // Implementacio de afegeixPagina()
    public void afegeixPagina(PaginaBitacola p) {
        if (p != null) { // Validacion per evitar nulls
            _pagines.add(p);
        }
    }

    // Implementacio de getIncidencies()
    @Override
    public List<PaginaIncidencies> getIncidencies() {
        List<PaginaIncidencies> incidencies = new ArrayList<>();
        for (PaginaBitacola p : _pagines) {
            if (p instanceof PaginaIncidencies pi) {
                incidencies.add(pi);
            }
        }
        return incidencies; // Retorna llista buida si no n'hi ha (sense prints)
    }

    // Override de toString() (versio neta)
    @Override
    public String toString() {
        if (_pagines.isEmpty()) {
            return "La bitacola esta buida.";
        }
        StringBuilder sb = new StringBuilder();
        for (PaginaBitacola p : _pagines) {
            sb.append(p.toString()).append("\n\n"); // Doble salt per separar pagines
        }
        return sb.toString().trim(); // Elimina l'ultim salt de linia
    }
}