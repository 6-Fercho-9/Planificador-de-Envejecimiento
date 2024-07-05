import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class animacion2 {
    public static void main(String[] args){

            JFrame frame = new JFrame("Animación de Texto");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            JLabel label = new JLabel("A B C");
            frame.getContentPane().add(label);
            frame.setVisible(true);

            Timer timer = new Timer(1000, new ActionListener() {


                String texto = "A B C";
                int index = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (index < texto.length()) {
                        texto = texto.substring(1); // Elimina el primer carácter
                        label.setText(texto);
                        label.repaint();
                        index++;
                    } else {
                        ((Timer) e.getSource()).stop(); // Detiene el timer cuando termina la animación
                    }
                }
            });

            timer.start();
        }

}
