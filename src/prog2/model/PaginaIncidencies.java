package prog2.model;
import java.io.Serializable;
import java.util.ArrayList;

public class PaginaIncidencies extends PaginaBitacola implements Serializable {
    private ArrayList<String> incidencies;

    public PaginaIncidencies(int dia) {
        super(dia);
        this.incidencies = new  ArrayList<>();
    }

    public ArrayList<String> getIncidencies() {
        return this.incidencies;
    }

    public void afegeixIncidencia(String incidencia){
        this.incidencies.add(incidencia);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Pagina Incidencies\n");
        sb.append("Dia: ").append(getDia()).append("\n");
        sb.append("Llista d'incidencies:\n");
        if (incidencies.isEmpty()) {
            sb.append("No hi ha incidencies\n");
        } else {
            for (String incidencia : incidencies) {
                sb.append("- ").append(incidencia).append("\n");
            }
        }
        return sb.toString();
    }
}