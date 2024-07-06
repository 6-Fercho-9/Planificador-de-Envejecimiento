package Presentacion;

import BotonesModificados.MyButton;
import Negocio.ListaColaPrioridad;
import Negocio.PCB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


public class Form extends JFrame {
    ListaColaPrioridad listaColaPrioridad;
    ListaColaPrioridad copiaListaColaPrioridad;
    PCB pcbUniversal;
    List<JLabel> listaLabel;//lista de label que parece ecuaciones
    List<JLabel> listalabelColas;//lista label de colas que son constantes
    int altoFrame=650;
    int anchoFrame=1000;
    int posicionXForm;
    int posicionYForm;
    int posicionXLabel;
    int posicionYlabel;
    int posicionFlechaX;
    int posicionFlechaY;
    int posicionYLabelEcu;
    MyButton btnCargarPRUN;
    MyButton btndarQ;
    MyButton btnAutomatico;
    MyButton btnReiniciar;
    JButton btnConfiguracion;
    JPanel panelContenedorFrame;

    private static final byte CANTIDAD_COLAS_MAXIMAS=10;
    private static final byte CANTIDAD_MAXIMA_PROCESOS=20;
    private static final Color COLOR=Color.black;//para letra negra
    private static final Color COLORBACKGROUND=Color.WHITE;
    //amarillo= new Color(228,228,54);
    private static final Color COLOR_ECUACIONES=new Color(30,28,53);//amarillo era para las ecuaciones
    private static final Color COLOR_Q=new Color(33,150,243);//celeste para Las Q[i]
    private static final Font FUENTE=new Font("DejaVu Sans",Font.PLAIN,18);
    private static final Font FUENTE_Q=new Font("DejaVu Sans",Font.BOLD,18);

    JLabel Flecha;//para la flecha
    JLabel labelPrun;
    JLabel labelProceso;
    int k;//cargado por defecto para moverme en las colas
    int anchoLabelProceso=60;
    int altoLabelProceso=60;
    JMenuBar barraMenu;
    JMenu setup;
    JMenuItem itemAgregarCantidadColas;
    JMenuItem itemMostrarHistorial;
    private static final int ANCHO_LABEL=8000;
    String historial;
    //Timer myTimer;
    JPanel panelMostradorProcesos;
    JScrollPane scrollpaneFrame;
    JLabel labelheader;
    JPanel panelTitulo;

    JPanel panelParaOptionPane;
    JTextField txtNombreProceso;
    JTextField txtCantidadParaFinalizar;

    JPanel panelRadios;
    ButtonGroup grupoDeRadios;
    JRadioButton rdOpcionAgregarProcesos;
    JRadioButton rdOpcionDarleQParaFinalizar;
    JLabel labelMostrarCantidadColasPanel1;
    JLabel labelMostrarCantidadColasPanel2;
    JLabel labelMostrarCantidadColasPanel3;
    JPanel panelCantidadProcesos;
    JTextField txtCantidadProcesos;
    Timer timer;//timer para usarlo para pausarlo o demas
    ActionListener actionUniversal;//evento universal para el timer
    boolean estaEnModoTimer=false;//esto sirve para que el boton sepa si es play,pause o automatico
    boolean activeCargarPRUN =false;//
    boolean existeClones=false;
    private static final String TEXTO_PLAY="Play";
    private static final String TEXTO_PAUSE="Pause";
    private static final String TEXTO_AUTOMATICO="Automatico";
        public Form(){
            listaLabel=new ArrayList<>();
            listalabelColas=new ArrayList<>();
            btndarQ=new MyButton();
            btnReiniciar=new MyButton();
            btnAutomatico=new MyButton();
            btnCargarPRUN=new MyButton();
            btndarQ.setEnabled(false);
            listaColaPrioridad=new ListaColaPrioridad();
            pcbUniversal=new PCB();
            this.setTitle("Planificador Por Envejecimiento");//titulo del frame
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(anchoFrame, altoFrame);
            setLocationRelativeTo(null);
            setLayout(null); // layout del frame nulo o absoluto
            posicionXForm=this.getX();
            posicionYForm=this.getY();
            posicionXLabel=anchoFrame/2-200;
            posicionYlabel=80;//se mantendra constante
            posicionYLabelEcu=posicionYlabel;//sera variable para mover la flecha

            panelContenedorFrame=new JPanel();
            panelContenedorFrame.setLayout(null);//panel que contiene al form con layout nulo
            panelContenedorFrame.setBackground(Form.COLORBACKGROUND);//color blanco de fondo
            panelContenedorFrame.setPreferredSize(new Dimension(Form.ANCHO_LABEL,altoFrame));
            this.instanciarheader();//instancio el panel con el label que actua como header
            /**
             * instancio las lista colas a nivel backend
             */
            this.listaColaPrioridad.crearColasConProcesosPorDefecto();
            this.copiaListaColaPrioridad=new ListaColaPrioridad(listaColaPrioridad);//instancio la copia
            this.copiaListaColaPrioridad.setK(this.listaColaPrioridad.getK());//doi la copia de k
            /**
             * a nivel front
             */
            this.crearLabelPorDefecto(0,this.listaColaPrioridad.size());
            this.instanciarBotones();
            this.instanciarLabelPRUN();
            this.agregarEventoAlosBotonesPrincipales();
            this.crearLabelFlecha();
            this.instanciarMenu();
            //instancio 3 jpanel para mostrarlo en option pane cuando presione label
            this.instanciarPanelRadios();
            this.instanciarPanelParaOptionPane();
            this.instanciarInputCantidadProcesos();
            //evento para activar lo option pane
            this.crearEventoParaIniciarTimer();
            scrollpaneFrame=new JScrollPane(panelContenedorFrame);
            scrollpaneFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollpaneFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

            this.setContentPane(scrollpaneFrame);//le digo al form que su contenedor es un scroll panel
            this.setResizable(false);
            this.setVisible(true);
        }

    /*
     * //////////////////////////////////////////////////////////////////////////////////////
     *                          METODO CREAR LABEL POR DEFECTO
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO CREAR LABEL POR DEFECTO
     * metodo para crear label por defecto
     * este metodo crea los suspuestos pcbs y tambien crea
     * los label Q[1],Q[2]...
     * asimismo se lo manda a sus respectivas listas los elementos creados
     * @param indicePorDondeEmpezar indice de la lista por donde queremos empezar a crear los label
     * @param cantidadACrear cantidad de label a crear
     *
     *                       puede que haya que modificar
     */
    public void crearLabelPorDefecto(int indicePorDondeEmpezar,int cantidadACrear){
        String cadena="";
        String Q="";
        int posicionXRef=this.posicionXLabel;
        int posicionYRef=this.posicionYlabel;

        for (int i = 0; i < cantidadACrear; i++) {
            cadena="";
            Q="Q[";
            for (PCB elemento:this.listaColaPrioridad.getColaPrioridad(indicePorDondeEmpezar).getColaPrioridad()){
                cadena=cadena+elemento.toString()+"     ";
            }
            Q+=(indicePorDondeEmpezar+1)+"]";
            cadena=cadena.trim();
            JLabel nuevoLabel=new JLabel(cadena);
            JLabel labelCola=new JLabel(Q);
            labelCola.setFont(Form.FUENTE_Q);
            labelCola.setBounds(posicionXRef-50,posicionYRef,60,30);
            labelCola.setForeground(Form.COLOR_Q);
            nuevoLabel.setForeground(Form.COLOR_ECUACIONES);
            nuevoLabel.setFont(Form.FUENTE);
            //ancho label antes this.anchoFrame-this.posicionXLabel
            //su alto del label es 30 y ancho por defecto 8k
            nuevoLabel.setBounds(posicionXRef,posicionYRef,Form.ANCHO_LABEL,30);
            nuevoLabel.setPreferredSize(new Dimension(Form.ANCHO_LABEL,30));
            nuevoLabel.setName(String.valueOf(indicePorDondeEmpezar+1));//por defecto tendra un numero asociado como nombre;
            this.agregarEventoALabel(nuevoLabel);
            this.panelContenedorFrame.add(labelCola);//antes lo agregaba directo al frame
            this.panelContenedorFrame.add(nuevoLabel);//antes lo agregaba directo al frame
            labelCola.setVisible(true);
            nuevoLabel.setVisible(true);
            this.listaLabel.add(nuevoLabel);
            this.listalabelColas.add(labelCola);
            posicionYRef+=40;
            indicePorDondeEmpezar++;
        }
        //este parece innecesario
        //this.posicionYLabelEcu=posicionYRef;//para saber donde esta el ultimo Label
    }


    /*
      //////////////////////////////////////////////////////////////////////////////////////
                                 METODO INSTANCIAR HEADER
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL INSTANCIAR HEADER
     * SE ENCARGA DE CREAR UN PANEL CON UN LABEL DENTRO QUE HARA UNA SIMULACION DE UN HEADER
     * QUE DIRA PROYECTO DE SO1 MAS NADA
     */
    public void instanciarheader(){
            panelTitulo=new JPanel();//un panel para el titulo,aunque no es necesario ya parece
            panelTitulo.setBounds(0,0,anchoFrame,30);//dimensiones y posicion en la cabeza del frame(header)
            panelTitulo.setLayout(null);
            labelheader = new JLabel("Proyecto Sistemas Operativos I");
            labelheader.setHorizontalAlignment(SwingConstants.CENTER);
            labelheader.setBounds(0,0,anchoFrame,30);
            labelheader.setFont(Form.FUENTE_Q);
            labelheader.setForeground(Color.white);
            panelTitulo.setBackground(Form.COLOR_Q);
            panelTitulo.add(labelheader);
            this.panelContenedorFrame.add(panelTitulo);
            labelheader.setVisible(true);
        }



    /*
     * //////////////////////////////////////////////////////////////////////////////////////
     * *                          METODO INSTANCIAR BOTONES POR DEFECTO
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO INSTANCIAR LOS BOTONES
     * instanciar los botones
     * sin los eventos
     */
    public void instanciarBotones(){
        btndarQ.setText("Dar Q");
        btnReiniciar.setText("Reiniciar");
        btnAutomatico.setText("Automatico");
        btnCargarPRUN.setText("Cargar PRUN");
        MyButton[] vectorMyButton={this.btndarQ,this.btnCargarPRUN,this.btnAutomatico,this.btnReiniciar};
        int posXRef=this.anchoFrame/4-70;
        //int posYRef=Form.COLAS_POR_DEFECTO<=5?this.posicionLabelYPorDefectoMenorA5:this.posicionYlabel;
        int posYRef=altoFrame-150;
        for(MyButton elemento:vectorMyButton) {
            elemento.setBounds(posXRef - 40, posYRef, 140, 30);
            //las siguientes 2 instrucciones es para quitar una especie de borde que le da el jetbrains por defeto
            elemento.setFocusPainted(false);
            elemento.setBorderPainted(false);
            this.panelContenedorFrame.add(elemento);
            posXRef += 200;
        }
    }


    /*
      //////////////////////////////////////////////////////////////////////////////////////
                               METODO INSTANCIAR LABEL PRUN
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO INSTANCIAR LABEL PRUN
     * instanciar el labelPRUN que es el mostrador
     * para mostrar los procesos que salen de cada cola
     * ACA SE INSTANCIA EL LABEL DONDE SE MUESTRA EL PROCESOS Y OTRO LABEL DONDE DICE PRUN
     */
    public void instanciarLabelPRUN(){
        labelProceso=new JLabel();
        labelPrun=new JLabel("PRUN");
        int x=anchoFrame/10;
        this.labelProceso.setBounds(x-5,this.altoFrame/3,120,120);
        this.labelProceso.setFont(new Font("DejaVu Sans",Font.BOLD,60));
        this.labelProceso.setForeground(Form.COLOR_Q);
        this.panelContenedorFrame.add(labelProceso);
        this.labelProceso.setVisible(true);

        this.labelPrun.setBounds(x-10,this.altoFrame/2,this.anchoLabelProceso+20,40);
        this.labelPrun.setFont(new Font("DejaVu Sans",Font.BOLD,28));
        this.labelPrun.setForeground(Form.COLOR_Q);
        this.panelContenedorFrame.add(labelPrun);
        this.labelPrun.setVisible(true);
    }


    /*
     //////////////////////////////////////////////////////////////////////////////////////
                       METODO AGREGAR EVENTOS A LOS BOTONES PRINCIPALES
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO AGREGAR EVENTOS A BOTONES PRINCIPALES
     * BASICAMENTE UNA VEZ YA CREADOS LOS BOTONES
     * SE DEBE AGREGAR LOS EVENTOS QUE ESTOS TENDRAN
     * QUE SON BASICAMENTE CUANDO UNO "CLICKEA"
     */
    public void agregarEventoAlosBotonesPrincipales(){
         this.agregarEventobtnAutomatico();
         this.agregarEventobtndarQ();
         this.agregarEventobtnReiniciar();
         this.agregarEventobtnCargarPRUN();
     }


    /*
     * //////////////////////////////////////////////////////////////////////////////////////
                               METODO CREAR LABEL FLECHA
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO CREAR LABEL FLECHA
     * metodo para crear el label flecha
     * para moverlo cuando se de darQ
     */
    public void crearLabelFlecha(){
        this.Flecha=new JLabel("→");
        this.Flecha.setBounds(this.posicionXLabel-100,this.posicionYlabel,100,30);
        this.Flecha.setFont(new Font("DejaVu Sans",Font.BOLD,30));
        this.Flecha.setForeground(Form.COLOR_Q);
        this.posicionFlechaX=this.Flecha.getX();
        this.posicionFlechaY=this.Flecha.getY();
        this.panelContenedorFrame.add(Flecha);
        Flecha.setVisible(true);
    }


    /*
     * //////////////////////////////////////////////////////////////////////////////////////
                               METODO INSTANCIAR MENU
     //////////////////////////////////////////////////////////////////////////////////////
     */

    /**CONTEXTO METODO INSTANCIAR MENU
     * metodo para instanciar el Jmenu y JMenu bar al formulario
     * incluye la instancia de eventos de las opciones del menu
     * los menu van directo al frame,pero lo demas debe ir al contenedor panel
     * ADEMAS DE QUE YA SE INSTANCIAN CON SUS EVENTOS
     */
    public void instanciarMenu(){
        barraMenu=new JMenuBar();
        barraMenu.setBackground(Color.WHITE);
        setup=new JMenu("Setup");
        itemAgregarCantidadColas=new JMenuItem("Agregar Cantidad De Colas");
        itemMostrarHistorial=new JMenuItem("Mostrar Historial");
        barraMenu.add(setup);
        setup.add(itemAgregarCantidadColas);
        //setup.add(itemMostrarHistorial);
        this.agregarEventoCantidadColas();
       // this.agregarEventoMostrarHistorial();
        this.setJMenuBar(barraMenu);

    }


    /*
     //////////////////////////////////////////////////////////////////////////////////////
                                  METODO INSTANCIR PANEL RADIOS
     //////////////////////////////////////////////////////////////////////////////////////
     */

    /**CONTEXTO DEL METODO INSTANCIAR PANEL RADIOS
     * el gridBagLayout es como una matriz dinamica
     * donde le agregamos los componetentes en sus coordenadas
     * si tenemos una matriz 3x2
     *  0  1  2  ->las columnas son X
     * 0[][][]
     * 1[][][]
     * y las filas son Y
     * parece elmundo al revez,pero es asi
     * ahora puedes decirle a un componente digamos que se inserte en 0,0
     * pero que ocupe 3 o 2 columnas de la matriz
     * asimismo con las filas
     * cuando usas estos layout ya saben donde insertarlo
     * solamente es configurarlo manualmente en cuanto a posicion de la matriz
     * en este caso usaremos
     * una matriz
     *1 label
     * 2 radios
     * y dejaremos un campo para el aceptar y cancelar que proporciona el joption
     * en total 4 filas y 2 columnas
     * label  []
     * radio  []
     * radio  []
     * [aceptar] [continuar]
     * los [] son imaginarios
     * instancia el panel para los radios
     * solamente instancia el nuevo panel con los radios
     * de seleccion si son de agrar procesos o dar q para finalizar
     */
    public void instanciarPanelRadios(){
        this.panelRadios=new JPanel(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5,5,5,5);
        this.labelMostrarCantidadColasPanel1 =new JLabel();//label paramostrar en que cola esta
        this.grupoDeRadios=new ButtonGroup();
        this.rdOpcionDarleQParaFinalizar=new JRadioButton("Dar Q para Finalizar");
        this.rdOpcionAgregarProcesos=new JRadioButton("Agregar Procesos");
        //los agrego al grupo de radio button
        //para que solo sea de una seleccion
        grupoDeRadios.add(rdOpcionDarleQParaFinalizar);
        grupoDeRadios.add(rdOpcionAgregarProcesos);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;//con esto le diremos que ocupe 2 columnas
        panelRadios.add(labelMostrarCantidadColasPanel1,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=2;//con esto el radio ocupa 2
        panelRadios.add(rdOpcionAgregarProcesos,gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=2;//con esto el radio ocupa 2
        panelRadios.add(rdOpcionDarleQParaFinalizar,gbc);
    }

    /*

      //////////////////////////////////////////////////////////////////////////////////////
                            METODO INSTANCIAR PANEL PARA OPTION PANE
     //////////////////////////////////////////////////////////////////////////////////////

     */
    /**CONTEXTO DEL METODO INSTANCIAR PANEL PARA OPTION PANE
     * con esto tendremos un panel que sera mostrado en un JOptionPane
     * para modificar un proceso y decirle cuando finaliza
     *
     * este metodo se encarga de crear un panel con
     * 2 textfield
     * 1 textfield es para poner el nombre del proceso
     * y el otro textField es para poner cuando finalizara el proceso
     * posterior a eso se usa un gridbag layout
     * para que se ajusten mejor los componentes
     */
    public void instanciarPanelParaOptionPane(){
        this.panelParaOptionPane=new JPanel(new GridBagLayout());
        this.txtNombreProceso=new JTextField();
        txtNombreProceso.setPreferredSize(new Dimension(90,20));
        this.txtCantidadParaFinalizar=new JTextField();
        this.labelMostrarCantidadColasPanel3=new JLabel();
        txtCantidadParaFinalizar.setPreferredSize(new Dimension(90,20));
        JLabel labelNombreProceso=new JLabel("Nombre Proceso: ");
        JLabel labelCantidadParaFinalizar=new JLabel("Cantidad Para Finalizar: ");
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5,5,5,5);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        panelParaOptionPane.add(this.labelMostrarCantidadColasPanel3,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        panelParaOptionPane.add(labelNombreProceso,gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridwidth=1;
        panelParaOptionPane.add(txtNombreProceso,gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.gridwidth=1;
        panelParaOptionPane.add(labelCantidadParaFinalizar,gbc);
        gbc.gridx=1;
        gbc.gridy=2;
        gbc.gridwidth=1;
        panelParaOptionPane.add(txtCantidadParaFinalizar,gbc);
    }



    /*
     //////////////////////////////////////////////////////////////////////////////////////
                         METODO PARA INPUNT CANTIDAD DE PROCESOS
     //////////////////////////////////////////////////////////////////////////////////////
     */

    /**CONTEXTO PARA EL METOODO INSTANCIAR INPUT CANTIDAD DE PROCESOS
     * este metodo crea
     * un panel y en este panel tiene
     * un textfield para saber la cantidad de procesos que digitara el usuario
     *
     * agregado a ello un layout gridBagLayout para que se ajuste mejor los componentes
     *
     * JPanel panelCantidadProcesos;
     *     TextField txtCantidadProcesos;
     *     Label labelCantidadProcesos;
     *
     */
    public void instanciarInputCantidadProcesos(){
        panelCantidadProcesos=new JPanel(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        JLabel labelCantidadProcesos=new JLabel("Cantidad Procesos: ");
        this.labelMostrarCantidadColasPanel2=new JLabel();
        txtCantidadProcesos= new JTextField();
        txtCantidadProcesos.setPreferredSize(new Dimension(90,20));;
        gbc.insets=new Insets(5,5,5,5);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        panelCantidadProcesos.add(this.labelMostrarCantidadColasPanel2,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        panelCantidadProcesos.add(labelCantidadProcesos,gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridwidth=1;
        panelCantidadProcesos.add(txtCantidadProcesos,gbc);

    }





    /**METODO QUE QUIZAS NO SEA NECESARIO
     public void instanciarPanelMostradorProceso(){
         panelMostradorProcesos.setBounds(0,30,anchoFrame/10,altoFrame);
         panelMostradorProcesos.setBackground(Form.COLOR_Q);
         this.add(panelMostradorProcesos);
         panelMostradorProcesos.setVisible(true);
     }*/

    /**CONTEXTO DE EVENTOS
     * existe action listener que es cuando se ejecuta una sola vez(click)
     * KeyListener para eventos de teclado
     * MouseListener para eventos del mouse
     * FocusListener para eventos cuando se pierde foco y demas
     * son interfaces cada una con una manera diferente de instanciar
     */
    /*
     //////////////////////////////////////////////////////////////////////////////////////
                  METODOS PARA AGREGAR EVENTOS A LOS BOTONES PRINCIPALES
     //////////////////////////////////////////////////////////////////////////////////////
     */
    /**CONTEXTO DEL METODO
     * eventos para los botones
     * evento para cuando se clickee solamente en el btn cargarPRUN
     * EL METODO agregarEventoAlosBotonesPrincipales
     * llama a este metodo para agregarlo a los bootones
     */
    private void agregarEventobtnCargarPRUN(){

        btnCargarPRUN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Form.this.estaEnModoTimer){
                    Form.this.ejecutarCargarPRUN();
                    //Form.this.btndarQ.setEnabled(true);
                    //Form.this.btnCargarPRUN.setEnabled(false);
                }
            }
        });
    }


    /**CONTEXTO DEL AGREGAR DAR Q
     * boton para dar Q
     */
    private void agregarEventobtndarQ(){
        btndarQ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Form.this.estaEnModoTimer){
                    //Form.this.btndarQ.setEnabled(false);
                    //Form.this.btnCargarPRUN.setEnabled(true);
                    Form.this.ejecutarDarQ();
                }
            }
        });
        //no requiero agregarlo al contenedor del frame(panel)
        //por que en otro metodo lo hago
    }


    /**CONTEXTO DEL AGREGAR AUTOMATICO
     * metodo para agregar un evento al boton de automatico
     * para que se ejecute automaticamente con un start en el timer
     * EN ESTE METODO SE LE DA START AL TIMER
     */
    private void agregarEventobtnAutomatico(){
        btnAutomatico.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(Form.this.activeCargarPRUN&&!Form.this.estaEnModoTimer){//si esta en modo activarCARGARprun
                    JOptionPane.showMessageDialog(null,"¡Se debe dar Dar q Primero!");
                    return;
                }
                if(!Form.this.estaEnModoTimer){//si no esta en modo timer que de start
                        Form.this.timer=new Timer(1000,Form.this.actionUniversal);
                        Form.this.timer.start();
                        Form.this.estaEnModoTimer=true;
                }else{//si esta en modo timer y presiona de nuevo le dara stop,para parar el timer
                    Form.this.timer.stop();
                    Form.this.estaEnModoTimer=false;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {//cuando entra al componente
                if(!Form.this.estaEnModoTimer){
                    Form.this.btnAutomatico.setText(Form.TEXTO_PLAY);
                }else{//si esta en modo timer hay que pausarlo
                    Form.this.btnAutomatico.setText(Form.TEXTO_PAUSE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Form.this.btnAutomatico.setText(Form.TEXTO_AUTOMATICO);
            }
        });
    }

    /**CONTEXTO METODO AGREGAR EVENTO PARA REINICIAR
     * metodo para agregar un evento al boton de reiniciar

     * ESTE METODO SE ENCARGA DE REINICIAR LA LISTA PRINCIPAL
     * LLAMADA LISTA COLA PRIORIDAD CON LA COPIA
     * ASIMISMO SE DEBE REINICIAR A NIVEL BACKEND Y LA FLECHA TAMBIEN
     *
     */
    private void agregarEventobtnReiniciar(){
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Form.this.estaEnModoTimer){
                    Form.this.ejecutarReinicio();
                }
            }
        });
    }


    public void ejecutarReinicio(){
        Form.this.listaColaPrioridad=new ListaColaPrioridad(copiaListaColaPrioridad);//vuelvo al origen
        Form.this.eliminarDeLaListaLabel(0,Form.this.listaColaPrioridad.size());
        Form.this.posicionYLabelEcu=Form.this.posicionYlabel;
        Form.this.crearLabelPorDefecto(0,Form.this.listaColaPrioridad.size());
        Form.this.k=0;
        Form.this.moverFlecha(Form.this.Flecha.getX(),Form.this.Flecha.getY());
        Form.this.limpiarLabelProceso();
        Form.this.btnCargarPRUN.setEnabled(true);
        Form.this.btndarQ.setEnabled(false);
    }
    /**
     * CONTEXTO DEL METODO ELIMINAR CADENA DEL LABEL
     * metodo para eliminar una cadena de un determinado label
     * ese determinado label esta dado por k
     * esto se usa cuando se hace un cargar prun
     *
     * @param procesoAEliminar cadena a eliminar
     */
    private void eliminarProcesoDeLabel(String procesoAEliminar){

        String cadenaCompleta=this.listaLabel.get(k).getText();
        int posicionInicial=cadenaCompleta.indexOf(procesoAEliminar);
        cadenaCompleta=cadenaCompleta.substring(posicionInicial+ procesoAEliminar.length());
        cadenaCompleta=cadenaCompleta.trim();
        this.listaLabel.get(k).setText(cadenaCompleta);
    }


    /**CONTEXTO MOVER FLECHA
     * este metodo es para mover el label flecha
     * donde le mandamos las coordenadas para mover
     * si K==0 quiere decir que volvio al inicio
     * por lo tanto la flecha vuelve a su posicion por defecto
     * recordando que ese posicionYlabel y posicion XLabel son como constantes
     *
     * @param x
     * @param y
     */
    private void moverFlecha(int x,int y){
        if(k==0){
            this.Flecha.setLocation(x,this.posicionYlabel);//se reinicia
        }else{
            this.Flecha.setLocation(x,y);
        }
    }

    /**
     * metodo para actualizar todos los label en base a sus valor de cada cola de la lista de colas
     */
    public void actualizarTodosLosLabel() {
        String cadena = "";
        for (int i = 0; i < this.listaColaPrioridad.size(); i++) {
            this.actualizarLabel(i);
        }
    }

    private void limpiarLabelProceso(){
        this.labelProceso.setText("");
    }

    /**
     * un poco costoso dado que cada que se le de el quantum se ejecutara esto
     * pero es por si se algun pcb por si tiene limite te quantum
     * si son infinitos es envano casi
     *
     * este metodo depende de la actualizacion en el negocio o backen
     * dado que usamos un label solamente y en ese estan todos los supuestos procesos
     * cuando actualizamos un pcb
     * debemos actualizar el label en si
     * @param  posicionColaInicial  posicion de la lista que queremos actualizar
     *                              al dar una posicion en la lista y la lista al hacer get
     *                              obtiene una cola
     */
    public void actualizarLabel(int posicionColaInicial){
            String cadena="";
                for(PCB elemento: this.listaColaPrioridad.getColaPrioridad(posicionColaInicial).getColaPrioridad()){
                    cadena+=elemento.toString()+"     ";
                }
                cadena=cadena.trim();
                this.listaLabel.get(posicionColaInicial).setText(cadena);
    }



    /**
     * metodo para crear un evento PARA LA ACCION UNIVERSAL
     * ESTO SIRVE PARA EJECUTAR EL TIMER QUE HACE EL AUTOMATICO
     */
    public void crearEventoParaIniciarTimer(){
        this.actionUniversal=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //puede provocar fallos si ya se hizo un previo cargar prun
                //sin embargo si se le da un dar q y luego automatico no da errores
                //pero si damos cargar prun y luego automatico,pues nos arrojaria resultados inconsistentes
                Form.this.ejecutarCargarPRUN();
                SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Thread.sleep(600);
                        return null;
                    }
                    @Override
                    public void done(){
                        Form.this.ejecutarDarQ();
                    }
                };
                worker.execute();
            }
        };
    }

    /**CONTEXTO EJECUTAR CARGAR PRUNR
     * ESTE METODO SE ENCARGA DE CARGAR PRUN ES DECIR
     * ES EL QUE SE ENCARGAR DE EJECUTAR LA LOGICA EN EL BACKEN
     * Y TAMBIEN MUESTRA LOS RESULTADOS EN EL FRONT QUE SERIA EN LOS LABEL
     */
    public void ejecutarCargarPRUN(){
        this.activeCargarPRUN=true;
        this.btndarQ.setEnabled(true);
        this.btnCargarPRUN.setEnabled(false);
        pcbUniversal=Form.this.listaColaPrioridad.cargarPRUN();//cargarPRUN no afecta a K
        //a nivel backend decola de la cola
        if(pcbUniversal!=null){
            Form.this.labelProceso.setText(pcbUniversal.toString());//a nivel front ese pcb que tengo actualmente lomuestro
            k=Form.this.listaColaPrioridad.getK();
            Form.this.eliminarProcesoDeLabel(pcbUniversal.toString());//a nivel front lo elimino del label
        }

    }

    /**CONTEXTO EJECUTAR DARQ
     * ESTE METODO SIRVE EJECUTAR EL METODO DAR Q DE LA LISTA COLA PRIORIDAD
     * TENER EN CUENTA QUE SE DEBE HACER PRIMERO EN EL BACKEND Y DESPUES EL FRONT
     * TAMBIEN LA FLECHA DEBE MOVERSE AQUI
     * LA LOGICA ES ALGO ASI
     * SI EL PCB UNIVERSAL ES NULO,LO MANDA AL METODO
     * Y SOLAMENTE AVANZA DE COLA
     * SI EL PCB ES INFINITO NUNCA ACABA POR LO TANTO DEBO ACTUALIZAR SU LABEL DONDE ESTABA ANTES
     * Y DONDE ESTA ACTUALMENTE PARA ELLO SE REUTILIZA UN METODO ACTUALIZAR LABEL PASANDO K  Y UN K ANTERIOR
     *
     */
    public void ejecutarDarQ(){
        Form.this.limpiarLabelProceso();
        Form.this.existeClones=true;
        Form.this.activeCargarPRUN=false;
        Form.this.btndarQ.setEnabled(false);
        Form.this.btnCargarPRUN.setEnabled(true);
        if(pcbUniversal!=null){
            if(!pcbUniversal.esQuantumInfinito()){
                double supuestoQuantum=pcbUniversal.getQuantum();
                if(supuestoQuantum-1==0){//si finalizo
                    Form.this.listaColaPrioridad.darQuantum(pcbUniversal);//cuando entra aca puede ser que el pcbUniversal sea nulo
                    Form.this.actualizarTodosLosLabel();//actualizo todos los label
                    k=Form.this.listaColaPrioridad.getK();
                    Form.this.moverFlecha(Flecha.getX(),Flecha.getY()+40);
                    return;
                }
            }
            double kAnterior=Form.this.k;
            //si viene por aca quiere decir que el pcb es infinito
            Form.this.listaColaPrioridad.darQuantum(pcbUniversal);
            Form.this.actualizarLabel((int) kAnterior);
            k=listaColaPrioridad.getK();
            Form.this.actualizarLabel(k);
            Form.this.moverFlecha(Flecha.getX(),Flecha.getY()+40);
        }else{
            Form.this.listaColaPrioridad.darQuantum(pcbUniversal);//cuando entra aca puede ser que el pcbUniversal sea nulo
            k=listaColaPrioridad.getK();
            Form.this.moverFlecha(Flecha.getX(),Flecha.getY()+40);
        }
    }

    /**
     * por ahora solo pausa el timer
     */
    /*public void agregarEventoMostrarHistorial(){
        this.itemMostrarHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("sd");
            }
        });
    }*/

    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            Form frame = new Form();
            frame.setVisible(true);
        });
    }

    /**
     * metodo para activar el inout box para pedir la cantida de colas
     *
     * Ya esta con validaciones
     */
    public void agregarEventoCantidadColas(){
            this.itemAgregarCantidadColas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!Form.this.estaEnModoTimer){
                        try {
                            int colas = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca la cantidad de colas: "));
                            if(colas<=Form.CANTIDAD_COLAS_MAXIMAS){
                                Form.this.generarColas(colas);
                                Form.this.copiaListaColaPrioridad=new ListaColaPrioridad(Form.this.listaColaPrioridad);//actualizo la copia
                            }else{
                                JOptionPane.showMessageDialog(null,"¡La cantidad de colas Ingresada Excede al Maximo!","Advertencia",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }catch(Exception E){
                            JOptionPane.showMessageDialog(null,"Error..Dato No valido..","ERROR",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
    }

    /**CONTEXTO DEL METODO GENERAR COLAS
     * ESTE METODO SIRVE PARA GENERAR COLAS A NIVEL FRONT ES DECIR
     * CREAR LOS LABEL NECESARIOS
     * A SI MISMO UNA VEZ QUE SE GENERA LAS COLAS POR DEFECTO SE DEBE VOLVER LA FLECHA AL INICIO
     * Y TAMBIEN K VALE 0,OSEA ES COMO UN REINICIO PERO A NIVEL FRONT
     *
     * DADO QUE CADA QUE SE EJECUTE ESTE METODO LOS CREA CON 2 PROCESOS POR DEFECTO
     * @param cantidadAGenerar LA CANTIDAD A GENERAR
     */
    public void generarColas(int cantidadAGenerar){
        // la manera que estoy usando era que no importa que tenga la cola
        //se va a eliminar y empezara otra desde 0
        if(cantidadAGenerar>0){
            this.eliminarDeLaListaLabel(0,this.listaColaPrioridad.size());//elimina a nivel front
            this.listaColaPrioridad.vaciar();
            this.listaColaPrioridad.crearColasConProcesos(cantidadAGenerar);// a nivel backend
            this.posicionYLabelEcu=posicionYlabel;//mueve la posion y a la origen
            this.crearLabelPorDefecto(0,cantidadAGenerar);
            this.listaColaPrioridad.setK(0);
            k=this.listaColaPrioridad.getK();
            this.moverFlecha(this.Flecha.getX(),this.Flecha.getY());
        }

    }

    /**CONTEXTO METODO GENERAR COLAS 2
     * ESTE METODO ES UNA IDEA INICIAL QUE TENIA DEL GENERAR COLAS DEJANDO PEDASOS ARRIBA Y ABAJO
     * SIN EMBARGO LA IMPLEMENTACION NO ES ASI,PERO LO DEJO POR SI SE HICIESE CAMBIOS
     * @param cantidadAGenerar CANTIDAD DE COLAS A GENERAR
     */
    public void generarColas2(int cantidadAGenerar){
        if(cantidadAGenerar==this.listaColaPrioridad.size()){
                return;
            }
            if(cantidadAGenerar<this.listaColaPrioridad.size()){
                //si n es menor al tamaño de la lista,entonces debe eliminar
                int cantidadeColas=this.listaColaPrioridad.size();
                //eliminar colas o lista label(inicio,fin)
                //elimina de un final al inicio sin eliminar el inicio
                this.listaColaPrioridad.eliminarColas(cantidadAGenerar,cantidadeColas);
                this.eliminarDeLaListaLabel(cantidadAGenerar,cantidadeColas);
                //si esta vacio la lista quiere decir que se elimino todos
                //que la posicion vuelva a por defecto
                if(this.listaLabel.isEmpty()){
                    this.posicionYLabelEcu=posicionYlabel;
                    this.listaColaPrioridad.setK(0);
                    this.moverFlecha(this.Flecha.getX(),this.Flecha.getY());
                }else{
                    this.posicionYLabelEcu=this.listaLabel.get(this.listaLabel.size()-1).getY()+40;
                    this.listaColaPrioridad.setK(this.listaColaPrioridad.size()-1);
                    this.moverFlecha(this.Flecha.getX(),posicionYLabelEcu-40);
                }

                return;
            }
            //si n es mayor al tamaño de la lista,debo agregar elementos
            int diferencia=cantidadAGenerar-this.listaColaPrioridad.size();
            int sizeActual=this.listaColaPrioridad.size();
            //a este metodo le digo que me cree colas y le mando como parametro cuantas colas quiere
            this.listaColaPrioridad.crearColasConProcesos(diferencia);//a nivel backend ya creo las colas(la lista crecio)
            this.crearLabelPorDefecto(sizeActual,diferencia);// a nivel front faltaria
            this.repaint();
    }


    /**CONTEXTO METODO ELIMINAR DE LA LISTA LABEL
     * metodo para eliminar los elementos de la lista donde aparecen los Q
     * y los label pcb
     * se eliminan del frame
     * al remover un label de la lista este devuelve la referencia del objeto
     * esto me sirve para eliminarlo del frame
     * CUANDO SE ELIMINA UN OBJETO SE DEBE HACER REPAIN PARA HACER NOTAR QUE SE ELIMINO
     * @param inicio desde donde quieres eleminar
     * @param fin hasta donde quieres eliminar
     */
    public void eliminarDeLaListaLabel(int inicio,int fin){
        for (int i = fin; i >inicio ; i--) {
            //lo remuevo del frame
            this.panelContenedorFrame.remove(this.listaLabel.remove(i-1));
            this.panelContenedorFrame.remove(this.listalabelColas.remove(i-1));
        }
        panelContenedorFrame.revalidate();
        panelContenedorFrame.repaint();//repinto el frame para hacer ver que si elimino
    }
    /**CONTEXTO PARA AGREGAR EVENTO A LABEL
     * PASANDO EL DETERMINADO LABEL QUE QUEREMOS QUE TENGA ESE EVENTO
     * paso de parametros para instanciar JOptionPane showOption Dialog
     * se le pasa un contenedor donde se ejecutara(?
     * el panel de radios
     * el titulo
     * un "icono " sin embargo al estar en noOption no sale nada
     * el texto plano
     * sin icono
     * y el vector de opciones
     *
     * este metodo es para agregar un evento a cada label donde estan los procesos
     * o ecuaciones
     *
     * los eventos seran donde se mostraran las ventanas para dar Q para finalizar
     * o dar una cantida de procesos a una determinada cola
     *
     * estos eventos son agregados a un label que se usa en conjunto con el agregar
     * label o crear label por defecto
     *
     */
    public void agregarEventoALabel(JLabel label){
        String[] opciones={"Aceptar","Cancelar"};
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String msj="Se encuentra en la Cola Q["+e.getComponent().getName()+"]";
                Form.this.labelMostrarCantidadColasPanel1.setText(msj);
                Form.this.labelMostrarCantidadColasPanel3.setText(msj);
                Form.this.labelMostrarCantidadColasPanel2.setText(msj);
                int posicionCola=Integer.parseInt(e.getComponent().getName());//por defecto los label estan nombrados con [1..N]
                //para utilizarlo y acceder se resta -1
                try{
                    int opcion=JOptionPane.showOptionDialog(null,Form.this.panelRadios,"Seleccione una Opcion"
                            ,JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,opciones,opciones[0]);
                    //System.out.println(opcion);//0 para aceptar//cuando se cancela//-1 cuando se cierra la ventana
                    if(opcion==0){//si se selecciono aceptar entra,caso contrario no
                        if(Form.this.rdOpcionAgregarProcesos.isSelected()){

                            Form.this.ejecutarOptionPaneAgregarProcesos(posicionCola-1);
                        }else{
                            Form.this.ejecutarOptionPaneDarProcesoParaFinalizar(posicionCola-1);
                        }
                        Form.this.limpiarTextFieldDeLosJOption();//limpiro los textfield
                        Form.this.grupoDeRadios.clearSelection();//limpiar la seleccion
                    }else{
                        JOptionPane.showMessageDialog(null,"No se selecciono ninguna Opcion..","Advertencia",JOptionPane.WARNING_MESSAGE);
                    }
                }catch (Exception E){
                    System.out.println(E.toString());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**CONTEXTO GETNOMBREPROCESO
     * METODO PARA OBTENER EL NOMBRE DE PROCESOS DEL TXT NOMBRE DE PROCESO
     * @return retorna lo que tenga el campo nombre de proceso
     */
    public String getNombreProceso(){
        return this.txtNombreProceso.getText();
    }

    /**CONTEXTO GET CANTIDAD PARA FINALIZAR
     *
     * @return retorna lo que tenga el campo del txt cantidad de procesos para finalizar
     */
    public String getCantidadParaFinalizar(){
        return this.txtCantidadParaFinalizar.getText();
    }

    /**CONTEXTO DEL METOOD DAR PROCESOS PARA FINALIZAR
     * metodo para setear al pcb cuando finalizara
     *es decir que recibimos de los campos para obtener el nombre de procesos y cantidad
     * y en base a eso actualizar a nivel backend todos los pcb con el nuevo (Q)
     * podria modificarse si se ajusta el pcb
     *
     * falta validadcion
     */
    public void darProcesoParaFinalizar(int posicionDeLaCola){
        //aun no hay validaciones
        try{
            String nombreProceso=Form.this.getNombreProceso();
            String cantidadProceso=Form.this.getCantidadParaFinalizar();
            nombreProceso=nombreProceso.toUpperCase();
            double cantidadProcesoParaFinalizar;
            if(cantidadProceso.toUpperCase().equals("INFINITO")){
                cantidadProcesoParaFinalizar=Double.POSITIVE_INFINITY;
            }else{
                cantidadProcesoParaFinalizar=Double.parseDouble(cantidadProceso);
            }

            int posicion=nombreProceso.indexOf("*");
            int id;
            boolean clon;
            if(posicion>=0){
                 id=Integer.parseInt(nombreProceso.substring(1,posicion));
                clon=true;
            }else{
                 id=Integer.parseInt(nombreProceso.substring(1,nombreProceso.length()));
                 clon=false;
            }
            //ENTRA ACA SI PILLA EL PCB EN LA COLA DONDE ESTA PARADO
            if(this.listaColaPrioridad.getColaPrioridad(posicionDeLaCola).existePCB(
                    new PCB(nombreProceso.substring(0,1),id,clon,cantidadProcesoParaFinalizar
            ))>=0){
                Form.this.listaColaPrioridad.actualizarPCBEnTodasLasColas(
                        new PCB(nombreProceso.substring(0,1),id,clon,cantidadProcesoParaFinalizar)
                );
            }else{
                JOptionPane.showMessageDialog(null,"Error..proceso no encontrado..","ERROR",JOptionPane.ERROR_MESSAGE);
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error.. datos no validos..","ERROR",JOptionPane.ERROR_MESSAGE);
        }


    }


    /**CONTEXTO METODO LIMPIAR TODOS LOS TEXTFIELD DE LOS JOPTION
     * limpia todos los textField
     * de nombre de procesos,cantidad para finalizar y cantidad de procesos
     */
    public void limpiarTextFieldDeLosJOption(){
        this.txtNombreProceso.setText("");
        this.txtCantidadParaFinalizar.setText("");
        this.txtCantidadProcesos.setText("");
    }

    /**CONTEXTO METODO EJECUTAR OPTION PANE AGREGAR PROCESO PA FINALIZAR
     * METODO QUE SE EJECUTA CUANDO SE SELECCIONA LA OPCION DE AGREGAR PROCESOS PARA FINALIZAR
     * debemos mandarle la posicion de la cola donde deneos actualizar la cantidad de procesos
     * ya con validaciones
     * las validaciones cubren por si no se selecciona nada
     * o por si se introduce una cantidad superior a los procesos maximos
     * o por si se intruduce valores no numericos
     *
     */
    public void ejecutarOptionPaneAgregarProcesos(int posicionDeLaCola){
        String[] opciones={"ACEPTAR","CANCELAR"};
        int opcion2=JOptionPane.showOptionDialog(null,Form.this.panelCantidadProcesos,"Ingrese una cantidad De procesos: ",
                JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,opciones,opciones[0]);
        if(opcion2==0){//si esta seleccionado la opcion de agregarProcesos entra
            //int posicionDeLaCola=Integer.parseInt(e.getComponent().getName());
            try{
                int cantidadProcesosAAgregar=Integer.parseInt(Form.this.txtCantidadProcesos.getText());
                if(cantidadProcesosAAgregar<=Form.CANTIDAD_MAXIMA_PROCESOS){
                    if(Form.this.existeClones){
                        this.ejecutarReinicio2();
                        Form.this.existeClones=false;
                    }//y si no existe clones
                    Form.this.listaColaPrioridad.getColaPrioridad(posicionDeLaCola).vaciarCola();
                    Form.this.listaColaPrioridad.getColaPrioridad(posicionDeLaCola).crearProcesosPorDefecto(posicionDeLaCola,cantidadProcesosAAgregar);//y creo nuevos procesos
                    Form.this.actualizarLabel(posicionDeLaCola);
                    Form.this.copiaListaColaPrioridad= new ListaColaPrioridad(Form.this.listaColaPrioridad);//actualizo la copia
                }else{
                    JOptionPane.showMessageDialog(null,"¡La cantidad de procesos ingresados Excede al Maximo!","Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Error..Datos no validos..","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null,"Se cancelo el proceso..","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * metodo para reiniciar todos las colas,pero que no empieze desde el inicio
     */
    public void ejecutarReinicio2(){
        int kAnterior=this.listaColaPrioridad.getK();
        Form.this.listaColaPrioridad=new ListaColaPrioridad(copiaListaColaPrioridad);//vuelvo al origen
        Form.this.eliminarDeLaListaLabel(0,Form.this.listaColaPrioridad.size());
        Form.this.crearLabelPorDefecto(0,Form.this.listaColaPrioridad.size());
        this.listaColaPrioridad.setK(kAnterior);
        this.k=this.listaColaPrioridad.getK();
    }
    /**CONTEXTO DEL METODO EJECUTAR OPTION PAR DAR PROCESOS PARA FINALIZAR
     * METODO QUE SE EJECUTA CUANDO SE SELECCIONA EL PROCESOS PARA FINALIZAR
     * @param posicionDeLaCola se requiere la cola donde se actualizara los camos en el backend
     */
    public void ejecutarOptionPaneDarProcesoParaFinalizar(int posicionDeLaCola){
        String[] opciones={"ACEPTAR","CANCELAR"};
        int opcion2=JOptionPane.showOptionDialog(null,Form.this.panelParaOptionPane,"Asignacion de procesos para Finalizar"
                ,JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,opciones,opciones[0]);
        if(opcion2==0){
            Form.this.darProcesoParaFinalizar(posicionDeLaCola);
        }else{
            JOptionPane.showMessageDialog(null,"Se cancelo el proceso..","Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }
}
