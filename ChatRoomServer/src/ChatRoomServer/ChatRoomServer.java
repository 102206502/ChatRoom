package ChatRoomServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoomServer {
	private Socket connection;
	private final int PORT = 3100;
	//private final String IP = "192.168.1.111";
	private ServerSocket serverSocket;
	private Thread serverThread;
	private BufferedReader input;
	private BufferedWriter output;
	private int counter = 1; // counter for number of connections
	private String usermsg = "";
	private BufferedReader keyInput;

	public static void main(String[] args) {
		new ChatRoomServer();
	}
	
	public ChatRoomServer() {
		runServer();
	}
	
	private void runServer() {
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server start.");
			
			try {
				waitForConnection();
				getStreams();
				processConnection();
				runServerSending();
			} catch(EOFException e) {
				System.out.println("Server terminated connection.");
			} finally {
				closeConnection();
				counter++;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void waitForConnection() throws IOException {
		System.out.println("waiting...");
		connection = serverSocket.accept();
		System.out.println("Connection " + counter + " recieve from " 
				+ connection.getInetAddress().getHostAddress());
	}
	
	private void getStreams() throws IOException {
		output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
		input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	}
	
	private void processConnection() throws IOException {
		System.out.println("Connection sucessful.");
		serverThread = new ServerListeningThread(input, connection);
		serverThread.start();
		if(serverThread.isAlive()) {System.out.println("Start getting message from client...");}
		else {System.out.println("Server thread dead.");}
	}
	
	private void runServerSending() throws IOException {

		keyInput = new BufferedReader(new InputStreamReader(System.in));
		
		usermsg = "Server>> Here is message from server.";
		try {
			output.write(usermsg);
			output.flush();
			
			while(!usermsg.equals("Server>> terminate")) {
				System.out.print("Server>> ");
				usermsg = "Server>> " + keyInput.readLine();
				if(!usermsg.equals("")) {
					output.write(usermsg);
					output.flush();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void closeConnection() {
		System.out.println("Terminating connection...");
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
