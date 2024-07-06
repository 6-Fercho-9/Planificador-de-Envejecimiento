package BotonesModificados;
/**
 *
 * @author Lenovo
 */
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
public class MyButton extends JButton {
    private static final Color BACKGROUND_COLOR=Color.white;
    private static final Color COLOR_EVENTO=new Color(33,150,243);
    private static final Color COLOR_LETRAS=Color.BLACK;
    private static final Font FONDO_DEFECTO=new Font("RobotoLt",Font.PLAIN,14);
    private static final Color COLOR_EVENTO_SALE=Color.WHITE;
    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    //obtener color de encima
    public Color getColorOver() {
        return colorOver;
    }
    //enviar color encima
    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
    }
    //obtener color cuando se hace click
    public Color getColorClick() {
        return colorClick;
    }
    //enviar el color cuando se hace click
    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }
    //obtener el color del borde del botom
    public Color getBorderColor() {
        return borderColor;
    }
    //enviar color de borde al botom
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    public void setColorTexto(Color c){
        this.setForeground(c);
    }
    public MyButton() {
        //  Init Color
//por defecto el color del boton es negro
        setFont(MyButton.FONDO_DEFECTO);
        setColor(MyButton.BACKGROUND_COLOR);//parece que es para la letra
        setForeground(MyButton.COLOR_LETRAS);//y las letras en negro
        colorOver = MyButton.COLOR_EVENTO;//color encima
        colorClick = MyButton.COLOR_EVENTO;//color click blanco
        //colorClick = new Color(152, 184, 144);
        //borderColor = new Color(30, 136, 56);
        borderColor = new Color(33,150,243);;//borde de color blanco
        radius=25;
        setContentAreaFilled(false);
        //  Add event mouse
        addMouseListener(new MouseAdapter() {

            @Override

            //evento cuando el mouse entra al objeto boton
            public void mouseEntered(MouseEvent me) {
                setBackground(colorOver);//pintalo  blanco
                setForeground(MyButton.COLOR_EVENTO_SALE);//y las letras en negro
                over = true;//y habilita la bandera para decir que esta encima
            }

            @Override
            //evento cuando el mouse sale del objeto boton
            public void mouseExited(MouseEvent me) {
                setBackground(color);
                setForeground(MyButton.COLOR_LETRAS);//y las letras en negro
                over = false;

            }

            @Override
            //evento cuando el se presiona el mouse
            public void mousePressed(MouseEvent me) {
                setBackground(colorClick);
                setForeground(MyButton.COLOR_EVENTO_SALE);
            }

            @Override
            //evento cuando se suelta de hacer click(?
            public void mouseReleased(MouseEvent me) {
                setBackground(MyButton.COLOR_EVENTO);
                setForeground(MyButton.COLOR_EVENTO_SALE);
                /*if (over) {
                    setBackground(Color.GREEN);
                    //setForeground(new Color(0,0,0));
                } else {
                    //setBackground(color);
                    setBackground(Color.BLACK);
                }*/
            }
        });
    }

    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private int radius = 0;

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //  Paint Border
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        //  Border set 2 Pix
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(grphcs);
    }
}
