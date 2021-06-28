package ex1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * 
 * this is the server class 
 * it creates a server thread for every 2 clients connected
 *
 */

public class MemoryGameServer {
	
	public static void main(String[] args) {
		// connection components
		ServerSocket serverSocket = null;
		Socket socketA = null;
		Socket socketB = null;
		//listening is always true while the server is on
		boolean listening = true;
		
		// creating socket
		try {
			serverSocket = new ServerSocket(7777);
		}catch(IOException e) {
			System.out.println("cant create server socket");
			System.exit(1);
		}
		
		System.out.println("Waiting players");
		while(listening) {
			// wait for 2 players
			try {
				socketA = serverSocket.accept();
				System.out.println("Player one connected");
				socketB = serverSocket.accept();
				System.out.println("Player 2 connected");
				
				// create a server thread to handle the 2 players
				(new MemoryGameThread(socket1, socket2)).start();
				
			}catch(IOException e) {
				System.out.println("cant accept sockets");
				System.exit(1);
			}
		}
		try {
			serverSocket.close();
		}catch(IOException e) {
			System.out.println("cant close server socket");
			System.exit(1);
		}
	}
}

			
		
	


