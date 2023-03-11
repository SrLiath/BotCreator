import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClickPosition extends JFrame {
   private static final long serialVersionUID = 1L;
   
   public static void main(String[] args) {
      ClickPosition click = new ClickPosition();
      click.start();
   }

   public void start() {
      // Configurações da janela
      setUndecorated(true);
      setBackground(new Color(0, 0, 0, 100));
      setSize(Toolkit.getDefaultToolkit().getScreenSize());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
      // Cria um novo objeto Robot
      Robot robot = null;
      try {
         robot = new Robot();
      } catch (AWTException e) {
         e.printStackTrace();
      }

      // Adiciona um listener para o evento de clique do mouse
      addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            // Obtém a posição atual do mouse
            Point point = MouseInfo.getPointerInfo().getLocation();
            int x = point.x;
            int y = point.y;

            // Exibe as coordenadas do ponto clicado
            System.out.println("Posição do clique: x=" + x + ", y=" + y);
         }
      });

      // Loop principal do programa
      while (true) {
         // Espera por um curto período de tempo para reduzir a taxa de atualização
         try {
            Thread.sleep(10);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}
