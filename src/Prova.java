import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List; // Importar List si no está ya

public class Prova extends JFrame {

    private JPanel panelProva;
    private JTextField textTextField;
    private JButton adeuButton;
    private JButton holaButton;
    private JCheckBox botonsActiusCheckBox;
    private JButton btnMostrar;
    private JList<A> lstLlista1;
    private JButton btnEliminar;

    ArrayList llista;

    class A {
        int x;
        int y;

        public A(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "x= " + x + "\ny= " + y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Prova prova = new Prova();
            prova.setVisible(true);
        });
    }

    public Prova() {
        setTitle("Prova GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panelProva);
        setSize(500, 400);
        setLocationRelativeTo(null);

        botonsActiusCheckBox.setSelected(true);
        textTextField.setEnabled(true);

        holaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTextField.setText(holaButton.getText());
            }
        });
        adeuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textTextField.setText(adeuButton.getText());
            }
        });
        botonsActiusCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                holaButton.setEnabled(botonsActiusCheckBox.isSelected());
                adeuButton.setEnabled(botonsActiusCheckBox.isSelected());
            }
        });

        llista = null;
        afegirDadesTest();

        btnEliminar.setEnabled(false);

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                omplirLlista();
            }
        });

        lstLlista1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    btnEliminar.setEnabled(!lstLlista1.isSelectionEmpty());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (A elementSeleccionat : lstLlista1.getSelectedValuesList()) { // <-- La línea problemática se corrige aquí
                    llista.remove(elementSeleccionat);
                }
                omplirLlista();
            }
        });
    }

    private void afegirDadesTest() {
        llista = new ArrayList(); // ArrayList por defecto es de Object, pero los métodos add lo infieren
        llista.add(new A(1, 2));
        llista.add(new A(3, 4));
        llista.add(new A(5, 6));
    }

    void omplirLlista() {
        DefaultListModel<A> model = new DefaultListModel<>(); // <--- ¡Asegúrate de este cambio también!
        model.clear();

        for (A item : llista) {
            model.addElement(item);
        }
        lstLlista1.setModel(model);
    }
}