import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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


    private java.util.ArrayList<Usuario> usuarios;

    private Usuario usuarioEnEdicion = null;

    private String rutaFoto = "";

    private int siguienteId;

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }
        public void cargarUsuarioParaEditar(Usuario usuario) {

            this.usuarioEnEdicion = usuario;

            cmbTipoId.setSelectedItem(usuario.getTipoIdentificacion());
            txtNumeroId.setText(usuario.getNumeroIdentificacion());

            txtPrimerNombre.setText(usuario.getPrimerNombre());
            txtSegundoNombre.setText(usuario.getSegundoNombre());

            txtPrimerApellido.setText(usuario.getPrimerApellido());
            txtSegundoApellido.setText(usuario.getSegundoApellido());

            txtTelefono.setText(usuario.getTelefono());
            txtCorreo.setText(usuario.getCorreo());

            lblUsername.setText(usuario.getUsername());

            txtPassword.setText("");
            // Recuperar la fotografía del usuario
            rutaFoto = usuario.getRutaFoto();

            if (rutaFoto != null && !rutaFoto.isEmpty()) {
                java.io.File archivoFoto = new java.io.File(rutaFoto);

                if (archivoFoto.exists()) {
                    ImageIcon imagen = new ImageIcon(rutaFoto);

                    Image imagenAjustada = imagen.getImage().getScaledInstance(
                            150, 150, Image.SCALE_SMOOTH
                    );

                    lblFoto.setIcon(new ImageIcon(imagenAjustada));
                    lblFoto.setText("");
                }
            }

            btnGuardar.setText("Guardar cambios");
        }

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

        btnCargarFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();

            selector.setDialogTitle("Seleccionar fotografía");

            selector.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "Imágenes JPG y PNG", "jpg", "jpeg", "png"
                    )
            );

            int resultado = selector.showOpenDialog(panelPrincipal);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                java.io.File archivo = selector.getSelectedFile();

                try {
                    java.nio.file.Path carpetaFotos =
                            java.nio.file.Paths.get("fotos");

                    java.nio.file.Files.createDirectories(carpetaFotos);

                    String nombreArchivo =
                            java.util.UUID.randomUUID().toString()
                                    + "."
                                    + archivo.getName().substring(
                                    archivo.getName().lastIndexOf(".") + 1
                            ).toLowerCase();

                    java.nio.file.Path destino =
                            carpetaFotos.resolve(nombreArchivo);

                    java.nio.file.Files.copy(
                            archivo.toPath(),
                            destino
                    );

                    rutaFoto = destino.toString();

                } catch (java.io.IOException ex) {
                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "Error al copiar la fotografía: " + ex.getMessage()
                    );
                    return;
                }
                ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());

                Image imagenAjustada = imagen.getImage().getScaledInstance(
                        150, 150, Image.SCALE_SMOOTH
                );

                lblFoto.setIcon(new ImageIcon(imagenAjustada));
                lblFoto.setText("");

                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "Fotografía seleccionada: " + archivo.getName()
                );
            }
        });

        // Validar datos al presionar Guardar usuario
        btnGuardar.addActionListener(e -> {

            if (validarCampos()) {

                if (usuarios == null) {
                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "No se ha configurado la lista de usuarios."
                    );
                    return;
                }

                // Obtener los datos del formulario
                String tipoId = cmbTipoId.getSelectedItem().toString();
                String numeroId = txtNumeroId.getText().trim();
                String primerNombre = txtPrimerNombre.getText().trim();
                String segundoNombre = txtSegundoNombre.getText().trim();
                String primerApellido = txtPrimerApellido.getText().trim();
                String segundoApellido = txtSegundoApellido.getText().trim();
                String username = lblUsername.getText();
                String password = new String(txtPassword.getPassword());
                String telefono = txtTelefono.getText().trim();
                String correo = txtCorreo.getText().trim();

                if (usuarioEnEdicion != null) {
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                panelPrincipal,
                                "Ingrese su contraseña para confirmar los cambios."
                        );
                        return;
                    }
                    if (!SeguridadPassword.verificarPassword(
                            password,
                            usuarioEnEdicion.getPassword())) {

                        JOptionPane.showMessageDialog(
                                panelPrincipal,
                                "Contraseña incorrecta. No se pueden guardar los cambios."
                        );

                        return;
                    }
                    usuarioEnEdicion.setTipoIdentificacion(tipoId);
                    usuarioEnEdicion.setNumeroIdentificacion(numeroId);
                    usuarioEnEdicion.setPrimerNombre(primerNombre);
                    usuarioEnEdicion.setSegundoNombre(segundoNombre);
                    usuarioEnEdicion.setPrimerApellido(primerApellido);
                    usuarioEnEdicion.setSegundoApellido(segundoApellido);
                    usuarioEnEdicion.setUsername(username);
                    usuarioEnEdicion.setTelefono(telefono);
                    usuarioEnEdicion.setCorreo(correo);
                    usuarioEnEdicion.setRutaFoto(rutaFoto);

                    ArchivoUsuarios.guardarUsuarios(usuarios);

                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "Usuario actualizado correctamente."
                    );

                    return;
                }
                password = SeguridadPassword.generarHash(password);
                // Crear el objeto Usuario
                Usuario nuevoUsuario = new Usuario(
                        siguienteId,
                        tipoId,
                        numeroId,
                        primerNombre,
                        segundoNombre,
                        primerApellido,
                        segundoApellido,
                        username,
                        password,
                        telefono,
                        correo,
                        rutaFoto
                );

                // Asignar un ID único al nuevo usuario
                int nuevoId = usuarios.stream()
                        .mapToInt(Usuario::getId)
                        .max()
                        .orElse(0) + 1;

                nuevoUsuario.setId(nuevoId);

                // Guardar en la lista
                usuarios.add(nuevoUsuario);

                ArchivoUsuarios.guardarUsuarios(usuarios);

                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "Usuario registrado correctamente."
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

        if (usuarios != null) {
            for (Usuario usuario : usuarios) {

                if (usuario.getNumeroIdentificacion().equals(numeroId)
                        && (usuarioEnEdicion == null
                        || usuario.getId() != usuarioEnEdicion.getId())) {

                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "Ya existe un usuario con este número de identificación."
                    );

                    return false;
                }
            }
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


        if (usuarioEnEdicion == null && txtPassword.getPassword().length == 0) {
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

        if (usuarios != null) {
            for (Usuario usuario : usuarios) {

                if (usuario.getCorreo().equalsIgnoreCase(correo)
                        && (usuarioEnEdicion == null
                        || usuario.getId() != usuarioEnEdicion.getId())) {

                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "Ya existe un usuario con este correo electrónico."
                    );

                    return false;
                }
            }
        }
        return true;
    }

        private void generarUsername() {

            String nombre = txtPrimerNombre.getText().trim();
            String apellido = txtPrimerApellido.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                lblUsername.setText("Pendiente de generar");
                return;
            }

            String usernameBase = (nombre + apellido)
                    .toLowerCase(Locale.ROOT)
                    .replaceAll("\\s+", "");

            String username = usernameBase;
            int contador = 1;

            if (usuarios != null) {
                boolean existe;

                do {
                    existe = false;

                    for (Usuario usuario : usuarios) {
                        if (usuario.getUsername().equalsIgnoreCase(username)
                                && (usuarioEnEdicion == null
                                || usuario.getId() != usuarioEnEdicion.getId())) {

                            existe = true;
                            username = usernameBase + contador;
                            contador++;
                            break;
                        }
                    }

                } while (existe);
            }

            lblUsername.setText(username);
        }
            public void setUsuarios(java.util.ArrayList<Usuario> usuarios) {
                this.usuarios = usuarios;
            }
        }
