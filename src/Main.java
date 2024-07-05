import javax.swing.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String ejemplo = "H₂⁴O es agua y 10⁴ representa diez mil.";
        System.out.println(ejemplo);

        // Usando StringBuilder para crear texto con superíndices y subíndices
        StringBuilder sb = new StringBuilder();
        sb.append("10").append('\u2074'); // 10⁴
        System.out.println(sb.toString());

    }
}