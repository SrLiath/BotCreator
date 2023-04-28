import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class ClickGUI extends JFrame implements NativeMouseListener {

    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JButton clickButton;
    private JLabel infoLabel;
    private Point clickPoint;

    public ClickGUI() {
        setTitle("Click GUI");
        setSize(100, 200);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        clickButton = new JButton("Click");
        clickButton.addActionListener(new ClickButtonListener());
        infoLabel = new JLabel("Clique na onde deseja adicionar o comando");

        panel.add(clickButton);
        panel.add(infoLabel);
        add(panel);

        setVisible(true);

        // Registra este objeto como um ouvinte do mouse nativo
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
        } catch (NativeHookException ex) {
            System.err.println("Erro ao registrar o mouse nativo: " + ex.getMessage());
            System.exit(1);
        }
    }

    private class ClickButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Salva as coordenadas do clique em um arquivo de texto
            try {
                FileWriter writer = new FileWriter("click-coordinates.txt");
                writer.write(clickPoint.x + "," + clickPoint.y);
                writer.close();
                System.out.println("Coordenadas salvas em click-coordinates.txt");
            } catch (IOException ex) {
                System.err.println("Erro ao salvar as coordenadas do clique: " + ex.getMessage());
            }
        }
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // Armazena as coordenadas do clique do mouse do usuário
        clickPoint = e.getPoint();
        // Define a mensagem de informação para exibir as coordenadas do clique
        infoLabel.setText("Clique registrado em (" + clickPoint.x + ", " + clickPoint.y + ")");
    }

    // Métodos desnecessários para este exemplo, mas precisam ser implementados para a interface NativeMouseListener
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // Define o cursor do mouse como um "+"
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(getClass().getResource("/images/plus.png")).getImage(),
                new Point(0, 0), "plus"));
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // Define o cursor do mouse padrão
        setCursor(Cursor.getDefaultCursor());
    }

    public static void main(String[] args) {
        new ClickGUI();
    }
}