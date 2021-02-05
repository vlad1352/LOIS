import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {

    public JTextField textField;


    UI(){
        super("ЛОИС 1-2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textField = new JTextField(20);
        JLabel label = new JLabel("Введите формулу");
        JLabel label2 = new JLabel("Введите количество подформул");
        JTextField textField1 = new JTextField(20);
        JButton jButton1 = new JButton("Найти количество подформул");
        jButton1.setSize(new Dimension(100,50));
        JButton button = new JButton("Построить СКНФ");
        button.setSize(new Dimension(100,50));

        JPanel panel1 = new JPanel();
        panel1.add(label);
        panel1.add(textField);
        panel1.add(label2);
        panel1.add(textField1);
        panel1.add(jButton1);
        panel1.add(button);

        // Кнопка подсчета подформул и проверки пользователя
        jButton1.addActionListener(e -> {

           Formula formula = new Formula(textField.getText());
           JDialog jDialog = createDialog();

           JPanel panel2 = new JPanel();
           panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

           JButton jButton2 = new JButton("ОК");
           jButton2.addActionListener(e1 -> jDialog.setVisible(false));
           JLabel label1;
           JLabel label3;

           if (formula.checkFormula()){
               label1 = new JLabel("Количество подформул: " + formula.findSubFormulas());
               if(textField1.getText().equals("")){
                   label3 = new JLabel("Вы не ввели количество подформул");
               }
               else if(formula.findSubFormulas() == Integer.parseInt(textField1.getText())) {
                   label3 = new JLabel("Введенное количество подформул - верно");
               }
               else {
                   label3 = new JLabel("Введенное количество подформул - неверно");
               }
           } else {
               label1 = new JLabel("                Введите правильную формулу ");
               label3 = new JLabel("");
           }

           panel2.add(label1);
           panel2.add(Box.createVerticalGlue());
           panel2.add(label3);
            panel2.add(Box.createVerticalGlue());
            panel2.add(jButton2);
           jDialog.add(panel2);
           jDialog.setVisible(true);

        });


        //Кнопка построения СКНФ из заданной формулы
        button.addActionListener(e -> {
            Formula formula = new Formula(textField.getText());
            JDialog jDialog = createDialog();
            JPanel panel2 = new JPanel();
            panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
            JButton jButton2 = new JButton("ОК");
            JLabel label1;

            jButton2.addActionListener(e1 -> jDialog.setVisible(false));
//----------------

            if(formula.checkFormula()){
                label1 = new JLabel(formula.makeSKNF());

            } else {
                label1 = new JLabel("                Введите правильную формулу ");
            }

            panel2.add(label1);
            panel2.add(Box.createVerticalGlue());
            panel2.add(jButton2);
            jDialog.add(panel2);
            jDialog.setVisible(true);

        });

        setContentPane(panel1);
        setSize(300,300);
        setVisible(true);

    }

    // Создание диалогового окна
    private JDialog createDialog(){
        JDialog jDialog = new JDialog(this, "Информация", true);
        jDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jDialog.setSize(300,100);
        return jDialog;
    }

    public String getFormula(){
        return textField.getText();
    }
}
