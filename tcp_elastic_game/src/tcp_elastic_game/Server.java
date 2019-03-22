package tcp_elastic_game;
import java.net.*;
import java.util.*;
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

			server = new ServerSocket(port);
			System.out.println("Opened server on port " + Integer.toString(port));


			String line = "";
			String param = "";

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

						URL url = new URL("localhost:9200");
						HttpURLConnection con = (HttpURLConnection) url.openConnection();

						Map<String, String> parameters = new HashMap<String, String>();

						if (line == "query") {
							//build a query through an HTTP request
							con.setRequestMethod("GET");
							while (!line.equals("Over") && !line.equals("done")) {
								line = in.readUTF();
								param = in.readUTF();
								parameters.put(line, param);
							}
						} else {
							//Add data via a JSON structure
							con.setRequestMethod("POST");

						}

						con.setDoOutput(true);
						DataOutputStream elastic_out = new DataOutputStream(con.getOutputStream());
						out.writeBytes(getParamsString(parameters));
						out.flush();
						out.close();

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

  public static String getParamsString(Map<String, String> params)
    throws UnsupportedEncodingException{
      StringBuilder result = new StringBuilder();

      for (Map.Entry<String, String> entry : params.entrySet()) {
        result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        result.append("&");
      }

      String resultString = result.toString();
      return resultString.length() > 0
        ? resultString.substring(0, resultString.length() - 1)
        : resultString;
		}
}
