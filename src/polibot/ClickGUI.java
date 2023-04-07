package polibot;
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
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class ClickGUI extends JFrame implements NativeMouseListener, NativeKeyListener {
    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JLabel infoLabel, infoUsuario;
    private Point clickPoint;
    private JButton stopButton;
    private JButton startButton;
    private int lastPressedKey;
    private Usuario nome;

    public ClickGUI() {
    	nome = new Usuario();
    	//pegando o usuario logado no computador pela variavel de ambiente
    	nome.setUsuario(System.getenv("USERNAME"));
    	
    	frame = new JFrame("Click GUI");
    	stopButton = new JButton("Stop");
    	infoLabel = new JLabel("Clique em Start para iniciar");
    	infoUsuario = new JLabel("Olá " + nome.getNome());
    	startButton = new JButton("Start");
        ClickGUI thisNovo = this;
    	
    	frame.setSize(400, 200);
    	frame.setLayout(null);
    	frame.setAlwaysOnTop(true);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	startButton.setBounds(80, 45, 100, 30);
    	stopButton.setBounds(200, 45, 100, 30);
    	infoLabel.setBounds(110, 60, 300, 100);
    	infoUsuario.setBounds(150, 15, 300, 25);
    	
    	frame.add(infoLabel);
    	frame.add(infoUsuario);
    	frame.add(startButton);
    	frame.add(stopButton);
            
    	startButton.addActionListener(new ActionListener() {
	  		public void actionPerformed(ActionEvent e) {
	  			try {
	  				GlobalScreen.registerNativeHook();
	  				GlobalScreen.addNativeMouseListener(thisNovo);
	  				GlobalScreen.addNativeKeyListener(thisNovo);
	  				GlobalScreen.setEventDispatcher(new SwingDispatchService());
	  			} catch (NativeHookException ex) {
	  				System.err.println("Erro ao registrar o mouse e teclado nativo: " + ex.getMessage());
	  				System.exit(1);
	  			}
	  			
	  	}});
    	
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    			try { 
    				// Executa o comando "cmd" para iniciar o prompt de comando
                    Process p = Runtime.getRuntime().exec("compiler.bat");

                    // Aguarda a finaliza��o do processo filho
                    p.waitFor();
                    GlobalScreen.unregisterNativeHook();
                    dispose(); 
                    System.exit(0);
    			} catch (IOException ex) {
                	System.err.println("Erro ao escrever no arquivo Bot.java: " + ex.getMessage());
                } catch (NativeHookException ex) {
                    System.err.println("Erro ao desregistrar o mouse e teclado nativo: " + ex.getMessage());
                } catch (InterruptedException ex) {
					System.out.println("Erro do cmd" + ex.getMessage());
				}
        }});
        
    }  
    	
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
    	File file = new File("Bot.java");
    	if (file.exists()) {   	  		
    	}else {  
    		try {
		        FileWriter writer = new FileWriter(file, true);
		        writer.write("import java.awt.Robot;\r\n"
		        		+ "import java.awt.event.KeyEvent;\r\n"
		        		+ "\r\n"
		        		+ "public class Bot {\r\n"
		        		+ "    public static void main(String[] args) throws Exception {\r\n"
		        		+ "        // cria uma inst�ncia de Robot\r\n"
		        		+ "        Robot bot = new Robot();\n"
		        		+ "bot.setAutoDelay(500);");
		        writer.close();
		        System.out.println("Linha adicionada ao arquivo Bot.java");
    		} catch (IOException ex) {
    			System.err.println("Erro ao adicionar linha ao arquivo Bot.java: " + ex.getMessage());
	    }}
    	
    	String button = "";
    	
    	if(e.getButton() == 1) button = "Botao Esquerdo";
    	else if(e.getButton() == 2) button = "Botao Direito";
    	
    	
    	
    	

        // Armazena as coordenadas do clique do mouse do usu�rio
        clickPoint = e.getPoint();
        // Define a mensagem de informa��o para exibir as coordenadas do clique
        
        infoLabel.setText("Adicionado " + button + " (" + clickPoint.x + ", " + clickPoint.y + ")");
        
        // Salva as coordenadas do clique e as teclas pressionadas em um arquivo de texto
        try {
            FileWriter writer = new FileWriter("Bot.java", true);
            writer.write("bot.mouseMove("+ clickPoint.x + "," + clickPoint.y +");\r\n"
            		+ "bot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);\r\n"
            		+ "bot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);\r\n");
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
	        		+ "        // cria uma inst�ncia de Robot\r\n"
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
        	
        	System.out.println(Key);
        	//Corre��o de comandos para o bot
        	if (Key == "Enter") Key = "ENTER";
        	else if(Key == "Space") Key = "SPACE";
        	else if(Key == "Backspace") Key = "BACK_SPACE";
        	else if(Key == "Guia") Key = "TAB";
        	else if(Key == "Caps Lock") Key = "CAPS_LOCK";
        	else if(Key == "Escape") Key = "ESCAPE";
        	else if(Key == "Ctrl") Key = "CONTROL";
        	else if(Key == "Alt") Key = "ALT";
        	else if(Key == "Shift") Key = "SHIFT";
        	else if(Key == "Tab") Key = "TAB";
        	else if(Key == "Period") Key = "PERIOD";
        	else if(Key == "Back Quote") Key = "BACK_QUOTE";
        	//else if(Key =="Meta") {Key = "VK_CONTROL | KeyEvent.VK_ESCAPE";}
            FileWriter writer = new FileWriter("Bot.java", true);
            writer.write("bot.keyPress(KeyEvent.VK_" + Key + ");\n"
            		+ "bot.keyRelease(KeyEvent.VK_"+  Key + ");\n");
            infoLabel.setText("Adicionado: "+ NativeKeyEvent.getKeyText(lastPressedKey));
            writer.close();
            System.out.println("Tecla pressionada salva em Bot.java" + NativeKeyEvent.getKeyText(lastPressedKey));
            
        } catch (IOException ex) {
            System.err.println("Erro ao salvar a tecla pressionada: " + ex.getMessage());
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        // Armazena as coordenadas do clique do mouse do usu�rio
        clickPoint = e.getPoint();
       
        // Define a mensagem de informa��o para exibir as coordenadas do clique
        infoLabel.setText("Adicionado (" + clickPoint.x + ", " + clickPoint.y + ")");
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // Define o cursor do mouse padr�o
        setCursor(Cursor.getDefaultCursor());
    }

    
}