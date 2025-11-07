import java.sql.*;
import java.util.*;

public class AvionDAO {
    public List<String> obtenerAviones() {
        List<String> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MODELO, TIPO, ANO_INGRESO, ESTADO FROM AVIONES")) {
            while (rs.next()) {
                lista.add(rs.getString(1) + " - " + rs.getString(2) + " - " + rs.getInt(3) + " - " + rs.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public void ejecutarSP() {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call POPULATE_SAMPLE_DATA}")) {
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


