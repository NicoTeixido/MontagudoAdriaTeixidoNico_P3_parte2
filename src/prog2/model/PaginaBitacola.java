package prog2.model;

import java.io.Serializable;

public abstract class PaginaBitacola implements Serializable {
    private final int _dia;  // Dia al que correspon la pàgina

    // Constructor
    public PaginaBitacola(int dia) {
        this._dia = dia;
    }

    // Getter del día
    public int getDia() {
        return _dia;
    }

    // Metodo toString
    @Override
    public abstract String toString();
}
