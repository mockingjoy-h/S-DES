import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SDESUI extends JFrame implements ActionListener {
    private JLabel inputLabel, keyLabel, outputLabel;
    private JTextField inputField, keyField, outputField;
    private JRadioButton binaryInputRadio, asciiInputRadio, binaryOutputRadio, asciiOutputRadio;
    private ButtonGroup inputGroup, outputGroup;
    private JButton encryptButton, decryptButton;

    public SDESUI() {
        super("SDES");

        inputLabel = new JLabel("输入:");
        keyLabel = new JLabel("密钥:");
        outputLabel = new JLabel("输出:");

        inputField = new JTextField(20);
        keyField = new JTextField(20);
        outputField = new JTextField(20);
        outputField.setEditable(false);

        binaryInputRadio = new JRadioButton("二进制输入");
        asciiInputRadio = new JRadioButton("ASCII输入");
        binaryOutputRadio = new JRadioButton("二进制输出");
        asciiOutputRadio = new JRadioButton("ASCII输出");

        inputGroup = new ButtonGroup();
        inputGroup.add(binaryInputRadio);
        inputGroup.add(asciiInputRadio);

        outputGroup = new ButtonGroup();
        outputGroup.add(binaryOutputRadio);
        outputGroup.add(asciiOutputRadio);

        encryptButton = new JButton("加密");
        decryptButton = new JButton("解密");
        encryptButton.addActionListener(this);
        decryptButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(binaryInputRadio);
        panel.add(asciiInputRadio);
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(outputLabel);
        panel.add(outputField);
        panel.add(binaryOutputRadio);
        panel.add(asciiOutputRadio);
        panel.add(encryptButton);
        panel.add(decryptButton);

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText();
        String key = keyField.getText();
        String output = "";

        if (binaryInputRadio.isSelected()) {
            if (e.getSource() == encryptButton) {
                output = SDES.encrypt(input, key);
            } else if (e.getSource() == decryptButton) {
                output = SDES.decrypt(input, key);
            }
        } else if (asciiInputRadio.isSelected()) {
            if (e.getSource() == encryptButton) {
                output = SDES.encryptASCII(input, key);
            } else if (e.getSource() == decryptButton) {
                output = SDES.decryptASCII(input, key);
            }
        }

        if (binaryOutputRadio.isSelected()) {
            outputField.setText(output);
        } else if (asciiOutputRadio.isSelected()) {
            try {
                int binaryValue = Integer.parseInt(output, 2);
                char asciiChar = (char) binaryValue;
                outputField.setText(String.valueOf(asciiChar));
            } catch (NumberFormatException ex) {
                outputField.setText("Invalid Binary Output");
            }
        }
    }

    public static void main(String[] args) {
        new SDESUI();
    }
}
