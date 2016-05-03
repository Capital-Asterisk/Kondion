package vendalenger.kondion.kobj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import vendalenger.kondion.objectbase.KObj_Node;

public class GKO_Client extends KObj_Node {
	
	public String[] statusStrings = 
		{"Idle", "Connecting", "Connection failed", "Connected", "Disconnected"};
	private List<KObj_Node> syncObjects;
	private List<String> sendThis;
	private Thread t = null;
	private int status = 0;
	
	private DataInputStream input;
	private DataOutputStream output;
	
	private Socket socket;
	private ServerSocket serverSocket;
	
	
	/* Send to server:
	 * 'hash' of important objects
	 * silly string (i shot you)
	 * 
	 * From server:
	 * 'hash' of important objects
	 * physics object's transform and velocity
	 * JS functions
	 * (Un)important objects to add
	 * 
	 * DATA FORMAT:
	 * [byte type], [data with lengths ...]
	 * 
	 */
	
	public void connect(String host, int port) {
		if (t == null) {
			boolean running = true;
			sendThis = new ArrayList<String>();
			syncObjects = new ArrayList<KObj_Node>();
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("Connecting. please wait...");
					try {
						socket = new Socket(host, port);
						output = new DataOutputStream(socket.getOutputStream());
						input = new DataInputStream(socket.getInputStream());
						while (running) {
							try {
								//if (input.available() != 0) {
									
								//}
								int bite = input.readByte() & 0xFF;
								if (bite == 17) {
									// pong
									System.out.println("Client: Reply to ping");
									output.writeByte(18);
								} else {
									
								}
								System.out.println("Client: " + bite);
							} catch (IOException e) {
								e.printStackTrace();
								
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			t.start();
		} else {
			System.out.print("Already connected");
		}
	}
	
	public void send(String bird) {
		if (t != null)
			try {
				//output.writeBytes(bird);
				output.writeUTF(bird);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void disconnect() {
		
	}

	@Override
	public void update() {
		
	}
	
	public void status() {
		
	}

	@Override
	public void delete() {
		super.delete();
		
		
	}
	
	
}
