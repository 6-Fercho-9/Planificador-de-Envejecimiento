import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class animacion {
    public static void main(String[] args){
        JPanel contenedor=new JPanel();
        JFrame frame = new JFrame("TextField Animation Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        contenedor.setBounds(0,0,400,200);
        //frame.setBackground(Color.red);
        frame.setLayout(null); // Usando diseño nulo para posicionamiento absoluto
        frame.setContentPane(contenedor);
        contenedor.setBackground(Color.red);
        JLabel originalLabel = new JLabel("x1 + x2 + x3");
        originalLabel.setBounds(50, 50, 200, 30); // Posición inicial
        frame.add(originalLabel);

        JLabel newLabel = new JLabel();//nuveo label para la animacion(?
        newLabel.setBounds(50, 100, 50, 30); // Posición inicial para x1
        frame.add(newLabel);

        JButton button = new JButton("Animate");
        button.setBounds(260, 50, 100, 30);
        frame.add(button);

        button.addActionListener(new ActionListener() {
            //todo esto se creara  cuando se clickee
            private Timer timer;
            private int delay = 10; // milisegundos
            private int step = 2; // píxeles
            private int targetX = 50; // Posición objetivo para x2 + x3
            private int currentX = 100; // Posición inicial de x2 + x3

            @Override
            public void actionPerformed(ActionEvent e) {
                String[] parts = originalLabel.getText().split(" \\+ ");//lo separa para obtener las partes
                if (parts.length >= 3) {
                    String x1 = parts[0];
                    String x2 = parts[1];
                    String x3 = parts[2];

                    newLabel.setText(x1);

                    originalLabel.setText(x2 + " + " + x3);
                    originalLabel.setBounds(currentX, 50, 200, 30); // Ajustar la posición inicial

                    if (timer != null && timer.isRunning()) {
                        timer.stop();
                    }

                    timer = new Timer(delay, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (currentX > targetX) {
                                currentX -= step;
                                originalLabel.setBounds(currentX, 50, 200, 30); // Mover el texto
                            } else {
                                ((Timer) e.getSource()).stop();
                            }
                        }
                    });
                    timer.start();
                } else {
                    JOptionPane.showMessageDialog(frame, "The text field must contain 'x1 + x2 + x3'");
                }
            }
        });

        frame.setVisible(true);
    }
}
