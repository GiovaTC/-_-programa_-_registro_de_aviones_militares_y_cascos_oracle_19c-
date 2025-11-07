import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AvionesGUI extends JFrame {
    private JTextArea area;
    private AvionDAO dao = new AvionDAO();
    public AvionesGUI() {
        setTitle("Registro de Aviones y Cascos - oracle 19 c");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        area = new JTextArea();
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel panelBotones = new JPanel();
        JButton btnListar = new JButton("cargar / refrescar");
        JButton btnInsertar = new JButton("insertar 15 ejemplos (sp)");
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