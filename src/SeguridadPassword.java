import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

    public class SeguridadPassword {

        private static final int ITERACIONES = 600000;
        private static final int LONGITUD_HASH = 256;
        private static final int LONGITUD_SAL = 16;

        public static String generarHash(String password) {
            byte[] sal = new byte[LONGITUD_SAL];
            new SecureRandom().nextBytes(sal);

            PBEKeySpec especificacion = new PBEKeySpec(
                    password.toCharArray(),
                    sal,
                    ITERACIONES,
                    LONGITUD_HASH
            );

            try {
                SecretKeyFactory fabrica =
                        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                byte[] hash = fabrica.generateSecret(especificacion).getEncoded();

                return "PBKDF2$" + ITERACIONES + "$"
                        + Base64.getEncoder().encodeToString(sal)
                        + "$" + Base64.getEncoder().encodeToString(hash);

            } catch (Exception e) {
                throw new IllegalStateException(
                        "Error al proteger la contraseña", e
                );
            } finally {
                especificacion.clearPassword();
            }
        }

        public static boolean verificarPassword(String password, String hashGuardado) {
            try {
                String[] partes = hashGuardado.split("\\$");

                if (partes.length != 4 || !partes[0].equals("PBKDF2")) {
                    return false;
                }

                int iteraciones = Integer.parseInt(partes[1]);
                byte[] sal = Base64.getDecoder().decode(partes[2]);
                byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

                PBEKeySpec especificacion = new PBEKeySpec(
                        password.toCharArray(),
                        sal,
                        iteraciones,
                        hashOriginal.length * 8
                );

                try {
                    SecretKeyFactory fabrica =
                            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                    byte[] hashCalculado =
                            fabrica.generateSecret(especificacion).getEncoded();

                    return java.security.MessageDigest.isEqual(
                            hashOriginal, hashCalculado
                    );
                } finally {
                    especificacion.clearPassword();
                }

            } catch (Exception e) {
                return false;
            }
        }

    }
