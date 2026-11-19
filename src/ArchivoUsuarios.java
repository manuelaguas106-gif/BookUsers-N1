import java.io.*;
import java.util.ArrayList;

    public class ArchivoUsuarios {

        private static final String ARCHIVO = "usuarios.dat";

        // Guardar la lista de usuarios en un archivo
        public static void guardarUsuarios(ArrayList<Usuario> usuarios) {

            try (ObjectOutputStream salida =
                         new ObjectOutputStream(
                                 new FileOutputStream(ARCHIVO))) {

                salida.writeObject(usuarios);

            } catch (IOException e) {
                System.err.println(
                        "Error al guardar usuarios: " + e.getMessage()
                );
            }
        }

        // Recuperar los usuarios almacenados
        public static ArrayList<Usuario> cargarUsuarios() {

            File archivo = new File(ARCHIVO);

            if (!archivo.exists()) {
                return new ArrayList<>();
            }

            try (ObjectInputStream entrada =
                         new ObjectInputStream(
                                 new FileInputStream(archivo))) {

                Object datos = entrada.readObject();

                if (datos instanceof ArrayList<?>) {
                    ArrayList<?> lista = (ArrayList<?>) datos;

                    ArrayList<Usuario> usuarios = new ArrayList<>();

                    for (Object elemento : lista) {
                        if (!(elemento instanceof Usuario)) {
                            throw new IOException("Archivo de usuarios inválido.");
                        }
                        usuarios.add((Usuario) elemento);
                    }

                    return usuarios;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println(
                        "Error al cargar usuarios: " + e.getMessage()
                );
            }

            return new ArrayList<>();
        }
    }
