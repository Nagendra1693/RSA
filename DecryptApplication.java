import java.util.*;
import java.math.*;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DecryptApplication{
    static String path = "";
    static JLabel fileLabel;
    static JTextArea area;


    public static void main(String[] args) {
        

        JFrame f= new JFrame("Decryption");

        JButton fBtn = new JButton("Select file");
        fBtn.setBounds(50, 50, 100, 30);
        
        fileLabel = new JLabel();  
        fileLabel.setBounds(200, 50, 200, 30);
        fileLabel.setText("No file selected");

        JButton dBtn = new JButton("Decrypt");
        dBtn.setBounds(180, 120, 80, 30);

        area=new JTextArea();  
        area.setBounds(40, 180, 400, 200);

        fBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String[] data = openFileDialog();
                String file = data[0];
                path = data[1];
                fileLabel.setText("Selected : "+file);
            }
        });

        dBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(!path.contentEquals("")){
                    if(!decryptFile(path)){
                        openDialog("Failed to decrypt the file");
                    }
                }else{
                    openDialog("Select a file");
                }
                
            }
        });


        f.add(fBtn);
        f.add(fileLabel);
        f.add(dBtn);
        f.add(area); 
        f.setSize(500,500);  
        f.setLayout(null);  
        f.setVisible(true);  
    }

    public static String[] openFileDialog(){
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        File dir = new File( dialog.getDirectory() );
        String path = new File( dir, file).getAbsolutePath();
        String[] data = {file, path};
        return data;
    }
    public static boolean decryptFile(String filePath){
        BigInteger n = new BigInteger("Your Common value");
        BigInteger d = new BigInteger("Your Decryption key");

        try{
            byte[] encryptedBytes = Files.readAllBytes(new File(filePath).toPath());
            byte[] decryptBytes = (new BigInteger(encryptedBytes)).modPow(d,n).toByteArray();
            
            String decryptedMessage = new String(decryptBytes);
            area.setText(decryptedMessage);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    public static void openDialog(String message){
        JOptionPane jPane = new JOptionPane();
        jPane.showMessageDialog(null, message, "Error", JOptionPane.INFORMATION_MESSAGE);
    }
}