import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {
	public static void main(String[]args) throws IOException, InterruptedException{
		if(args[0].equals("-server")){
			new Server().runServer();
		}
		if(args[0].equals("-client"))
		{
			new Client(Server.listenningPort).runClient();
		}
	}
}

class Client{
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	public Client(int port) throws IOException{
		socket = new Socket("localhost",port);
	}
	public void runClient() throws IOException, InterruptedException{
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(),true);
		while(true){
			output.println(System.currentTimeMillis());
			Thread.sleep(3000);
		}
	}	
}
class Server{
	public static final int listenningPort = 1234;
	public void runServer() throws IOException{
		ExecutorService executor = Executors.newFixedThreadPool(4);
		ServerSocket server = new ServerSocket(listenningPort);
		while(true){
			Socket newClient = server.accept();
			Runnable runnable = new ServerThread(newClient);
			executor.execute(runnable);
		}
	}
}
class ServerThread implements Runnable {
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	private static int number = 0;
	public ServerThread(Socket socket){
		this.socket = socket;
		number++;
	}
	
	public void run(){
		try {
			String user = "user"+number;
			System.out.println(user+" is connected from the port "+socket.getPort());
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			while(true){
				System.out.println(user+": "+input.readLine());	
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
				input.close();
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
}