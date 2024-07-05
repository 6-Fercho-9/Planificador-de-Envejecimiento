package Presentacion;

import Negocio.ListaColaPrioridad;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 *
 * clase para no bloquear los botones al momento
 * de ejecutar el automatico
 *
 * ademas de que esta clase es como un action listener
 * directamente lo podemos implementar en un evento
 *
 */
public class ProcesoAsincrono extends SwingWorker<Void,Void> {
    private Runnable tarea;

    public ProcesoAsincrono(Runnable tarea){
        this.tarea=tarea;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Thread.sleep(1000);
        return null;
    }
    @Override
    public void done(){
        if(tarea!=null){
            tarea.run();
        }
    }
}
