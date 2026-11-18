import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Locale;


public class FormRegistro {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JComboBox cmbTipoId;
    private JTextField txtNumeroId;
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JLabel lblUsername;
    private JPasswordField txtPassword;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JLabel lblFoto;
    private JButton btnCargarFoto;
    private JButton btnGuardar;
    private JButton btnVolver;

    // Constructor del formulario
    public FormRegistro() {

        // Configurar tipos de identificación
        cmbTipoId.addItem("Seleccione un documento");
        cmbTipoId.addItem("Cédula de ciudadanía");
        cmbTipoId.addItem("Tarjeta de identidad");
        cmbTipoId.addItem("Pasaporte");
        cmbTipoId.setSelectedIndex(0);

        // Botón para volver al menú principal
        btnVolver.addActionListener(e -> {

            // Obtener la ventana que contiene el formulario
            java.awt.Window ventana =
                    SwingUtilities.getWindowAncestor(panelPrincipal);

            // Cerrar solamente la ventana de registro
            if (ventana != null) {
                ventana.dispose();
            }

        });

        // Generar username automáticamente
        DocumentListener listener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                generarUsername();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                generarUsername();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                generarUsername();
            }
        };
        txtPrimerNombre.getDocument().addDocumentListener(listener);
        txtPrimerApellido.getDocument().addDocumentListener(listener);

        // Validar datos al presionar Guardar usuario
        btnGuardar.addActionListener(e -> {

            if (validarCampos()) {
                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "Validación inicial correcta. Pendiente guardar usuario."
                );
            }

        });
    }

    // Obtener el panel principal del formulario
    public JPanel getPanelPrincipal() {
        return panelPrincipal;}


    // Validar los campos obligatorios del formulario
    private boolean validarCampos() {

        if (cmbTipoId.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Seleccione un tipo de identificación."
            );
            return false;
        }

        // Validar que la identificación contenga solo números
        String numeroId = txtNumeroId.getText().trim();

        if (!numeroId.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "El número de identificación solo debe contener números."
            );
            return false;
        }

        // Validar que el teléfono contenga solo números
        String telefono = txtTelefono.getText().trim();

        if (!telefono.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "El teléfono solo debe contener números."
            );
            return false;
        }

        if (txtNumeroId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese el número de identificación."
            );
            return false;
        }

        if (txtPrimerNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese el primer nombre."
            );
            return false;
        }

        if (txtPrimerApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese el primer apellido."
            );
            return false;
        }

        if (txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese una contraseña."
            );
            return false;
        }

        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese el número de teléfono."
            );
            return false;
        }

        if (txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese el correo electrónico."
            );
            return false;
        }

        // Validar el formato del correo electrónico
        String correo = txtCorreo.getText().trim();

        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(
                    panelPrincipal,
                    "Ingrese un correo electrónico válido."
            );
            return false;
        }

        return true;
    }



    // Generar automáticamente el nombre de usuario
        private void generarUsername() {

            String nombre = txtPrimerNombre.getText().trim();
            String apellido = txtPrimerApellido.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                lblUsername.setText("Pendiente de generar");
                return;
            }

            String username = (nombre + apellido)
                    .toLowerCase(Locale.ROOT)
                    .replaceAll("\\s+", "");

            lblUsername.setText(username);
        }

    }
