package snake;



	
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


	public class Server {

		public boolean clienteConectado = false;
		public static int jogadores;
		public static int NPLAYERS = 0;
		public static String ipServer;
    	//public static Map<String, Snake> snakeMap = new HashMap<String, Snake>();
    	private static ArrayList<DataOutputStream> out_stream = new ArrayList<DataOutputStream>();
    	
    	
    	Server(int jogadores){
    		Server.jogadores = jogadores;
    	}
    	


	    public  void inicia() throws Exception {
	        System.out.println("Server is running...");
	         ExecutorService pool = Executors.newFixedThreadPool(500);
	        try (ServerSocket listener = new ServerSocket(59001)) {
	        	//ipServer = listener.getInetAddress().toString();
	            while (NPLAYERS<=jogadores) {
	            	
	            	
	                pool.execute(new Handler(listener.accept()));
	                NPLAYERS += 1;
	                System.out.println(NPLAYERS);
	                //System.out.println(jogadores);
	            }
	            System.out.println("aqui");
	        }
	    }

	    /**
	     * The client handler task.
	     */
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
	            	System.out.println("Aqui");
	            	in = new DataInputStream(socket.getInputStream());
	                out = new DataOutputStream(socket.getOutputStream());
	                out_stream.add(out);
	                
	                int posicao;
	                String playerName = "Snake" + NPLAYERS;
	                // Keep requesting a new movement until we get a unique one.
	                System.out.println(playerName);
	                out.writeUTF(playerName);
	                //snakeMap.put(playerName, new Snake());
	                out.writeInt(jogadores);
	                
	                int prontos=0;
	                
	                //iniciaJogo();
	                
	                while(prontos<jogadores){
	                	
	                	int inp = in.readInt();	
	                	for(DataOutputStream o:out_stream){
		            		o.writeInt(1);
		            	}
	                	prontos+= inp;
	                	out.writeInt(prontos);
	                }
	                
	                
	                
	                
	                
	                while (true) {
	                	
	                	playerName = in.readUTF();
	                	posicao = in.readInt();
	                	System.out.println(playerName+" " + posicao);
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
