import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {
	public static void main(String[]args) throws IOException, InterruptedException{
		if(args[0].equals("-server")){
			ServerSocket socket = new ServerSocket(2222);
			while(true){
			new Thread(new ServerThread(socket.accept())).start();
			}
		}
		if(args[0].equals("-client"))
		{
			new Client(2222).run();
		}
	}
}

class Client{
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	
	public Client(int port) throws IOException{
		socket = new Socket();
		socket.connect(new InetSocketAddress("localhost",2222));
	}
	public void run() throws IOException, InterruptedException{
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
		while(true){
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("msg:"+input.readLine());
				output.write("OK");
				Thread.sleep(3000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
		/*ExecutorService executor = Executors.newFixedThreadPool(2);
		Runnable emission = new Runnable(){
			public void run(){
				while(true){
					output.println("Hello");
				}
			}
		};
		Runnable reception = new Runnable(){
			public void run(){
				while(true){
					try {
						System.out.print("msg"+input.readLine());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		executor.execute(emission);
		executor.execute(reception);
		executor.shutdown();
		while(!executor.isTerminated()){
			Thread.sleep(3000);
		}
	}
	*/
}
class ServerThread implements Runnable {
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			while(true){
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.print(input.readLine());
				output.println("Hello"+System.currentTimeMillis());
				output.flush();
				
			}
			/*ExecutorService executor = Executors.newFixedThreadPool(2);
			Runnable emission = new Runnable(){
				public void run(){
					while(true){
						output.println("Hello");
					}
				}
			};
			Runnable reception = new Runnable(){
				public void run(){
					while(true){
						try {
							System.out.print("msg"+input.readLine());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			executor.execute(emission);
			executor.execute(reception);
			executor.shutdown();
			while(!executor.isTerminated()){
				Thread.sleep(3000);
			}
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}