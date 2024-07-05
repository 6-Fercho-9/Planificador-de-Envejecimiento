import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class EjemploTimer {
     static int inicio=1;
     static int limite=10;
     static Timer timer;
    public static void main(String[] args){
        timer = new Timer();
        /*timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (inicio == limite) {
                    timer.cancel();
                }
                System.out.println("estoy ejecutando el timer");
                inicio++;
            }
        }, 0, 1000);*/

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (inicio == limite) {
                    timer.cancel();
                }
                System.out.println("pasada: "+inicio);

                inicio++;
            }
        }, 0, 1000);
    }

        //para ejecutar esa tarea




}
