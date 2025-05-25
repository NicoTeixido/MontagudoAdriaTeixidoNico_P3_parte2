package prog2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bitacola implements InBitacola, Serializable {
    private List<PaginaBitacola> _pagines;

    // Constructor
    public Bitacola() {
        this._pagines = new ArrayList<>(); // Inicializacion directa
    }

    // Implementacio de afegeixPagina()
    public void afegeixPagina(PaginaBitacola p) {
        if (p != null) { // Validacio per evitar nulls
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
        return incidencies; // Retorna llista buida si no n'hi ha
    }

    // Override de toString()
    @Override
    public String toString() {
        if (_pagines.isEmpty()) {
            return "La bitacola esta buida.";
        }
        StringBuilder sb = new StringBuilder();
        for (PaginaBitacola p : _pagines) {
            sb.append(p.toString()).append("\n\n"); // Per separar pagines
        }
        return sb.toString().trim(); // Elimina l'ultim salt de linia
    }
}