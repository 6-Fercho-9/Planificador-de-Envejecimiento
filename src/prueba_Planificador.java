import Negocio.*;
public class prueba_Planificador {
    public static void main(String[] args){
        ListaColaPrioridad lista=new ListaColaPrioridad();
        lista.crearColasConProcesosPorDefecto();
        lista.getColaPrioridad(0).setearQuantumAProceso(0,1);
        lista.getColaPrioridad(0).setearQuantumAProceso(1,2);
        for (int i = 0; i < 100; i++) {
            PCB unPCB=lista.cargarPRUN();
            lista.darQuantum(unPCB);
            System.out.println("iteracion: "+i);
            System.out.println(unPCB);
            System.out.println(lista);

        }


    }
}

