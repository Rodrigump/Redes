package snake;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;



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

    private static void startsGame() {
		
		MainScreen.ex.getContentPane().removeAll();
        MainScreen.ex.getContentPane().invalidate();
        MainScreen.ex.getContentPane().add(Server.jogo);
        MainScreen.ex.getContentPane().revalidate();
       // jogo.requestFocusInWindow();
        MainScreen.ex.setVisible(true);
		
	}

    
  class Teclado extends Thread{
	
	String playerN;
	
	Teclado(String playerN){
		
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
	        	   //Server.jogo.atualizaPosicao(key,playerName);

	           } catch (Exception err) {
	        	   System.out.println(err.getMessage());
	           }

	        }
	    }
	
	public void run() {
		
		Server.jogo.addKeyListener(new TAdapter());
		
	}
	

}

class Input extends Thread{
	DataInputStream in;
	//Board jogo;
	Input(DataInputStream in){
		this.in = in;
		
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
	        	if(!temp[0].equals(playerName))	Server.jogo.atualizaPosicao(posicao,temp[0]);
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

            String snake = (String)in.readUTF();
            playerName = snake;
            System.out.println("Cliente recebeu "+ snake);
            int totalPlayers = in.readInt();
            System.out.println(totalPlayers);
            
            int r = JOptionPane.showConfirmDialog(null, "Iniciar o jogo?");
            
            if (r == JOptionPane.YES_OPTION) {
            	out.writeInt(1);
                
            	
            } 
            
            
            int podeComecar=0;
            int NPLAYERS=0;
            
            
            
            System.out.println("fora" +podeComecar + " "+ NPLAYERS);
            while(podeComecar<= NPLAYERS && NPLAYERS == totalPlayers){
            	System.out.println("dentro" +podeComecar + " "+ NPLAYERS);
                podeComecar = in.readInt();
                NPLAYERS = in.readInt();

            }
            
            
         if(NPLAYERS==podeComecar && NPLAYERS == totalPlayers) {
    		System.out.println("A");
    		
        	startsGame();
         }
            
            
           System.out.println("AAAA");

            Input i = new Input(in);
            Teclado t = new Teclado(playerName);
            
            i.start();
            t.start();
            

            
        } catch(Exception err) {
        	System.out.println(err.getMessage());
        }
    }
    
    private void atualizaPosicao(int key) {
    	
    }


}