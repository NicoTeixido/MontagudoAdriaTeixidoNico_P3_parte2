import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Prova extends JFrame{

    private JPanel panelProva;
    private JTextField textTextField;
    private JButton adeuButton;
    private JButton holaButton;
    private JCheckBox botonsActiusCheckBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Prova prova = new Prova();
            prova.setVisible(true);
        });


    }
    public Prova(){
        setTitle("Prova GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panelProva);
        setSize(500,400);
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
    }

}