import java.net.*;
import java.io.*;
import java.util.*;

// server class
class server {
	// class used to listen commands from clients
	// each client has a thread
	public class readHandler implements Runnable {
		// reader which used to read commands from clients
		private BufferedReader reader;
		// scoket of client
		private Socket sock;

		// constructor
		public readHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		// implememtns run method
		public void run() {
			try {
				String temp = "";
				while((temp = reader.readLine()) != null) {
					System.out.println("client: " + temp);
					CharSequence s = "_";
					if(temp.contains(s)) {
						System.out.println(temp);
						nameCounter++;
						names[Integer.parseInt(temp.split("_")[0])] = temp.split("_")[1];
						if(nameCounter == 4) {
							String nameCmd = "";
							for(int i = 0; i < 4; i++) {
								nameCmd += names[i] + "_";
							}
							nameCmd = nameCmd.substring(0, nameCmd.length() - 1);
							System.out.println(nameCmd);
							table.boradcast(nameCmd);
							table.shuffle();
							table.distribute();
						}
					}else {
						synchronized(table) {
							table.cmdHandler(temp);
						}
					}
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// counter 
	private int nameCounter = 0;
	// names
	String[] names = new String[4];
	// server soceket
	private ServerSocket serverS;
	// port number 
	private int port = 4545;
	// mahjong table
	private MahjongTable table;
	// the limit numebr of player
	private final int limitSize = 4;
	// clients
	private client[] clients = new client[limitSize];
	// thread pool
	private Thread[] thPool = new Thread[limitSize];
	// writer array
	private PrintWriter[] writers = new PrintWriter[limitSize];
	// current client
	private int currentClient = 0;
	// if the game is on
	boolean isGame = false;
	// constructor
	public server() {
		try {
			serverS = new ServerSocket(port);
			System.out.println("Hi, Server is online");
		}catch(Exception ioe) {
			System.out.println("Sorry, server is down");
			ioe.printStackTrace();
		}	
	}

	// start server
	public void start() {
		try{
			while(true) {
				Socket sock = serverS.accept();
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				System.out.println("welcome");
				writer.println(currentClient);
				writer.flush();
				thPool[currentClient] = new Thread(new readHandler(sock));
				writers[currentClient] = writer;
				currentClient++;
				if(currentClient == 4) {
					table = new MahjongTable(writers);
					for(int i = 0; i < limitSize; i++) {
						thPool[i].start();
					}
					table.boradcast("name");
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		server newServer = new server();
		newServer.start();
	}
}