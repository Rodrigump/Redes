package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


//import snake.Board.TAdapter;

public class Client {

    String serverAddress;
    //Scanner in;
    DataInputStream in;
    DataOutputStream out;
    //PrintWriter out;
   String playerName;
    MainScreen screen;
    

    public Client(String serverAddress) {
    	
        this.serverAddress = serverAddress;
       // MainScreen.ex.pack();


    }

    private static void startsGame(JPanel jogo) {
		
		MainScreen.ex.getContentPane().removeAll();
        MainScreen.ex.getContentPane().invalidate();
        MainScreen.ex.getContentPane().add(jogo);
        MainScreen.ex.getContentPane().revalidate();
       // jogo.requestFocusInWindow();
        MainScreen.ex.setVisible(true);
		
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
    			String leitura;
	        	leitura = in.readUTF();
	        	String[] temp = new String[2];
	        	temp = leitura.split(" ");
	        	posicao = Integer.parseInt(temp[1]);
	        	System.out.println("Recebeu "+temp[0]+" "+posicao+" Player = "+playerName);
	        	if(!temp[0].equals(playerName))	jogo.atualizaPosicao(posicao,temp[0]);
	        	//System.out.println("CC"+posicao);
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

            Board jogo = new Board();
            String snake = (String)in.readUTF();
            playerName = snake;
            System.out.println("Cliente recebeu "+ snake);
            
            
            
            while(Server.NPLAYERS<=MainScreen.N_PLAYERS){
            	System.out.println(Server.NPLAYERS);
            	System.out.println(MainScreen.N_PLAYERS);
            	if(Server.NPLAYERS==MainScreen.N_PLAYERS) {
            		
                	startsGame(jogo);
                	break;
                }else{
                	continue;
                }
            }
            
            
           

            Input i = new Input(in,jogo);
            Teclado t = new Teclado(jogo,playerName);
            
            i.start();
            t.start();
            

            
        } catch(Exception err) {
        	System.out.println(err.getMessage());
        }
    }
    
    private void atualizaPosicao(int key) {
    	
    }


}