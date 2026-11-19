
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
    // Cargar los usuarios guardados en el archivo

    // Cargar los usuarios guardados en el archivo
    private final ArrayList<Usuario> usuarios =
            ArchivoUsuarios.cargarUsuarios();

    // Contador para asignar los ID
    private int siguienteId = 1;


    // Constructor
    public FormMenu() {

        // Continuar los ID desde el último usuario registrado
        for (Usuario usuario : usuarios) {
            if (usuario.getId() >= siguienteId) {
                siguienteId = usuario.getId() + 1;
            }
        }

        setTitle("BookUsers - Menú principal");

        setContentPane(panelPrincipal);

        setSize(500, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Abrir formulario de registro
        // Abrir formulario de registro
        btnRegistrar.addActionListener(e -> {

            FormRegistro registro = new FormRegistro();

            registro.setUsuarios(usuarios);

            JFrame ventanaRegistro =
                    new JFrame("BookUsers - Registro de usuarios");

            ventanaRegistro.setContentPane(
                    registro.getPanelPrincipal()
            );

            ventanaRegistro.setDefaultCloseOperation(
                    JFrame.DISPOSE_ON_CLOSE
            );

            ventanaRegistro.setSize(600, 750);
            ventanaRegistro.setLocationRelativeTo(null);
            ventanaRegistro.setVisible(true);

        });

        // Abrir listado de usuarios
        btnListado.addActionListener(e -> {

            FormListado listado = new FormListado();

            listado.cargarUsuarios(usuarios);

            JFrame ventanaListado =
                    new JFrame("BookUsers - Listado de usuarios");

            ventanaListado.setContentPane(
                    listado.getPanelPrincipal()
            );

            ventanaListado.setDefaultCloseOperation(
                    JFrame.DISPOSE_ON_CLOSE
            );

            ventanaListado.setSize(950, 550);
            ventanaListado.setLocationRelativeTo(null);
            ventanaListado.setVisible(true);

        });

    }

    // Método principal
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FormMenu ventana = new FormMenu();

            ventana.setVisible(true);

        });
    }
}
