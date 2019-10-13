package snake;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


//import snake.Board.TAdapter;

public class Client {

    String serverAddress;
    //Scanner in;
    DataInputStream in;
    DataOutputStream out;
    //PrintWriter out;
   String playerName;
    MainScreen screen;
    public JFrame ex;

    public Client(String serverAddress, JFrame ex) {
    	this.ex = ex;
        this.serverAddress = serverAddress;
       // MainScreen.ex.pack();


    }

    private static void startsGame(JPanel jogo,JFrame ex) {
		
		ex.getContentPane().removeAll();
        ex.getContentPane().invalidate();
        ex.getContentPane().add(jogo);
        ex.getContentPane().revalidate();
      
       
        //jogo.requestFocusInWindow();
        ex.setVisible(true);
		
	}

    
  class Teclado extends Thread{
	Board jogo;
	String playerN;
	
	Teclado(Board jogo,String playerN){
		this.jogo = jogo;
		this.playerN = playerN;
	}
	
	 final class TAdapter extends KeyAdapter {
	    	
	        @Override
	        public void keyPressed(KeyEvent e) {
	        	
	            int key = e.getKeyCode();
	            //System.out.println(key);
	           try {
	        	   out.writeUTF(playerName);
	        	   out.writeInt(key);
	        	   jogo.atualizaPosicao(key,playerName);

	           } catch (Exception err) {
	        	   System.out.println(err.getMessage());
	           }

	        }
	    }
	
	public void run() {
		
		jogo.addKeyListener(new TAdapter());
		
	}
	

}

class Input extends Thread{
	DataInputStream in;
	Board jogo;
	Input(DataInputStream in,Board jogo){
		this.in = in;
		this.jogo = jogo;
	}
	
	public void run() {
	int posicao;
		while(true) {
    		try {
    			System.out.println("Tentando ler");
    			String leitura;
	        	leitura = in.readUTF();
	        	String[] temp = new String[2];
	        	temp = leitura.split(" ");
	        	posicao = Integer.parseInt(temp[1]);
	        	if(!temp[0].equals(playerName))	jogo.atualizaPosicao(posicao,temp[0]);
	        	System.out.println("CC"+posicao);
	    		}catch(Exception err) {
	    			System.out.println(err.getMessage());
	    		}
		}
	}
	
}
   
    public void run() throws IOException {
        try {

            Socket socket = new Socket(serverAddress, 59001);
            
            System.out.println("conectado");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
           
            
            String snake = (String)in.readUTF();
            playerName = snake;
            System.out.println("Cliente recebeu "+ snake);
            int players = in.readInt();
            
            int prontos=-1;
           
            int r = JOptionPane.showConfirmDialog(null, "Pronto?");
            if (r == JOptionPane.YES_OPTION) {
            	
            	out.writeInt(1);
            }
            //System.out.println("Prontos"+prontos+" players "+players);
            
            
            
            
            while(prontos<players){
            	int inp = in.readInt();	 
            	
            	prontos+= inp;
            	//System.out.println("prontos"+prontos);
            }
            //System.out.println("Prontos"+prontos+" players "+players);

            
            Board jogo = new Board(players);

            startsGame(jogo,this.ex);
            
            in.close();
            in = new DataInputStream(socket.getInputStream());
            
           

            Input i = new Input(in,jogo);
            Teclado t = new Teclado(jogo,playerName);
            
            i.start();
            t.start();
            

            
        } catch(Exception err) {
        	System.out.println(err.getMessage());
        }
    }



}