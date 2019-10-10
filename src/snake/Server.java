package snake;



	
	import java.io.IOException;
	import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
	import java.net.Socket;
	import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


	public class Server {

		public boolean clienteConectado = false;
		public static int jogadores;
		public static int NPLAYERS = 0;
		public static String ipServer;
    	private final Map<String, Snake> snakeMap = new HashMap<String, Snake>();
		
    	
    	Server(int jogadores){
    		Server.jogadores = jogadores;
    	}
    	
    	
    	

	    public  void inicia() throws Exception {
	        System.out.println("Server is running...");
	         ExecutorService pool = Executors.newFixedThreadPool(500);
	        try (ServerSocket listener = new ServerSocket(59001)) {
	        	//ipServer = listener.getInetAddress().toString();
	            while (NPLAYERS<=jogadores) {
	            	
	            	NPLAYERS += 1;
	                pool.execute(new Handler(listener.accept()));
	                System.out.println(NPLAYERS);
	                System.out.println(jogadores);
	            }
	            System.out.println("aqui");
	        }
	    }

	    /**
	     * The client handler task.
	     */
	    private static class Handler implements Runnable {
	    	
	        private Socket socket;
	        private Scanner in;
	        private PrintWriter out;

	        /**
	         * Constructs a handler thread, squirreling away the socket. All the interesting
	         * work is done in the run method. Remember the constructor is called from the
	         * server's main method, so this has to be as short as possible.
	         */
	        public Handler(Socket socket) {
	            this.socket = socket;
	        }

	        
	        
	        
	        
	        /**
	         * Services this thread's client by repeatedly requesting a screen name until a
	         * unique one has been submitted, then acknowledges the name and registers the
	         * output stream for the client in a global set, then repeatedly gets inputs and
	         * broadcasts them.
	         */
	        public void run() {
	            try {
	                in = new Scanner(socket.getInputStream());
	                out = new PrintWriter(socket.getOutputStream(), true);

	                int posicao;
	                
	                // Keep requesting a new movement until we get a unique one.
	                while (true) {
	                    posicao = in.nextInt();
	                    
	                    out.println();
	                    
	                    
	                }

	                // Now that a successful name has been chosen, add the socket's print writer
	                // to the set of all writers so this client can receive broadcast messages.
	                // But BEFORE THAT, let everyone else know that the new person has joined!
	                


	            } catch (Exception e) {
	                System.out.println(e);
	            } finally {
	                try { socket.close(); } catch (IOException e) {}
	            }
	        }
	    }
	
	
	
}
