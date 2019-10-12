package snake;



	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


	public class Server {

		public boolean clienteConectado = false;
		public static int jogadores;
		public static int NPLAYERS = 0;
		public static String ipServer;
    	public static Map<String, Snake> snakeMap = new HashMap<String, Snake>();
		
    	
    	Server(int jogadores){
    		Server.jogadores = jogadores;
    	}
    	

	    public  void inicia() throws Exception {
	        System.out.println("Servidor está rodando...");
	         ExecutorService pool = Executors.newFixedThreadPool(500);
	        try (ServerSocket listener = new ServerSocket(59001)) {
	        	//ipServer = listener.getInetAddress().toString();
	            while (NPLAYERS<=jogadores) {
	            	
	            	
	                pool.execute(new Handler(listener.accept()));
	                NPLAYERS += 1;
	                //System.out.println(NPLAYERS);
	                //System.out.println(jogadores);
	            }
	          
	        }
	    }


	    private static class Handler implements Runnable {
	    	
	        private Socket socket;
//	        private Scanner in;
//	        private PrintWriter out;
	        DataInputStream in;
	        DataOutputStream out; 
	        


	        public Handler(Socket socket) {
	            this.socket = socket;
	        }

	        
	        
	        
	        

	        public void run() {
	            try {
	            	
	            	in = new DataInputStream(socket.getInputStream());
	                out = new DataOutputStream(socket.getOutputStream());
	                int posicao;
	                String playerName = "Snake" + NPLAYERS;
	                // Keep requesting a new movement until we get a unique one.
	                //System.out.println(playerName);
	                out.writeUTF(playerName);
	                snakeMap.put(playerName, new Snake());
	                
	                
	                while (true) {
	                	
	                	playerName = in.readUTF();
	                	posicao = in.readInt();
	                	//System.out.println(playerName+" " + posicao);
	                	out.writeUTF(playerName+" " + posicao);
	                }


	                


	            } catch (Exception e) {
	                System.out.println(e);
	            } finally {
	                try { socket.close(); } catch (IOException e) {}
	            }
	        }
	    }
	
	
	
}
