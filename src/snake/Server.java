package snake;



	

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    	private static ArrayList<DataInputStream> in_stream = new ArrayList<DataInputStream>();
    	private static ArrayList<Handler> sockets = new ArrayList<Handler>();
    	
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
	        String name;
	        PrintWriter outP ;
            BufferedReader inB ;

	        public Handler(Socket socket) {
	            this.socket = socket;
	            sockets.add(this);
	            
	        }

	        public void run() {
	            try {
	            	System.out.println("Aqui");
	            	
	            	in = new DataInputStream(socket.getInputStream());
	                out = new DataOutputStream(socket.getOutputStream());
	                
	                outP = new PrintWriter(socket.getOutputStream(), true);
	                inB = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                
	                out_stream.add(out);
	                in_stream.add(in);
	                
	                int posicao = 0;
	                
	                String playerName = "Snake" + NPLAYERS;
	                this.name = playerName;
	                // Keep requesting a new movement until we get a unique one.
	                System.out.println(playerName);
	                out.writeUTF(playerName);
	                //snakeMap.put(playerName, new Snake());
	                out.writeInt(jogadores);
	                
	                int prontos=0;
	                

	                	
	                	int inp = in.readInt();
	                	for(Handler s: sockets){
	                		if(!playerName.equals(s.name)) s.out.writeInt(1);
		            	}
	                	prontos+= inp;

	                
	                //System.out.println(in.read());
	                System.out.println(in.available());
	                	
	                	
	                String info;
	                while (true) {
	                	if(in.available()>0){
		                	System.out.println("Q");
		                	
		                	//info = inB.readLine();
			                playerName = in.readUTF();
			                System.out.println("playerName "+ playerName);
			                System.out.println("Depois");
			                //posicao = in.readInt();
	
		                	System.out.println(playerName+" " + posicao);
	
		                	for(Handler s: sockets){
		                		
		                		if(!playerName.equals(s.name)) s.out.writeUTF(playerName);
			            	}
	                	}
	                }


	            } catch (Exception e) {
	                System.out.println(e);
	            } finally {
	                try { socket.close(); } catch (IOException e) {}
	            }
	        }
	    }
	
	
	
}
