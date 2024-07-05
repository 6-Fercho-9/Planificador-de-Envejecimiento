import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;


//@SuppressWarnings("serial");
public class EjemploFrame extends JFrame {

    public static int MAX_ITE = 100000;

    private JTextField jtfInformacion = new JTextField();//textfield para mostrar
    private JButton jbIncrementarSinSwingWorker = new JButton();//boton 1
    private JButton jbIncrementarConSwingWorker = new JButton();//boton 2
    private JPanel jpMarco = new JPanel();//panel para el frame
    private JPanel jpAccciones = new JPanel();//panel para botones

    public EjemploFrame(){
        initComponents();
    }

    private void initComponents(){
        jpMarco.setLayout(new BorderLayout());
        jpMarco.add(jtfInformacion, BorderLayout.NORTH);

        //boton para agregar un evento setAction
        jbIncrementarSinSwingWorker.setAction(new IncrementarSinSwingWorkerAction(this));
        jbIncrementarSinSwingWorker.setText("Incrementar sin swing worker");
        jbIncrementarConSwingWorker.setAction(new IncrementarConSwingWorkerAction(this));
        jbIncrementarConSwingWorker.setText("Incrementar con swing worker");

        jpAccciones.add(jbIncrementarSinSwingWorker);
        jpAccciones.add(jbIncrementarConSwingWorker);

        jpMarco.add(jpAccciones, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        getContentPane().add(jpMarco);

        this.setTitle("Ejemplo funcionamiento Swing Worker");

        pack();
    }

    public JTextField getTextField(){
        return this.jtfInformacion;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new EjemploFrame().setVisible(true);
            }
        });
    }
}

@SuppressWarnings("serial")
 class IncrementarSinSwingWorkerAction extends AbstractAction{

    private EjemploFrame ejemploFrame;

    public IncrementarSinSwingWorkerAction(EjemploFrame ejemploSinSwingWorkerFrame){
        this.ejemploFrame = ejemploSinSwingWorkerFrame;
    }

    public void actionPerformed(ActionEvent arg0) {
        int ite = 0;
        while (ite < EjemploFrame.MAX_ITE){
            ite = ite + 1;
            this.ejemploFrame.getTextField().setText("" + ite);
        }

    }

}
class IncrementarConSwingWorkerAction extends AbstractAction{

    private EjemploFrame ejemploFrame;

    public IncrementarConSwingWorkerAction(EjemploFrame ejemploSinSwingWorkerFrame){
        this.ejemploFrame = ejemploSinSwingWorkerFrame;
    }

    public void actionPerformed(ActionEvent arg0) {
        final SwingWorker worker = new SwingWorker(){

            @Override
            protected Object doInBackground() throws Exception {
                int ite = 0;
                while (ite < EjemploFrame.MAX_ITE){
                    ite = ite + 1;
                    ejemploFrame.getTextField().setText("" + ite);
                }
                return null;
            }
        };
        worker.execute();
    }
}