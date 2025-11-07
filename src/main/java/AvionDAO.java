// AvionDAO.java
import java.sql.*;
import java.util.*;

public class AvionDAO {

    /**
     * Obtiene la lista de aviones registrados en la tabla AVIONES_A.
     * Muestra modelo, tipo, año de ingreso y fabricante.
     */
    public List<String> obtenerAviones() {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT MODELO, TIPO, ANO_INGRESO, FABRICANTE FROM AVIONES_A ORDER BY AVION_ID";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String modelo = rs.getString("MODELO");
                String tipo = rs.getString("TIPO");
                int ano = rs.getInt("ANO_INGRESO");
                String fabricante = rs.getString("FABRICANTE");

                lista.add(modelo + " - " + tipo + " - " + ano + " - " + fabricante);
            }

        } catch (SQLException e) {
            System.err.println("⚠ Error al obtener los aviones: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Ejecuta el procedimiento almacenado POPULATE_SAMPLE_DATA
     * para insertar datos de ejemplo.
     */
    public void ejecutarSP() {
        String callSP = "{call POPULATE_SAMPLE_DATA}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(callSP)) {

            cs.execute();
            System.out.println("✅ Procedimiento POPULATE_SAMPLE_DATA ejecutado correctamente.");

        } catch (SQLException e) {
            System.err.println("⚠ Error al ejecutar el procedimiento almacenado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
