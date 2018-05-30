package vigenere;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author cassini
 * @version 2.0
 * vigenere cipher
 * */

public class VigenereCipherFrame extends JFrame {
    //components
    private JButton button1, button2, button3, button4;
    private JTextArea inputArea;
    private JTextField outputArea;
    private JTextField keyField;
    private JTextField locationField;
    private JLabel locationLabel;

    /**
     * ActionListener for encryption
     * */
    class encryptAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            outputArea.setText(
                    VigenereCipher.encrypt(
                            inputArea.getText(), keyField.getText()
                    ));
        }
    }

    /**
     * ActionListener for decryption
     * */
    class decryptAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            outputArea.setText(
                    VigenereCipher.decrypt(
                            inputArea.getText(), keyField.getText()
                    )
            );
        }
    }

    /**
     * ActionListener for decipher
     * */
    class decipherAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            outputArea.setText(
                    VigenereCipher.decipher(
                            inputArea.getText().replaceFirst(".+-", "")
                    )
            );
            keyField.setText(outputArea.getText().split("-")[0]);
        }
    }

    class decipherActionToFile implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String path = locationField.getText();

            File dirPath = new File(path);
            if (!dirPath.exists())
                locationLabel.setText("Path don't exist!");
            else {
                File newFile = new File(dirPath, "newText.txt");
                try {
                    newFile.createNewFile();
                    PrintWriter printWriter = new PrintWriter(new FileWriter(newFile));

                    String string = VigenereCipher.decipher(inputArea.getText());
                    printWriter.print("Key:" + string.split("-", 2)[0] + "\n");
                    printWriter.append(string.replaceFirst(".+-", ""));

                    locationLabel.setText("\"newText.txt\" created.");

                    printWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    VigenereCipherFrame(){
        //initialize the frame
        super("Vigenere Cipher Tools");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 900);
        setResizable(false);

        button1 = new JButton("Encrypt");
        button1.addActionListener(new encryptAction());

        button2 = new JButton("Decrypt");
        button2.addActionListener(new decryptAction());

        button3 = new JButton("To string");
        button3.addActionListener(new decipherAction());

        button4 = new JButton("To a file");
        button4.addActionListener(new decipherActionToFile());

        keyField = new JTextField("the key", 5);
        inputArea = new JTextArea(55, 55);
        inputArea.setText("Input source text here.\nNote:\n" +
                "  Only support English encryption.\n" +
                "  If you want to decipher, " +
                "expect long text to guarantee the correct result.");
        outputArea = new JTextField(50);
        locationField = new JTextField(5);
        locationLabel = new JLabel("Folder location");

        //layout
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        upperPanel.add(Box.createVerticalStrut(10));
        upperPanel.add(new JLabel("KEY:"));
        upperPanel.add(Box.createVerticalStrut(10));
        upperPanel.add(keyField);
        upperPanel.add(Box.createVerticalStrut(10));;
        upperPanel.add(button1);
        upperPanel.add(Box.createVerticalStrut(10));
        upperPanel.add(button2);
        upperPanel.add(Box.createVerticalStrut(10));


        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
        lowerPanel.add(Box.createVerticalStrut(6));
        lowerPanel.add(new JLabel("Decipher it!"));
        lowerPanel.add(Box.createVerticalStrut(6));
        lowerPanel.add(button3);
        lowerPanel.add(Box.createVerticalStrut(6));
        lowerPanel.add(locationField);
        lowerPanel.add(Box.createVerticalStrut(6));
        lowerPanel.add(locationLabel);
        lowerPanel.add(Box.createVerticalStrut(6));
        lowerPanel.add(button4);
        lowerPanel.add(Box.createVerticalStrut(6));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(upperPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(new JScrollPane(inputArea));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(lowerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(outputArea);
        mainPanel.add(Box.createVerticalStrut(10));

        add(mainPanel);
        new encryptAction().actionPerformed(new ActionEvent("", 0, ""));
    }

    static VigenereCipherFrame vigenereCipherFrame;
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                vigenereCipherFrame = new VigenereCipherFrame();
            }
        });
    }
}
