import java.util.*;
import java.math.*;
import java.io.*;

import javax.swing.*;  
import java.awt.event.*;

import javax.mail.*;
import javax.mail.internet.*;

import javax.activation.*;

public class Application implements DataSource{

    static String USERNAME = /*Enter your gmail username*/;
    static String PASSWORD = /*Enter the gmail password*/;

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
        JFrame f= new JFrame("Encryption");

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
        BigInteger n = new BigInteger("Your Common value");
        BigInteger e = new BigInteger("Your Encryption key");
      
        byte[] plainBytes = message.getBytes();
        byte[] encryptBytes = (new BigInteger(plainBytes)).modPow(e,n).toByteArray();

        return encryptBytes;
    }

    public static boolean sendMail(String toAddr,String plainMessage){

        String from = USERNAME;
        String pass = PASSWORD;
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