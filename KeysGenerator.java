import java.util.*;
import java.math.*;
import java.io.*;

import javax.swing.*;  
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.event.*;

class KeysGenerator{
	static BigInteger[] keys = {BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO};
	static JButton generateBtn;

	public static void main(String args[]){

		JFrame f= new JFrame();

        JLabel l1 = new JLabel();  
        l1.setBounds(50, 50, 120, 30);
        l1.setText("Encryption key (e) : ");

        JLabel l2 = new JLabel();
        l2.setBounds(50, 120, 120, 30);
        l2.setText("Decryption key (d) : ");

        JLabel l3 = new JLabel();
        l3.setBounds(50, 190, 120, 30);
        l3.setText("Common value (n) : ");

        JButton encBtn = new JButton("copy");  
        encBtn.setBounds(220, 50, 120, 30);
       	encBtn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String eKey = keys[0].toString();
                if(eKey.contentEquals("0")){
                	showPopup();
                }else{
                	StringSelection stringSelection = new StringSelection(eKey);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
                }
				
            }  
        });

        JButton decBtn = new JButton("copy");  
        decBtn.setBounds(220, 120, 120, 30);
       	decBtn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String dKey = keys[1].toString();
				if(dKey.contentEquals("0")){
                	showPopup();
                }else{
                	StringSelection stringSelection = new StringSelection(dKey);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
                }
            }  
        });

        JButton comBtn = new JButton("copy");  
        comBtn.setBounds(220, 190, 120, 30);
       	comBtn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String cVal = keys[2].toString();
				if(cVal.contentEquals("0")){
                	showPopup();
                }else{
                	StringSelection stringSelection = new StringSelection(cVal);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
                }
            }  
        });


        generateBtn = new JButton("Generate Keys");  
        generateBtn.setBounds(150, 270, 120, 30);
        generateBtn.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                keys = getKeys();
            }  
        });

        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(encBtn);
        f.add(decBtn);
        f.add(comBtn); 
        f.add(generateBtn);
        f.setSize(450, 450);  
        f.setLayout(null);  
        f.setVisible(true);
	}

	public static BigInteger[] getKeys(){
		int bitlength = 1024;
	    Random r = new Random();

        BigInteger p = BigInteger.probablePrime(bitlength, r);
        BigInteger q = BigInteger.probablePrime(bitlength, r);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.probablePrime(bitlength / 2, r);

        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0){
            e.add(BigInteger.ONE);
        }

        BigInteger d = e.modInverse(phi);
        BigInteger[] keys = {e, d, n};

        generateBtn.setText("Generated");
        generateBtn.setEnabled(false);
        return keys;
	}
	public static void showPopup(){
		JOptionPane jPane = new JOptionPane();
        jPane.showMessageDialog(null, "Keys are not generated", "Error", JOptionPane.INFORMATION_MESSAGE);
	}
}