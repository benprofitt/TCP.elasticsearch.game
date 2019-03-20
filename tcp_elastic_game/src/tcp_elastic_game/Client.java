package tcp_elastic_game;
import java.net.*;
import java.util.Scanner;
import java.io.*; 

public class Client {
	
	// Used to connect to the server
	private Socket socket = null;
	private BufferedReader server_input = null;
	private DataOutputStream out = null;
	private Scanner sc = null;
	

	
	public Client(String ip_addr, int port) {
		
		// Attempts to connect
		try {
			// Opening a connection to the given address through the provided port via TCP
			socket = new Socket(ip_addr, port);
			System.out.println("Successful Connection");
			
			// Prepares to take input from the user of the client (via std in)
			sc = new Scanner(System.in);
			
			// Prepares to read input returned from the server
			server_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Prepares to feed output to the server via the open connection on the socket
			out = new DataOutputStream(socket.getOutputStream());
			
		} catch(UnknownHostException u) {
			System.out.println(u);
		} catch(IOException e) {
			System.out.println(e);
		}
		
		String line = "";
		
		while (!line.equals("done") && !line.equals("Over")) {
			try {
				System.out.println("Here Client");
				// Get the value of each line and feed it to the server via the socket
				line = sc.nextLine();
				
				out.writeUTF(line);
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		try {
			
			// Get the results of our query or game from the server
			
			String results = server_input.readLine();			
			
			System.out.println(results);
			
			String message = server_input.readLine();			
			
			System.out.println(message);
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		// Close the connection
		try {
			
			socket.close();
			sc.close();
			server_input.close();
			out.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public static void main(String argv[]) {
		Client client = new Client("127.0.0.1", 5000);
	}
	
}
