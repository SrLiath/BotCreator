import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class ClickGUI extends JFrame implements NativeMouseListener, NativeKeyListener {
    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JLabel infoLabel;
    private Point clickPoint;
    private int lastPressedKey;

    public ClickGUI() {
        setTitle("Click GUI");
        setSize(400, 100);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        infoLabel = new JLabel("Clique na onde deseja adicionar o comando");

        panel.add(infoLabel);
        add(panel);

        setVisible(true);
        JButton stopButton = new JButton("Stop");
        panel.add(stopButton);
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { 
                	try {
                		// Executa o comando "cmd" para iniciar o prompt de comando
                        Process p = Runtime.getRuntime().exec("compiler.bat");

                        // Aguarda a finalização do processo filho
                        p.waitFor();
                	} catch (IOException ex) {
                	    System.err.println("Erro ao escrever no arquivo Bot.java: " + ex.getMessage());
                	}catch (InterruptedException w) { System.err.println("Erro ao escrever no arquivo Bot.java: ");}

                    GlobalScreen.unregisterNativeHook();
                    dispose(); // fecha a janela principal
                    System.exit(0); // encerra o programa
                } catch (NativeHookException ex) {
                    System.err.println("Erro ao desregistrar o mouse e teclado nativo: " + ex.getMessage());
                }
            }
        });

        
        // Registra este objeto como um ouvinte do mouse e teclado nativo
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
        } catch (NativeHookException ex) {
            System.err.println("Erro ao registrar o mouse e teclado nativo: " + ex.getMessage());
            System.exit(1);
        }
    }
    
    
    
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    	File file = new File("Bot.java");
    	if (file.exists()) {
    	  
    	}else {  try {
	        FileWriter writer = new FileWriter(file, true);
	        writer.write("import java.awt.Robot;\r\n"
	        		+ "import java.awt.event.KeyEvent;\r\n"
	        		+ "\r\n"
	        		+ "public class Bot {\r\n"
	        		+ "    public static void main(String[] args) throws Exception {\r\n"
	        		+ "        // cria uma instância de Robot\r\n"
	        		+ "        Robot bot = new Robot();\n");
	        writer.close();
	        System.out.println("Linha adicionada ao arquivo Bot.java");
	    } catch (IOException ex) {
	        System.err.println("Erro ao adicionar linha ao arquivo Bot.java: " + ex.getMessage());
	    }}

        // Armazena as coordenadas do clique do mouse do usuário
        clickPoint = e.getPoint();
        // Define a mensagem de informação para exibir as coordenadas do clique
        infoLabel.setText("Adicionado (" + clickPoint.x + ", " + clickPoint.y + ")");
        // Salva as coordenadas do clique e as teclas pressionadas em um arquivo de texto
        try {
            FileWriter writer = new FileWriter("Bot.java", true);
            writer.write("clicou com o botão esquerdo");
            writer.close();
            System.out.println("Coordenadas e teclas salvas em Bot.java");
        } catch (IOException ex) {
            System.err.println("Erro ao salvar as coordenadas e teclas do clique: " + ex.getMessage());
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
       	File file = new File("Bot.java");
    	if (file.exists()) {
    	  
    	}else {  try {
	        FileWriter writer = new FileWriter(file, true);
	        writer.write("import java.awt.Robot;\r\n"
	        		+ "import java.awt.event.KeyEvent;\r\n"
	        		+ "\r\n"
	        		+ "public class Bot {\r\n"
	        		+ "    public static void main(String[] args) throws Exception {\r\n"
	        		+ "        // cria uma instância de Robot\r\n"
	        		+ "        Robot bot = new Robot();\n");
	        writer.close();
	        System.out.println("Linha adicionada ao arquivo Bot.java");
	    } catch (IOException ex) {
	        System.err.println("Erro ao adicionar linha ao arquivo Bot.java: " + ex.getMessage());
	    }}
        // Armazena a tecla pressionada
        lastPressedKey = e.getKeyCode();
        // Salva as coordenadas do clique e a tecla pressionada em um arquivo de texto
        try {
        	String Key = NativeKeyEvent.getKeyText(lastPressedKey);
        	//Correção de comandos para o bot
        	if (Key == "Enter") {Key = "ENTER";}else if(Key == "Espaço") {Key = "SPACE";}else if(Key == "Backspace") {Key = "BACK_SPACE";}else if(Key == "Guia") {Key = "TAB";} else if(Key == "Caps Lock") {Key = "CAPS_LOCK";}else if(Key == "Escape") {Key = "ESCAPE";}else if (Key == "Ctrl") {Key = "CONTROL";}else if(Key == "Alt") {Key = "ALT";}else if(Key == "Shift") {Key = "SHIFT";}
        	//else if(Key =="Meta") {Key = "VK_CONTROL | KeyEvent.VK_ESCAPE";}
        	
            FileWriter writer = new FileWriter("Bot.java", true);
            writer.write("bot.keyPress(KeyEvent.VK_" + Key + ");\n"
            		+ "bot.keyRelease(KeyEvent.VK_"+  Key + ");\n"
            		+ "try {\r\n"
            		+ "    Thread.sleep(100); \r\n"
            		+ "} catch (InterruptedException e) {\r\n"
            		+ "}\n");
            infoLabel.setText("Adicionado: "+ NativeKeyEvent.getKeyText(lastPressedKey));
            writer.close();
            System.out.println("Tecla pressionada salva em Bot.java" + NativeKeyEvent.getKeyText(lastPressedKey));
        } catch (IOException ex) {
            System.err.println("Erro ao salvar a tecla pressionada: " + ex.getMessage());
        }
    }


    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // Armazena as coordenadas do clique do mouse do usuário
        clickPoint = e.getPoint();
        // Define a mensagem de informação para exibir as coordenadas do clique
        infoLabel.setText("Adicionado (" + clickPoint.x + ", " + clickPoint.y + ")");
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