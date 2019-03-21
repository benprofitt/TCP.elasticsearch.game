package tcp_elastic_game;
import java.net.*;

import org.elasticsearch.client.RestHighLevelClient;
// import org.elasticsearch.common.*;

import java.io.*;

public class Server {

	// Used to communicate with the client
	private Socket socket = null;

  // Used to connect with ES
  // private RestHighLevelClient client = null;
	// Used to communicate with the server
	private ServerSocket server = null;

	private DataInputStream in = null;

	private DataOutputStream out = null;

	public Server(int port) {
		try {

			connect_to_ES();

			server = new ServerSocket(port);
			System.out.println("Opened server on port " + Integer.toString(port));


			String line = "";

			while (!line.equals("Over")) {

				line = "";

				System.out.println("Waiting for connection from client");

				socket = server.accept();
				in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				out = new DataOutputStream(socket.getOutputStream());

				System.out.println("Connection successful");


				// This should allow for multiple sessions (non-parallel) from clients
				while (!line.equals("Over") && !line.equals("done")) {

					try {

						line = in.readUTF();



						System.out.println(line);

					} catch (Exception e) {
						System.out.println(e);
					}

				}

				out.writeUTF("Server: Completed single request\n");

				out.writeUTF("Server: Closing connection");

				socket.close();
				in.close();
				out.close();

			}

			System.out.println("Closing port");

			server.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String args[]) {
		Server server = new Server(5000);
	}

	private void connect_to_ES() {

	}

}
