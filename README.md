
# 鉁堬笍 Programa: Registro de Aviones Militares y Cascos (Oracle 19c) :.  

<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/4594e5f6-883d-49c8-ab7a-6674607f547b" />  

Este proyecto implementa un sistema completo de registro de **aviones militares** y sus respectivos **cascos** asociados, 
usando **Oracle 19c** como base de datos y una interfaz **Swing (Java)** para su gesti贸n.

Incluye:

- 馃П SQL para crear tablas **AVIONES** y **CASCOS**.
- 鈿欙笍 Stored Procedure PL/SQL (`POPULATE_SAMPLE_DATA`) para insertar 15 registros de ejemplo.
- 馃捇 Tres clases Java listas para usar en **IntelliJ IDEA**.
- 馃敡 Instrucciones detalladas de configuraci贸n y ejecuci贸n.

---

## 馃摌 1. SQL: Creaci贸n de Tablas

```sql
CREATE TABLE AVIONES (
  AVION_ID      NUMBER PRIMARY KEY,
  MODELO        VARCHAR2(100) NOT NULL,
  FABRICANTE    VARCHAR2(100),
  ANO_INGRESO   NUMBER(4),
  TIPO          VARCHAR2(50),
  OBSERVACIONES VARCHAR2(4000)
);

CREATE TABLE CASCOS (
  CASCO_ID     NUMBER PRIMARY KEY,
  AVION_ID     NUMBER NOT NULL,
  TALLA        VARCHAR2(10),
  MARCA        VARCHAR2(100),
  MODELO       VARCHAR2(100),
  ESTADO       VARCHAR2(50),
  FECHA_ENTREGA DATE,
  CONSTRAINT FK_CASCOS_AVIONES FOREIGN KEY (AVION_ID) REFERENCES AVIONES(AVION_ID)
);

CREATE SEQUENCE AVIONES_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE CASCOS_SEQ  START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE INDEX IDX_AVIONES_MODELO ON AVIONES(MODELO);
CREATE INDEX IDX_CASCOS_ESTADO ON CASCOS(ESTADO);
```

---

## 馃З 2. Stored Procedure: `POPULATE_SAMPLE_DATA`

Este procedimiento borra los datos previos e inserta **15 aviones** y un **casco asociado a cada avi贸n**.

```sql
CREATE OR REPLACE PROCEDURE POPULATE_SAMPLE_DATA IS
BEGIN
  DELETE FROM CASCOS;
  DELETE FROM AVIONES;
  COMMIT;

  -- Inserta 15 aviones
  INSERT INTO AVIONES(AVION_ID, MODELO, FABRICANTE, ANO_INGRESO, TIPO, OBSERVACIONES)
  VALUES(AVIONES_SEQ.NEXTVAL,'F-16A Fighting Falcon','General Dynamics',1990,'Caza','Grupo A');
  -- ... (resto de inserts del documento original)

  COMMIT;

  FOR r IN (SELECT AVION_ID, MODELO FROM AVIONES) LOOP
    INSERT INTO CASCOS(CASCO_ID, AVION_ID, TALLA, MARCA, MODELO, ESTADO, FECHA_ENTREGA)
    VALUES (CASCOS_SEQ.NEXTVAL, r.AVION_ID,
            CASE WHEN MOD(r.AVION_ID,3)=0 THEN 'L' WHEN MOD(r.AVION_ID,3)=1 THEN 'M' ELSE 'S' END,
            'HelmoCorp', r.MODELO || ' - H1', 'Bueno', SYSDATE - MOD(r.AVION_ID,30));
  END LOOP;

  COMMIT;
END POPULATE_SAMPLE_DATA;
/
```

---

## 馃捇 3. C贸digo Java (IntelliJ IDEA)

El proyecto incluye **tres clases Java** distribuidas en paquetes :

```
### DBConnection.java
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "USUARIO";
    private static final String PASSWORD = "CONTRASENA";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
 }
 
 ### AvionDAO.java
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
 
 ### AvionesGUI.java
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.util.List;
 public class AvionesGUI extends JFrame {
    private JTextArea area;
    private AvionDAO dao = new AvionDAO();
    public AvionesGUI() {
        setTitle("Registro de Aviones y Cascos — Oracle 19c");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        area = new JTextArea();
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel();
        JButton btnListar = new JButton("Cargar / Refrescar");
        JButton btnInsertar = new JButton("Insertar 15 ejemplos (SP)");
        panelBotones.add(btnListar);
        panelBotones.add(btnInsertar);
        add(panelBotones, BorderLayout.SOUTH);
        btnListar.addActionListener(e -> mostrarAviones());
        btnInsertar.addActionListener(e -> {
            dao.ejecutarSP();
            mostrarAviones();
        });
    }
    private void mostrarAviones() {
        List<String> aviones = dao.obtenerAviones();
        area.setText("");
        for (String a : aviones) area.append(a + "\n");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AvionesGUI().setVisible(true));
    }
 }

```
---

## 鈿欙笍 4. Configuración JDBC	

 Edita el archivo DBConnection.java:
 private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
 private static final String USER = "USUARIO";
 private static final String PASSWORD = "CONTRASENA";

```xml
<dependency>
  <groupId>com.oracle.database.jdbc</groupId>
  <artifactId>ojdbc8</artifactId>
  <version>19.3.0.0</version>
</dependency>
```

5. Ejecución
 1. Ejecuta el script SQL para crear tablas y procedimiento. 2. Ejecuta AvionesGUI.java desde
 IntelliJ IDEA. 3. Usa los botones: - **Cargar / Refrescar** → Lista registros actuales. - **Insertar 15
 ejemplos (SP)** → Llama al procedimiento

6. Ejecuta primero el script SQL y el procedimiento `POPULATE_SAMPLE_DATA` en Oracle.
7. Luego ejecuta `AvionesGUI.main()`.

---

## 馃 Notas y Recomendaciones

- Compatible con Oracle 19c y Java 11+. - Los nombres usan solo caracteres ASCII por
 compatibilidad. - Requiere permisos CREATE TABLE, CREATE SEQUENCE, CREATE
 PROCEDURE.

---

## 馃 Autor
**Giovanny Alejandro Tapiero Cata帽o**  
馃搮 2025 鈥?Proyecto educativo y demostrativo con Oracle 19c y Java Swing.

---

### 馃搨 Archivo generado autom谩ticamente por ChatGPT (GPT-5) :. 
