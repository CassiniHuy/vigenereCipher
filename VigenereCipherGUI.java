package vigenere;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class VigenereCipherGUI extends JFrame {

	private JPanel contentPane;
	private JTextField fieldKey;
	private JTextField fieldLocation;
	private JTextField fieldName;
	private JTextArea areaInput;
	private JTextArea areaOutput;
	private JTextArea areaInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VigenereCipherGUI frame = new VigenereCipherGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VigenereCipherGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 714);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelInput = new JLabel("Input");
		labelInput.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelInput.setBounds(15, 30, 69, 31);
		contentPane.add(labelInput);
		
		JLabel labelOutput = new JLabel("Output");
		labelOutput.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelOutput.setBounds(15, 361, 69, 31);
		contentPane.add(labelOutput);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 61, 517, 268);
		contentPane.add(scrollPane);
		
		areaInput = new JTextArea();
		areaInput.setText("The text you want to encrypt.");
		areaInput.setLineWrap(true);
		scrollPane.setViewportView(areaInput);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(15, 392, 515, 266);
		contentPane.add(scrollPane_1);
		
		areaOutput = new JTextArea();
		areaOutput.setLineWrap(true);
		scrollPane_1.setViewportView(areaOutput);
		
		JLabel labelKey = new JLabel("Key");
		labelKey.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelKey.setBounds(558, 275, 69, 31);
		contentPane.add(labelKey);
		
		fieldKey = new JTextField();
		fieldKey.setText("KEY");
		fieldKey.setBounds(558, 306, 285, 26);
		contentPane.add(fieldKey);
		fieldKey.setColumns(10);
		
		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(new encryptAction());
		btnEncrypt.setBounds(558, 337, 115, 29);
		contentPane.add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addActionListener(new decryptAction());
		btnDecrypt.setBounds(688, 337, 115, 29);
		contentPane.add(btnDecrypt);
		
		JButton btnDeciper = new JButton("Decipher");
		btnDeciper.addActionListener(new decipherAction());
		btnDeciper.setBounds(558, 382, 115, 29);
		contentPane.add(btnDeciper);
		
		JButton btnCreateFile = new JButton("Create");
		btnCreateFile.addActionListener(new createFileAction());
		btnCreateFile.setBounds(558, 629, 115, 29);
		contentPane.add(btnCreateFile);
		
		JLabel labelLocation = new JLabel("Location");
		labelLocation.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelLocation.setBounds(558, 497, 115, 31);
		contentPane.add(labelLocation);
		
		fieldLocation = new JTextField();
		fieldLocation.setBounds(559, 530, 264, 26);
		contentPane.add(fieldLocation);
		fieldLocation.setColumns(10);
		
		JLabel labelFileName = new JLabel("File Name");
		labelFileName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelFileName.setBounds(558, 556, 115, 31);
		contentPane.add(labelFileName);
		
		fieldName = new JTextField();
		fieldName.setBounds(558, 587, 225, 26);
		contentPane.add(fieldName);
		fieldName.setColumns(10);
		
		JLabel lbltxt = new JLabel(".txt");
		lbltxt.setBounds(789, 590, 69, 20);
		contentPane.add(lbltxt);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(558, 84, 285, 178);
		contentPane.add(scrollPane_2);
		
		areaInfo = new JTextArea();
		areaInfo.setEditable(false);
		areaInfo.setText("Note:All punctuation marks will be removed!");
		areaInfo.setLineWrap(true);
		scrollPane_2.setViewportView(areaInfo);
		
		JLabel lblCreateAFile = new JLabel("Create a file from output.");
		lblCreateAFile.setBounds(558, 461, 265, 20);
		contentPane.add(lblCreateAFile);
		
		JLabel lblInformation = new JLabel("Information");
		lblInformation.setBounds(558, 62, 115, 20);
		contentPane.add(lblInformation);
		
        new encryptAction().actionPerformed(new ActionEvent("", 0, ""));
	}

	private class createFileAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
            String path = fieldLocation.getText();

            File dirPath = new File(path);
            if (!dirPath.exists())
                areaInfo.append("\nPath don't exist!\n");
            else {
            	String fileName = fieldName.getText() + ".txt";
            	
                File newFile = new File(dirPath, fileName);
                if (newFile.exists()){
                    areaInfo.append("\nFile have aready existed!\n");
                	return;
                }
                
                try {
                    newFile.createNewFile();
                    PrintWriter printWriter = new PrintWriter(new FileWriter(newFile));

                    printWriter.print("Key:" + fieldKey.getText() + " \r\n");
                    printWriter.append(areaOutput.getText());

                    areaInfo.append("\n");
                    areaInfo.append(fileName + " have created at " + path);
                    areaInfo.append("\n");

                    printWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();       
                }
            }
		}
		
	}
	
	private class encryptAction implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String text = VigenereCipher.encrypt(areaInput.getText(), fieldKey.getText());
        areaOutput.setText(text);
		areaInfo.append("\n" + text.length() + " letters" + " encrypted.\n" );
		}
	}
	
	private class decryptAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = VigenereCipher.decrypt(areaInput.getText(), fieldKey.getText());
            areaOutput.setText(text);
    		areaInfo.append("\n" + text.length() + " letters" + " decrypted.\n" );
		}
	}
	
	private class decipherAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = VigenereCipher.decipher(areaInput.getText());
            areaOutput.setText(text.replaceFirst(".+-", ""));
            fieldKey.setText(text.split("-")[0]);
    		areaInfo.append("\n" + text.length() + " letters" + " deciphered and captured key.\n" );
		}
		
	}
}
