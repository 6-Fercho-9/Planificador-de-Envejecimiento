import Negocio.ColaPrioridad;
import Negocio.ListaColaPrioridad;
import Negocio.PCB;
import Presentacion.Form;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.util.TimerTask;

public class pruebas {
    public static void main(String[]  args){
        ColaPrioridad cola=new ColaPrioridad();
        cola.crearProcesosPorDefecto("P");
        ColaPrioridad cola2=new ColaPrioridad();
        cola2.crearProcesosPorDefecto("K");
        ColaPrioridad cola3=new ColaPrioridad();
        cola3.crearProcesosPorDefecto("L");
        ListaColaPrioridad listaCola=new ListaColaPrioridad();
        listaCola.crearColasConProcesosPorDefecto();
        ListaColaPrioridad listaCopia=new ListaColaPrioridad(listaCola);
        int limite=5;
        ActionListener al=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("e");
            }
        };
        Timer timer=new Timer(1000,al);
        //timer.start();
        /*TimerTask tarea=new TimerTask() {
            int a=1;
            @Override
            public void run() {
                a++;
                if(a==limite){
                    timer.stop();
                }
                try{
                 PCB pcb=listaCola.cargarPRUN();
                 listaCola.darQuantum(pcb);
                    System.out.println(listaCola);
                }catch(Exception E){
                    System.out.println("error: "+E.toString());
                }
                //System.out.println("me estoy ejecutando");
            }
        };
        timer.start();*/
        /*for (int i = 0; i < 5; i++) {
            ColaPrioridad cola=new ColaPrioridad();
            cola.crearProcesosPorDefecto(procesosConstantes[i]);//crea procesos en la cola
            this.listaColaPrioridad.add(cola);
        }*/

    }
}
