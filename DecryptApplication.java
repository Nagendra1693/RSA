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
        BigInteger n = new BigInteger("11498995174877243420082175312161167309422745982283778484994578543655350852017290344784030789845832459927762651561055357066442366378781154459169323762292445903710082992163337471872562308425285472363221260891120023193405099331209584555812393870067536808282739421035677031821924193007986092657100848598918298543395891189680268512319584408638357310468073970368491225869817830359614975960771370829902138453334836481918286406208900312365047256888572385015472867315536978484226851475211359872680218125276940936238591846167071845600032046377932783452872893618318930070303290584876422631870758343508276794799470402025618693857");
        BigInteger d = new BigInteger("5641533795442417158416281254553309094371952551284075093226744210723367489350781617249277769126985226765018057652641920158242876619416989836648543104022834485870282485705204640688319869001504840874490362364643791459299329785676377074092435320263976475930268645971300972616737308339830922491783773026198730129453899961475271187488976336884912950419795076940108709578149982368465501655627834640908342651542822174829373691895811600950327669048513810602949403164161970828067640445152833841437482701491806504096432183041858710067871479616514995253687875200231809293564767119486232104267375036932416222410976447239257210347");

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