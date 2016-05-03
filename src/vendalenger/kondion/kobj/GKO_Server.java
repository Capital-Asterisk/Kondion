package vendalenger.kondion.kobj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vendalenger.kondion.objectbase.KObj_Node;

public class GKO_Server extends KObj_Node {
	
	private List<KObj_Node> syncObjects;
	private List<ConnectedUser> users;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerSocket serverSocket;
	private Socket socket;
	private Thread t = null, u = null;
	private int status = 0;
	
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
	 * 0 - sync object variable array
	 * #,#totalsize,index,[d,double],[s,utf],[t],[f]
	 * 1 - sync object transform (with velocity depending on object type)
	 * #,index,m10,m11,m12,... vx,vy,vz
	 * 2 - add object
	 * 16 - text message
	 * #,utf
	 * 
	 * 17 - ping
	 * 18 - pong
	 * 
	 * 64 - call function
	 * #,#totalsize,index,[d,double],[s,utf],[t],[f]
	 */
	
	public void start(int port, int maxUsers) {
		if (t == null) {
			
			users = new ArrayList<ConnectedUser>();
			syncObjects = new ArrayList<KObj_Node>();
			
			boolean running = true;
			
			// Thread that listens to incoming users
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					
					try {
						serverSocket = new ServerSocket(port);
						
						System.out.println("Server created successfuly. :)");
						
						while (running) {
							try {
								socket = serverSocket.accept();
								System.out.println("Server: Incoming...");
								output = new DataOutputStream(socket.getOutputStream());
								input = new DataInputStream(socket.getInputStream());
								users.add(new ConnectedUser(input, output));
								output.writeByte(17); // ping
								System.out.println("Server: " + socket.getInetAddress() + " Connected! Total Players: " + users.size());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//Thread thread = new Thread(users[i]);
							//thread.start();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			t.start();
			
			u = new Thread(new Runnable() {
				@Override
				public void run() {
					
					while (running){
						try {
							// there is probably a better way of doing this 
							DataInputStream input;
							DataOutputStream output;
							KObj_Node node;
							for (int i = 0; i < syncObjects.size(); i++) {
								node = syncObjects.get(i);
								if (node != null && node.changed) {
									node.changed = false;
									for (int j = 0; j < users.size(); j++) {
										output = users.get(i).output;
										output.writeByte(115);
										//if (inpu)
									}
								}
							}
							
							for (int i = 0; i < users.size(); i++) {
								input = users.get(i).input;
								output = users.get(i).output;
								
								int av = input.available();
								if (av != 0) {
									byte[] msg = new byte[input.available()];
									input.read(msg);
									System.out.println("Server: data " + Arrays.toString(msg));
								}
								//if (inpu)
							}
							
							Thread.sleep(20);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//Thread thread = new Thread(users[i]);
						//thread.start();
					}
					
				}
			});
			
			u.start();
		} else {
			System.out.print("Already started");
		}
	}
	
	public void send(String bird) {
		
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


class ConnectedUser {
	
	public DataInputStream input;
	public DataOutputStream output;
	
	public ConnectedUser(DataInputStream input, DataOutputStream output) {
		this.input = input;
		this.output = output;
	}
	
}