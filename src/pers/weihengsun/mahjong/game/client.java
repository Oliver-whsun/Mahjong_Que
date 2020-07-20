package pers.weihengsun.mahjong.game;

import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

// client class
class client {
	public static String tempName = "";
	// listen command from server
	public class readHandler implements Runnable {
		// server socket
		private Socket serverSock;
		// socket reader
		private BufferedReader reader;
		// constructer
		public readHandler(Socket serverSock) {
			try {
				this.serverSock = serverSock;
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamReader);
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		// run method
		public void run() {
			String temp = "";
			try{
				while((temp = reader.readLine()) != null){
					// System.out.println(temp);
					if(temp.length() == 1) {
						if(isAi) {
							aiPlayer = new AIPlayer("", Integer.parseInt(temp));
						}else {
							player = new Player("", Integer.parseInt(temp));
						}
					}else {
						CharSequence s = "_";
						if(temp.equals("name")) {
							if(isAi) {
								String name = null;
								while(name == null) {
									name = JOptionPane.showInputDialog("Please enter AI name");
								}
								aiPlayer.name = name;
								writer.println(aiPlayer.playerNum + "_" + name);
								writer.flush();
							}else {
								String name = null;
								while(name == null) {
									name = JOptionPane.showInputDialog("Please enter your name");
								}
								player.name = name;
								writer.println(player.playerNum + "_" + name);
								writer.flush();
							}

						}else if(temp.contains(s)) {
							if(isAi) {
								aiPlayer.setNames(temp);
							}else {
								player.setNames(temp);
							}
						}else {
							if(isAi) {
								writer.println(aiPlayer.react(temp));
								writer.flush();
							}else {
								String respond = player.react(temp);
								// System.out.println(respond);
								writer.println(respond);
								writer.flush();	
								// System.out.println(respond);
							}
						}
					}									
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

	}
	// client socket
	private Socket sock;
	// server IP and port number
	private String serverIP = "localhost";
	private int serverPort = 4545;
	// socket writer
	private PrintWriter writer;
	// player
	Player player;
	// ai player
	AIPlayer aiPlayer;
	boolean isAi = false;
	// constracter
	public client() {
		try {
			sock = new Socket(serverIP, serverPort);
			writer = new PrintWriter(sock.getOutputStream());
			Thread listener = new Thread(new readHandler(sock));
			listener.start();
			// writer.println("hi, this is a test");
			// writer.flush();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// Ai constructor
	public client(String ai) {
		try {
			isAi = true;
			sock = new Socket(serverIP, serverPort);
			writer = new PrintWriter(sock.getOutputStream());
			Thread listener = new Thread(new readHandler(sock));
			listener.start();
			// writer.println("hi, this is a test");
			// writer.flush();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String keyboard = args[0];
		if(keyboard.equals("ai")){
			client newClient = new client(keyboard);
		}else {
			client newClient = new client();
		}
	}
}