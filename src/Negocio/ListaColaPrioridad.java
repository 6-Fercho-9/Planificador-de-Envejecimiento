package Negocio;

import java.util.ArrayList;
import java.util.List;

public class ListaColaPrioridad {
    private List<ColaPrioridad> listaColaPrioridad;
    private int k=0;
    private static final int COLAS_POR_DEFECTO=3;
    public ListaColaPrioridad(){
        listaColaPrioridad=new ArrayList<>();
    }
    public ListaColaPrioridad(ListaColaPrioridad lista){
        listaColaPrioridad=new ArrayList<>();
        for(ColaPrioridad cola:lista.getListaColaPrioridad()){
            listaColaPrioridad.add(new ColaPrioridad(cola));
        }
    }
    public List<ColaPrioridad> getListaColaPrioridad(){
        return this.listaColaPrioridad;
    }
    public void agregarColaVacia(){
        this.listaColaPrioridad.add(new ColaPrioridad());
    }
    public void agregarCola(ColaPrioridad cola){
        this.listaColaPrioridad.add(cola);
    }
    public void setColaPrioridad(int posicion,ColaPrioridad cola){
        this.listaColaPrioridad.set(posicion,cola);
    }
    public ColaPrioridad getColaPrioridad(int posicion){
        return this.listaColaPrioridad.get(posicion);
    }
    public void eliminarElemento(int posicion){
        this.listaColaPrioridad.remove(posicion);
    }
    public int size(){
        return this.listaColaPrioridad.size();
    }

    public void actualizarPCBEnTodasLasColas(PCB unPCB){
            for(ColaPrioridad cola:this.listaColaPrioridad){;
                int posicionPCB=cola.existePCB(unPCB);
                if(posicionPCB>=0){
                    cola.setearQuantumAProceso(posicionPCB,unPCB.getQuantum());
                }
            }
    }
    /**
     * metodo para eliminar de un final hasta un inicio de la lista de colas
     * sin incluir al inicio
     * @param inicio
     * @param fin
     */
    public void eliminarColas(int inicio,int fin){
        for (int i = fin; i >inicio; i--) {
            this.eliminarElemento(i-1);
        }
    }

    /**
     * metodo para crear n cantidad de colas en la lista
     * sin importar si la lista esta vacia o no
     * @param cantidadColas cantidad de colas a crear
     */
    public void crearColasConProcesos(int cantidadColas){
        int indiceNombreProceso=this.listaColaPrioridad.size();
        for (int i = 0; i < cantidadColas; i++) {
            ColaPrioridad cola=new ColaPrioridad();
            cola.crearProcesosPorDefecto(i);
            this.agregarCola(cola);
            indiceNombreProceso++;
        }
    }

    /**
     * metodo para crear colas con procesos por defecto
     */
    public void crearColasConProcesosPorDefecto(){
        for (int i = 0; i < ListaColaPrioridad.COLAS_POR_DEFECTO; i++) {
            ColaPrioridad cola=new ColaPrioridad();
            cola.crearProcesosPorDefecto(i);
            this.agregarCola(cola);
        }
    }

    /**
     * metodo para cargar prun
     * que nos devuelve un pcb ,solamente decola de la cola que esta en el momento dada
     * por k
     * @return un pcb
     */
    public PCB cargarPRUN(){
        /*if(this.getColaPrioridad(k).estaVacia()){
            k=this.nextCola();
            return null;
        }*/
        return this.getColaPrioridad(k).decolar();
        //k=this.nextCola();//parece que esto va en el dar q
    }

    /**
     * para rotar en la cola con k al ser contador circular
     * @return un valor de k para rotar
     */
    public int nextCola(){
        k++;
        return k%this.size();
    }

    public int getK(){
        return this.k;
    }
    /**
     * metodo para crear un clon dado un pcb
     * usando el constructor de copia
     * asumiendo que al crear una copia quizas no sea clon
     * entonces se debe setear que si es clon por obvias razones
     * retorno esa copia
     * @param pcbOriginal pcboriginal
     *
     * @return
     */
    public PCB crearClon(PCB pcbOriginal){
        PCB clonPCB=new PCB(pcbOriginal);
        clonPCB.setClon(true);
        return clonPCB;
    }

    /**
     * liberar memoria supuestamente
     * @param unPCB un pcb
     */
    public void freeMem(PCB unPCB){
        unPCB=null;
    }

    /**
     * metodo para verificar si estoy en el limite de la lista de colas
     * si la lista tiene 4 colas
     * este metodo retorna true si k esta en el ultimo elemento de la lista,false si no
     * @return true si k esta en el ultimo elemento de la lista,false si no
     */
    public boolean estoyEnElLimite(){
        return k==this.size()-1;
    }

    /**
     * metodo para dar quantum
     * basicamente aca es donde se clona y vuelve a su cola
     * recibe un pcb
     * casoI: y si la cola esta vacia,simplemente que k avanze
     * CasoII: si el quantum no es infinito,quiere decir que algun momento finalizara
     * entonces decrementamos su quantum
     * y si finalizo borramos todos sus pcb y avanzamos
     * CasoIII:
     * si no es clon
     * @param unPCB
     */
    public void darQuantum(PCB unPCB){
        /*por si acaso xD*/
        if(unPCB==null){
            k=this.nextCola();
            return;
        }
        if(!unPCB.esQuantumInfinito()){//si su cantidad de veces para finalizar no es infinito entra
            unPCB.setQuantum(unPCB.getQuantum() - 1);
            this.actualizarPCBEnTodasLasColas(unPCB);
            if(unPCB.finalizo()) {//y si finalizo,elimina todos los pcb donde este
                this.eliminarTodosSusPCBS(unPCB);
                this.freeMem(unPCB);
                k=this.nextCola();
                return;
            }
        }
        //si no se asume que el pcb entrante es de quantum infinito
        //por tanto se asume que nunca acaba
            if (!unPCB.esClon()) {
                //si no es clon de cajon,vuelve a su cola
                this.getColaPrioridad(k).encolar(unPCB);
                //ahora se clonara si y solo si,k es menor al limite
                if (!this.estoyEnElLimite()) {
                    //si no existe clon debajo entonces que encole al clon
                    if(!this.getColaPrioridad(k+1).existeClon(unPCB)){
                        this.getColaPrioridad(k + 1).encolar(this.crearClon(unPCB));
                    }
                }
                //si es igual al limite no hace nada
            } else {//si es clon
                if (!this.estoyEnElLimite()) {//entra aca si no estoy en el limite
                    if(!this.getColaPrioridad(k+1).existeClon(unPCB)){
                        this.getColaPrioridad(k + 1).encolar(unPCB);
                    }else{
                        this.freeMem(unPCB);//solo elimino a ese clon
                    }
                }else//en caso que este en el limite se elimina el pcb
                    this.freeMem(unPCB);//solo elimino a ese clon

            }
        k=this.nextCola();
    }
    public void setK(int k){
        this.k=k;
    }
    /**
     * metodo para borrar todos los pcbs de todas las colas que hay en la lista
     * @param unPCB pcb a eliminar
     */
    public void eliminarTodosSusPCBS(PCB unPCB){
        for(ColaPrioridad cola:this.listaColaPrioridad)
            cola.eliminarPCBDeLaCola2(unPCB);
    }

    public void vaciar(){
        this.listaColaPrioridad.clear();
    }
    @Override
    public String toString(){
        String S="";
        for(ColaPrioridad cola:this.listaColaPrioridad){
            S+= cola.toString()+"\n";
        }
        return S;
    }
    /**
     * para la copia
     */

}
