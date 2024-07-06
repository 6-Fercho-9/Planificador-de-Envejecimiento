package Negocio;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ColaPrioridad {
    //con una cola de objetos pcb
    private  Queue<PCB> colaPrioridad;
    public static final byte PROCESOS_POR_DEFECTO=2;
    private static final String[] NOMBRE_PROCESOS={"V","M","H","K","L","O","F","#","D","W","Y"
            ,"A","S","R","T","=","7","P","U","$"};
    public ColaPrioridad(){
        this.colaPrioridad=new LinkedList<>();
    }

    /**
     * constructor de copia,esto sirve para crear una copiar exacta de un objeto
     * sin embargo al tener una estructura dentro
     * se debe copiar manualmente cada elemento
     * si no,simplemente se haria referencia entre objetos
     * lo cual seria innconsistente
     * @param colaOriginal
     */
    public ColaPrioridad(ColaPrioridad colaOriginal){
        this.colaPrioridad=new LinkedList<>();
        for (PCB elemento: colaOriginal.colaPrioridad){
            this.encolar(new PCB(elemento));
        }
    }
    public void crearProcesosEnLaCola(int posicionNombreProceso,int cantidadDeProcesos){
        for (int i = 0; i <cantidadDeProcesos ; i++) {
            this.encolar(new PCB(ColaPrioridad.NOMBRE_PROCESOS[posicionNombreProceso],i,false ));
        }
    }
    //metodos basicos de la cola
    public boolean estaVacia(){
        return this.colaPrioridad.isEmpty();
    }
    public void encolar(PCB unProceso){
        this.colaPrioridad.offer(unProceso);
    }
    public PCB decolar(){
        return this.colaPrioridad.poll();
    }
    public int size(){
        return this.colaPrioridad.size();
    }
    public PCB peek(){
        return this.colaPrioridad.peek();
    }
    public Queue<PCB> getColaPrioridad(){
        return this.colaPrioridad;
    }

    public void vaciarCola(){
        this.colaPrioridad.clear();
    }
    public void crearProcesosPorDefecto(int posicionDeLaCola){
        for (int i=0;i<ColaPrioridad.PROCESOS_POR_DEFECTO;i++){
            this.encolar(new PCB(ColaPrioridad.NOMBRE_PROCESOS[posicionDeLaCola], i+1,false));//por defecto quantum de infinito
        }
    }
    /**
     * metodo para crear procesos por defecto en la cola
     * @param nombreProceso nombre del proceso,esto me lo da la lista de colas
     */

    public void crearProcesosPorDefecto(String nombreProceso){
        for (int i=0;i<ColaPrioridad.PROCESOS_POR_DEFECTO;i++){
            this.encolar(new PCB(nombreProceso,i+1,false));//por defecto quantum de infinito
        }
    }

    public void crearProcesosPorDefecto(String nombreProceso,int cantidadProcesos){
        for (int i = 0; i < cantidadProcesos; i++) {
            this.encolar(new PCB(nombreProceso,i+1,false));
        }
    }
    public void crearProcesosPorDefecto(int posicionCola,int cantidadProcesos){
        for (int i = 0; i < cantidadProcesos; i++) {
            this.encolar(new PCB(ColaPrioridad.NOMBRE_PROCESOS[posicionCola], i+1,false));
        }
    }
    /**
     *
     * @param unPCB pcb a buscar
     * @return retorna la posicion donde se encuentra ese pcb
     */
    public int existePCB(PCB unPCB){
        List<PCB> lista=(List<PCB>)this.colaPrioridad;
        return lista.indexOf((PCB)unPCB);
    }
    /**
     * metodo para verificar si existe un clon en una cola
     * @param unPCB pcb
     * @return true si existe un clon ,false si no
     */
    public boolean existeClon(PCB unPCB){
        return this.colaPrioridad.contains(unPCB);
    }

    /**
     * setea un quantum a un pcb de una determinada cola
     * @param posicion=posicion donde setear
     * @param quantum=quantum a setear
     */
    public void setearQuantumAProceso(int posicion,double quantum){
        List<PCB> listaPCB=(List<PCB>) this.colaPrioridad;
        listaPCB.get(posicion).setQuantum(quantum);
    }
    /**
     * metodo para eliminar un PCB de la cola
     * de manera manual
     * @param unPCB le mando unPCB
     */
    public void eliminarPCBDeLaCola(PCB unPCB){

        for (int i = 0; i < this.size(); i++) {
            if(this.peek().compareTo(unPCB)==0){//si el elemento en la cola es igual al pcb que quiero eliminar
                this.decolar();
            }else{
                this.encolar(this.decolar());
            }
        }
    }

    /**
     * metodo para eliminar unPCB de la cola,pero usando metodos de java
     * ambos eliminar si no estuviese el pcb en la cola no hacen nada
     * no se cuelga ni nada
     * @param unPCB un pcb a eliminar
     */
    public void eliminarPCBDeLaCola2(PCB unPCB){
        List<PCB> listaPCB= (List<PCB>) this.colaPrioridad;
        listaPCB.remove(unPCB);
    }

    /**
     * metodo para eliminar todos los clones de una cola
     */
    public void eliminarClonesDeTodasLasColas(){
        List<PCB> listaPCB=(List<PCB>) this.colaPrioridad;
        for (int i = 0; i < listaPCB.size(); i++) {
            if(listaPCB.get(i).esClon()){
                listaPCB.remove(i);
            }
        }
    }
    @Override
    public String toString(){
        return this.colaPrioridad.toString();
    }
}
