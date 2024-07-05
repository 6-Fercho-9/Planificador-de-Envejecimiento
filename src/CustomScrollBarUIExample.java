import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class CustomScrollBarUIExample {
    public static void main(String[] args) {
        // Crear el campo de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido");

        // Crear el primer campo de entrada
        JTextField field1 = new JTextField(10);

        // Crear el segundo campo de entrada
        JTextField field2 = new JTextField(10);

        // Crear el panel para contener los campos de entrada y los textos
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Margen entre componentes

        // Agregar el welcomeLabel usando GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Ocupa 2 columnas
        panel.add(welcomeLabel, gbc);

        // Agregar el JLabel "Digite su nombre:" y el JTextField correspondiente
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;  // Vuelve a ocupar solo 1 columna
        panel.add(new JLabel("Digite su nombre:"), gbc);

        gbc.gridx = 1;
        panel.add(field1, gbc);

        // Agregar el JLabel "Digite su edad:" y el JTextField correspondiente
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Digite su edad:"), gbc);

        gbc.gridx = 1;
        panel.add(field2, gbc);

        // Crear los botones personalizados
        String[] options = new String[] {"Aceptar", "Cancelar"};

        // Mostrar el JOptionPane personalizado
        int option = JOptionPane.showOptionDialog(
                null,
                panel,
                "Ingrese sus datos",
                JOptionPane.NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        // Obtener los valores de los campos de entrada si el usuario hace clic en "Aceptar"
        if (option == 0) { // Aceptar
            String name = field1.getText();
            String age = field2.getText();
            System.out.println("Nombre: " + name);
            System.out.println("Edad: " + age);
        } else {
            System.out.println("Operación cancelada.");
        }
    }}

    // Clase personalizada para ScrollBarUI
