package ChatRoomServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerListeningThread extends Thread {
	
	private String usermsg = "";
    private BufferedReader clientin;
    private Socket socket;
    
    public ServerListeningThread(BufferedReader input, Socket connect) throws IOException {
        clientin = input;
        socket = connect;
    }
    
    @Override
    public void run() {
    	while(usermsg != "terminate" || !socket.isClosed()) {
    		try {
    			usermsg = clientin.readLine();
    			if(usermsg != null)
    				System.out.println("\nClient>> " + usermsg);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }

}
