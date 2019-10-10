package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");

    

    public Client(String serverAddress,JFrame frame) {
        this.serverAddress = serverAddress;
        this.frame = frame;
        
        frame.pack();


    }

    

    public void run() throws IOException {
        try {
        	
            Socket socket = new Socket(serverAddress, 59001);
            
            System.out.println("conectado");
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            
        } catch(Exception err) {
        	System.out.println(err.getMessage());
        }
    }


}