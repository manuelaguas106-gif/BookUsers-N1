import javax.swing.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;


public class FormListado {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JTable tablaUsuarios;
    private JTextField txtBuscar;
    private JButton btnEliminar;
    private JButton btnEditar;
    private java.util.ArrayList<Usuario> usuarios;

    public FormListado() {

        String[] columnas = {
                "ID",
                "Tipo de identificación",
                "Número de identificación",
                "Nombre completo",
                "Usuario",
                "Teléfono",
                "Correo electrónico"
        };

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        tablaUsuarios.setModel(modelo);

// Ajustar el ancho de las columnas
        tablaUsuarios.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(170);
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(250);
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(140);
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(130);
        tablaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(250);


// Crear el filtro para buscar usuarios
        TableRowSorter<DefaultTableModel> filtro =
                new TableRowSorter<>(modelo);

        tablaUsuarios.setRowSorter(filtro);

// Detectar cuando se escribe en el buscador
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscar();
            }

            private void buscar() {
                String texto = txtBuscar.getText().trim();

                if (texto.isEmpty()) {
                    filtro.setRowFilter(null);
                } else {
                    filtro.setRowFilter(
                            RowFilter.regexFilter(
                                    "(?i)" + java.util.regex.Pattern.quote(texto)
                            )
                    );
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "Por favor, selecciona un usuario para eliminar."
                );
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    panelPrincipal,
                    "¿Estás seguro de que deseas eliminar este usuario?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {

                int filaModelo = tablaUsuarios.convertRowIndexToModel(filaSeleccionada);

                // Obtener el ID del usuario seleccionado
                int idUsuario = Integer.parseInt(
                        tablaUsuarios.getModel().getValueAt(filaModelo, 0).toString()
                );
                // Obtener el usuario antes de eliminarlo
                Usuario usuarioAEliminar = usuarios.stream()
                        .filter(usuario -> usuario.getId() == idUsuario)
                        .findFirst()
                        .orElse(null);

                // Eliminar el usuario de la lista
                boolean eliminado = usuarios.removeIf(
                        usuario -> usuario.getId() == idUsuario
                );

                if (eliminado) {
                    String rutaFotoEliminar = "";

                    if (usuarioAEliminar != null &&
                            usuarioAEliminar.getRutaFoto() != null) {

                        rutaFotoEliminar = usuarioAEliminar.getRutaFoto();
                    }
                    String fotoAComprobar = rutaFotoEliminar;

                    boolean fotoCompartida = !fotoAComprobar.isEmpty()
                            && usuarios.stream().anyMatch(usuario ->
                            fotoAComprobar.equals(usuario.getRutaFoto())
                    );
                    // Comprobar que la fotografía pertenece a la carpeta fotos
                    java.nio.file.Path carpetaFotos =
                            java.nio.file.Paths.get("fotos").toAbsolutePath().normalize();

                    java.nio.file.Path archivoFotoEliminar = null;

                    if (!fotoAComprobar.isEmpty()) {
                        java.nio.file.Path rutaCandidata =
                                java.nio.file.Paths.get(fotoAComprobar)
                                        .toAbsolutePath()
                                        .normalize();

                        if (rutaCandidata.startsWith(carpetaFotos)
                                && !rutaCandidata.equals(carpetaFotos)) {
                            archivoFotoEliminar = rutaCandidata;
                        }
                    }

                    ArchivoUsuarios.guardarUsuarios(usuarios);
                    if (!fotoCompartida && archivoFotoEliminar != null) {
                        try {
                            java.nio.file.Files.deleteIfExists(archivoFotoEliminar);
                        } catch (java.io.IOException ex) {
                            JOptionPane.showMessageDialog(
                                    panelPrincipal,
                                    "El usuario fue eliminado, pero no se pudo borrar su fotografía: "
                                            + ex.getMessage()
                            );
                        }
                    }
                    cargarUsuarios(usuarios);

                    JOptionPane.showMessageDialog(
                            panelPrincipal,
                            "Usuario eliminado correctamente."
                    );
                }
            }
        });

        btnEditar.addActionListener(e -> {

            int filaSeleccionada = tablaUsuarios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "Por favor, selecciona un usuario para editar."
                );
                return;
            }


            int filaModelo = tablaUsuarios.convertRowIndexToModel(filaSeleccionada);

            int idUsuario = Integer.parseInt(
                    tablaUsuarios.getModel().getValueAt(filaModelo, 0).toString()
            );

            Usuario usuarioSeleccionado = null;

            for (Usuario usuario : usuarios) {
                if (usuario.getId() == idUsuario) {
                    usuarioSeleccionado = usuario;
                    break;
                }
            }

            if (usuarioSeleccionado == null) {
                JOptionPane.showMessageDialog(
                        panelPrincipal,
                        "No se encontró el usuario seleccionado."
                );
                return;
            }

            FormRegistro formulario = new FormRegistro();

            formulario.setUsuarios(usuarios);

            formulario.cargarUsuarioParaEditar(usuarioSeleccionado);

            JFrame ventanaEditar = new JFrame("Editar usuario");
            ventanaEditar.setContentPane(formulario.getPanelPrincipal());
            ventanaEditar.pack();
            ventanaEditar.setLocationRelativeTo(null);
            ventanaEditar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaEditar.setVisible(true);
            ventanaEditar.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    cargarUsuarios(usuarios);
                }
            });


        });

    }


    public void cargarUsuarios(java.util.ArrayList<Usuario> usuarios) {

        this.usuarios = usuarios;

        javax.swing.table.DefaultTableModel modelo =
                    (javax.swing.table.DefaultTableModel) tablaUsuarios.getModel();

            modelo.setRowCount(0);

            for (Usuario usuario : usuarios) {

                String nombreCompleto =
                        usuario.getPrimerNombre() + " " +
                                usuario.getSegundoNombre() + " " +
                                usuario.getPrimerApellido() + " " +
                                usuario.getSegundoApellido();

                Object[] fila = {
                        usuario.getId(),
                        usuario.getTipoIdentificacion(),
                        usuario.getNumeroIdentificacion(),
                        nombreCompleto.trim().replaceAll("\\s+", " "),
                        usuario.getUsername(),
                        usuario.getTelefono(),
                        usuario.getCorreo()
                };

                modelo.addRow(fila);
            }
        }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

}