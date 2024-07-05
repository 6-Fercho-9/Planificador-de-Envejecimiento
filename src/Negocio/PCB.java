package Negocio;

import java.util.Objects;

public class PCB implements Comparable<PCB> {
    private String clave;//clave primaria para comparar
    private int id;//id actua como subindice
    private String nombreProceso;
    private double quantum;//quantum en realidad no es el concepto que conocemos,si no mas bien la cantidad de veces que le falta para finalizar al pcb
    private boolean clon;//actua como superindice
    private static final double INFINITO=Double.POSITIVE_INFINITY;
    //constructor de copia
    public PCB(PCB unPCB){
        this.id=unPCB.id;
        this.nombreProceso=unPCB.nombreProceso;
        this.quantum =unPCB.quantum;
        this.clon=unPCB.clon;
        this.clave= unPCB.clave;
    }
    //constructor de oficio
    public PCB(){};
    //constructor que por defecto mandamos el nombre,id y clon
    //y por defecto nos crea con quantum=infinito es decir nunca finaliza
    //asi mismo crea una clave primaria para el que es el nombre del proceso y su id
    public PCB(String nombreProceso,int id,boolean clon){
        this.nombreProceso=nombreProceso;
        this.id=id;
        this.quantum =PCB.INFINITO;
        this.clon=clon;
        this.clave=this.nombreProceso+this.id;
    }

    public PCB(String nombreProceso,int id,boolean clon,int quantum){
        this.nombreProceso=nombreProceso;
        this.id=id;
        this.quantum =quantum;
        this.clon=clon;
        this.clave=this.nombreProceso+this.id;
    }
    public PCB(String nombreProceso,int id,int quantum){
        this.nombreProceso=nombreProceso;
        this.quantum =quantum;
        this.clave=this.nombreProceso+this.id;
    }
    public PCB(String nombreProceso,int id){
        this.nombreProceso=nombreProceso;
        this.id=id;
        this.clave=this.nombreProceso+this.id;
    }
    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public double getQuantum() {
        return quantum;
    }

    public boolean finalizo(){
        return this.getQuantum()==0;
    }
    public boolean esQuantumInfinito(){
        return this.quantum ==PCB.INFINITO;
    }
    public void setQuantum(double quantum) {
        this.quantum = quantum;
    }

    public boolean esClon() {
        return clon;
    }

    public void setClon(boolean clon) {
        this.clon = clon;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    public String getClave(){
        return this.clave;
    }
    @Override
    public String toString(){
        String cadena = "";
        if(this.clon)
            cadena="*";
        //"₀"
        return this.getNombreProceso()+this.generarSubIndice(this.getId())+cadena;
    }
    public String toStr(){
        String cadena = "";
        if(this.clon)
            cadena="*";
        //"₀"
        return this.getNombreProceso()+this.generarSubIndice(this.getId())+" "+this.getQuantum();
    }

    public String obtenerPCBconSubIndicesYSuperIndices(){
        String cadena = "";

        if(this.clon)
             cadena="*";
        //"₀"
        return this.getNombreProceso()+this.getId()+cadena+" clave: "+this.getClave();
    }
    public String generarSubIndice(int numero){
        String numerosChiquitos="₀₁₂₃₄₅₆₇₈₉";
        String numerocad=String.valueOf(numero);
        String retorno="";
        for (int i = 0; i < numerocad.length(); i++) {
            retorno+=numerosChiquitos.charAt(Character.getNumericValue(numerocad.charAt(i)));
        }
        return retorno;
    }

    //compara en base a su clave que es un String
    @Override
    public int compareTo(PCB o) {
        return this.clave.compareTo(o.getClave());
    }

    //por si requiero usar algun metodo de java que use equals
    @Override
    public boolean equals(Object o) {
        if(o==null)
            return false;
        PCB pcb = (PCB) o;
        return this.compareTo(pcb)==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clave, id, nombreProceso, quantum, clon);
    }
}
