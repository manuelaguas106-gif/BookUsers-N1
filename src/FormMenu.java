
import javax.swing.*;
import java.util.ArrayList;

public class FormMenu extends JFrame {

    private JPanel panelPrincipal;
    private JPanel panelContenido;
    private JLabel lblTitulo;
    private JButton btnRegistrar;
    private JButton btnListado;
    private JButton btnCreditos;

    // Lista compartida de usuarios
    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    // Contador para asignar los ID
    private int siguienteId = 1;

    // Constructor
    public FormMenu() {

        setTitle("BookUsers - Menú principal");

        setContentPane(panelPrincipal);

        setSize(500, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Método principal
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FormMenu ventana = new FormMenu();

            ventana.setVisible(true);

        });
    }
}
