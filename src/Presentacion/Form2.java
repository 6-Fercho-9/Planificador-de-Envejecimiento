package Presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Form2 extends JFrame {
    JLabel lbl1;
    int contador=0;
    Timer t;
    int k= 100;
    ActionListener a;
    JButton btn;
    JButton btn2;
    public Form2(){
        this.setSize(new Dimension(600,400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lbl1=new JLabel("Contador: "+contador);
        btn=new JButton("Stop");
        btn2=new JButton("Start");
        btn.setBounds(50,50,70,40);
        btn2.setBounds(140,50,70,40);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.stop();
            }
        });
        this.add(btn);
        this.add(btn2);
        btn.setVisible(true);
        btn2.setVisible(true);

        this.add(lbl1);
        lbl1.setVisible(true);

        this.agregarActionListener();
        t=new Timer(1000,a);
        t.start();
        this.setVisible(true);
    }
    public void agregarActionListener(){
        this.a=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Form2.this.contador++;
                lbl1.setText("Contador: "+String.valueOf(Form2.this.contador));

            }
        };
    }

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            Form2 Form = new Form2();
            Form.setVisible(true);
        });
    }
}
