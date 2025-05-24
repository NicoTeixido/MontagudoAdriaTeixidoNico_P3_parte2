package prog2.adaptador;

import prog2.model.*;
import prog2.vista.CentralUBException;
import java.io.*;
import java.util.List; // Added for the new method

public class Adaptador {
    private Dades _dades;

    // Constructor
    public Adaptador() {
        this._dades = new Dades();
    }

    /* **********************
     * METODES DE PERSISTENCIA *
     *********************** */
    public void guardaDades(String camiDesti) throws CentralUBException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(camiDesti))) {
            oos.writeObject(_dades);
        } catch (IOException e) {
            throw new CentralUBException("Error al guardar les dades: " + e.getMessage());
        }
    }

    public void carregaDades(String camiOrigen) throws CentralUBException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(camiOrigen))) {
            _dades = (Dades) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CentralUBException("Error al carregar les dades: " + e.getMessage());
        }
    }

    /* **********************
     * METODES DE GESTIO *
     *********************** */

    // Reactor
    public void activaReactor() throws CentralUBException {
        try {
            _dades.activaReactor();
        } catch (CentralUBException e) {
            throw new CentralUBException("No s'ha pogut activar el reactor: " + e.getMessage());
        }
    }

    public void desactivaReactor() {
        _dades.desactivaReactor();
    }

    public String getEstatReactor() {
        return _dades.mostraReactor().toString();
    }

    // Sistema de Refrigeracio
    public void activaBomba(int idBomba) throws CentralUBException {
        try {
            _dades.activaBomba(idBomba);
        } catch (CentralUBException e) {
            throw new CentralUBException("No s'ha pogut activar la bomba: " + e.getMessage());
        }
    }

    public void desactivaBomba(int idBomba) {
        _dades.desactivaBomba(idBomba);
    }

    // NEW: Method to activate all pumps in the refrigeration system
    public void activaTotesBombesRefrigeracio() throws CentralUBException {
        try {
            _dades.activaSistemaRefrigeracio(); // Assuming Dades will have this method
        } catch (CentralUBException e) {
            throw new CentralUBException("Error en activar totes les bombes de refrigeracio: " + e.getMessage());
        }
    }

    // NEW: Method to deactivate all pumps in the refrigeration system
    public void desactivaTotesBombesRefrigeracio() {
        _dades.desactivaSistemaRefrigeracio(); // Assuming Dades will have this method
    }

    public String getEstatSistemaRefrigeracio() {
        return _dades.mostraSistemaRefrigeracio().toString();
    }

    // Bitacola i Informes

    public String getBitacolaCompleta() {
        return _dades.mostraBitacola().toString();
    }

    public String getIncidencies() {
        StringBuilder sb = new StringBuilder();
        List<PaginaIncidencies> incidencies = _dades.mostraIncidencies();
        if (incidencies.isEmpty()) {
            sb.append("No hi ha incidencies registrades.");
        } else {
            for (PaginaIncidencies pagina : incidencies) {
                sb.append(pagina.toString()).append("\n");
            }
        }
        return sb.toString().trim();
    }

    // Gestio del Dia
    public void finalitzaDia(float demandaPotencia) throws CentralUBException {
        try {
            // The finalitzaDia in Dades now returns the updated Bitacola instance
            // but we don't need to capture it here since _dades already holds the reference.
            _dades.finalitzaDia(demandaPotencia);
        } catch (CentralUBException e) {
            throw new CentralUBException("Error al finalitzar el dia: " + e.getMessage());
        }
    }

    public String getEstatActual() {
        return _dades.mostraEstat().toString();
    }

    // Metodes addicionals
    public float getPotenciaGenerada() {
        return _dades.calculaPotencia();
    }

    public float getGuanysAcumulats() {
        return _dades.getGuanysAcumulats();
    }

    public void setInsercioBarres(float grau) throws CentralUBException {
        try {
            _dades.setInsercioBarres(grau);
        } catch (CentralUBException e) {
            throw new CentralUBException("Error en les barres de control: " + e.getMessage());
        }
    }

    /**
     * Obtiene el día actual del modelo de datos.
     * @return El número del día actual.
     */
    public int getDiaActual() {
        return _dades.getDia();
    }
}