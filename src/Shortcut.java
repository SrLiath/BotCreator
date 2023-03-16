import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Shortcut implements NativeKeyListener {

    private static final int KEY_CTRL = NativeKeyEvent.VC_CONTROL;
    private static final int KEY_ALT = NativeKeyEvent.VC_ALT;
    private static final int KEY_B = NativeKeyEvent.VC_B;

    private boolean ctrlPressed = false;
    private boolean altPressed = false;
    private boolean bPressed = false;
//verifica se está pressionado, se estiver executa Bot.jar
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == KEY_CTRL) {
            ctrlPressed = true;
        } else if (e.getKeyCode() == KEY_ALT) {
            altPressed = true;
        } else if (e.getKeyCode() == KEY_B) {
            bPressed = true;
        }

        if (ctrlPressed && altPressed && bPressed) {
            // Execute o arquivo bot.jar
            try {
                Runtime.getRuntime().exec("java -jar Bot.jar");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == KEY_CTRL) {
            ctrlPressed = false;
        } else if (e.getKeyCode() == KEY_ALT) {
            altPressed = false;
        } else if (e.getKeyCode() == KEY_B) {
            bPressed = false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        // Nada aqui caraio
    }

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }

        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        Shortcut listener = new Shortcut();
        GlobalScreen.addNativeKeyListener(listener);

        // teste
        //JFrame frame = new JFrame("Bot");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JPanel panel = new JPanel();
        //JLabel label = new JLabel("O programa está rodando. Pressione CTRL + ALT + B para executar o bot.");
        //panel.add(label);
        //frame.getContentPane().add(panel);
        //frame.pack();
        //frame.setVisible(true);
    }
}
