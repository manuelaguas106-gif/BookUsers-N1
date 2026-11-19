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


    }

        public void cargarUsuarios(java.util.ArrayList<Usuario> usuarios) {

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