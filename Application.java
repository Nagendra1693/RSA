import java.util.*;
import java.math.*;
import java.io.*;

import javax.swing.*;  
import java.awt.event.*;

import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.*;

public class Application implements DataSource{

    // Methods needed for saving bytes to file 
    private byte[] _data; 
    private java.lang.String _name; 

    public Application(byte[] data, String name) { 
        _data = data; 
        _name = name;
    }
    public String getContentType() { return "application/octet-stream";} 
    public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(_data);}
    public String getName() { return _name;}
    public OutputStream getOutputStream() throws IOException { 
        OutputStream out = new ByteArrayOutputStream(); 
        out.write(_data); 
        return out;
    }
    // Ends here

    public static void main(String[] args) {  
        JFrame f= new JFrame();

        JLabel l1 = new JLabel();  
        l1.setBounds(50, 50, 80, 30);
        l1.setText("To :");

        JLabel l2 = new JLabel();  
        l2.setBounds(50, 170, 80, 30);
        l2.setText("Message : ");

        JTextField tfTo = new JTextField();  
        tfTo.setBounds(140, 50, 250, 30);

        JTextArea area=new JTextArea();  
        area.setBounds(140, 170, 250, 150);

        JLabel statusLabel = new JLabel();
        statusLabel.setBounds(280, 370, 150, 30);

        JButton b=new JButton("Send");  
        b.setBounds(140, 370, 100, 30);
        b.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                String toAddr = tfTo.getText();
                String message = area.getText();
                boolean status = sendMail(toAddr,message);
                if(status == true){
                    statusLabel.setText("Mail sent");
                }else{
                    statusLabel.setText("Failed to send");
                }
            }  
        });

        f.add(l1);
        f.add(l2);
        f.add(tfTo);
        f.add(area);
        f.add(b);
        f.add(statusLabel); 
        f.setSize(450, 450);  
        f.setLayout(null);  
        f.setVisible(true);  
    }

    public static byte[] encrypt(String message){
        BigInteger n = new BigInteger("11498995174877243420082175312161167309422745982283778484994578543655350852017290344784030789845832459927762651561055357066442366378781154459169323762292445903710082992163337471872562308425285472363221260891120023193405099331209584555812393870067536808282739421035677031821924193007986092657100848598918298543395891189680268512319584408638357310468073970368491225869817830359614975960771370829902138453334836481918286406208900312365047256888572385015472867315536978484226851475211359872680218125276940936238591846167071845600032046377932783452872893618318930070303290584876422631870758343508276794799470402025618693857");
        BigInteger e = new BigInteger("12974196090353888161867276411051314732266909087852766091030387154483266219438376294439538954117184810179758429633641913932326414449809712073609367371892483");
      
        byte[] plainBytes = message.getBytes();
        byte[] encryptBytes = (new BigInteger(plainBytes)).modPow(e,n).toByteArray();

        return encryptBytes;
    }

    public static boolean sendMail(String toAddr,String plainMessage){

        String from = "bproject82@gmail.com";
        String pass = "project123";
        String[] to = { toAddr };

        String subject = "Encrypted mail";
        String body = "Decrypt with your key";

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("This is message body");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();            
            
            Application bds = new Application(encrypt(plainMessage), "message.txt");
            messageBodyPart.setDataHandler(new DataHandler(bds));
            messageBodyPart.setFileName(bds.getName());
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        }
        catch (Exception ae) {
            return false;
        }
    }
}  