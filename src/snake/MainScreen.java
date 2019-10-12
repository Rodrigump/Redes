package snake;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainScreen {
	static JFrame ex = new Frame();
	/**
	 * Create the panel.
	 */
	public JPanel pane;
	public static int N_PLAYERS;
	
	public MainScreen() {
		
	       this.pane = painel();
		}
	
	
	private static class Servidor extends Thread{
		
		
		
		
		
		public void run() { 
			Server servidor = new Server(N_PLAYERS);
	        try{ 
	        	servidor.inicia();	
	        	// Displaying the thread that is running 
	            System.out.println ("Thread " + 
	                  Thread.currentThread().getId() + 
	                  " is running"); 
	  
	        } 
	        catch (Exception e) 
	        { 
	            // Throwing an exception 
	            System.out.println ("Exception is caught"); 
	        } 
	    } 
		
		
		
	}
	
		
	
	
	
	
	
	static private JPanel painel() {
		JPanel painel = new JPanel();
		 BoxLayout boxlayout = new BoxLayout(painel, BoxLayout.Y_AXIS);
	        painel.setLayout(boxlayout);

			
	        painel.setBackground(Color.black);
	        painel.setFocusable(true);

	        painel.setPreferredSize(new Dimension(500, 500));
		

	        JButton hospedar = new JButton("Hospedar um Jogo");
	        hospedar.setFocusable(false);
			JButton conectar = new JButton("Conectar a um Jogo existente");
	        
	        hospedar.setAlignmentX(Component.CENTER_ALIGNMENT);
	        conectar.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
			hospedar.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO chama o servidor e conecta o cliente desse jogador ao servidor com localhost

					N_PLAYERS = Integer.parseInt(JOptionPane.showInputDialog("Qual é o número de jogadores?"));
					
					
					JPanel board = new Board();
					Board.NPLAYERS = N_PLAYERS;
					
					Servidor serv = new Servidor();
					serv.start();
					
					String ipServer = "";
					String[] ipS = new String [2];
					try {
						ipServer = InetAddress.getLocalHost().toString();
						
						ipS = ipServer.split("/");
					} catch(Exception err){
						
					}
					JOptionPane.showMessageDialog(null, "Informar o ip: " + ipS[1]);
					System.out.println(ipS[1]);
					Client cliente = new Client(ipS[1]);
					
					try {
						cliente.run();	
					} catch (Exception err) {
						System.out.println(err.getMessage());
					}
					
					
					//startsGame(board);
					
				}
				
			});
			
			
			
			
			conectar.addActionListener(new ActionListener(){
		
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					String ip = JOptionPane.showInputDialog("Qual é o ip do servidor?");
					//System.out.println(ip);
					Client cliente = new Client(ip);
					try {
						cliente.run();	
					} catch (Exception err) {
						System.out.println(err.getMessage());
					}
					
				}
				
			});
			
			painel.add(hospedar);
			painel.add(conectar);
		
		
		return painel;
	}
	
public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                
                ex.setVisible(true);                
            }
        });
        
        
        
        
    }
	

	
}
