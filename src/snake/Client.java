package snake;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    PrintWriter outP;
    BufferedReader inB;
 
 
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
	        	   String info = playerName+" " +Integer.toString(key);
	        	  
	        	   
	        	   outP.print(info);
	        	   out.writeUTF(info);
	        	   System.out.println("imprimiu");
	        	   //out.writeUTF(info);
	        	   //out.writeInt(key);
	        	   //out.flush();
	        	   System.out.println("info "+ info);
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
	
	Board jogo;
	Input(Board jogo){
		
		this.jogo = jogo;
	}
	
	public void run() {
	int posicao;
		while(true) {
    		try {
    			System.out.println("Tentando ler");
    			String leitura;
	        	leitura = in.readUTF();
	        	System.out.println("leitura: "+leitura);
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
            
            outP = new PrintWriter(socket.getOutputStream(), true);
            inB = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String snake = (String)in.readUTF();
            playerName = snake;
            System.out.println("Cliente recebeu "+ snake);
            int players = in.readInt();
            
            int prontos=0;
           
            int r = JOptionPane.showConfirmDialog(null, "Pronto?");
            if (r == JOptionPane.YES_OPTION) {
            	
            	out.writeInt(1);
            	prontos++;
            }
            //System.out.println("Prontos"+prontos+" players "+players);
            
            
            
            
            while(prontos<players){
            	int inp = in.readInt();	 
            	
            	prontos+= inp;
            	System.out.println("prontos"+prontos);
            }
            
            System.out.println("Prontos"+prontos+" players "+players);
            
            
            Board jogo = new Board(players);

            startsGame(jogo,this.ex);
            
            
            
           

            Input i = new Input(jogo);
            
            Teclado t = new Teclado(jogo,playerName);
            
            i.start();
            t.start();
            

            
        } catch(Exception err) {
        	System.out.println(err.getMessage());
        }
    }



}