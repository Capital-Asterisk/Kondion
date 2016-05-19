package vendalenger.kondion.kobj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_Server extends KObj_Node {
	
	private List<Boolean> callAdd;
	private List<Integer> callIndex;
	private List<Integer> callTrack;
	private List<ScriptObjectMirror> callFunctions;
	
	private List<SyncObject> syncObjects;
	private List<Byte> syncMode;
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
	 * 1 - sync object transform
	 * 2 - sync with velocity
	 * #,index,m10,m11,m12,... (vx,vy,vz)
	 * 
	 * 16 - text message
	 * #,utf
	 * 
	 * 17 - ping
	 * 18 - pong
	 * 
	 * 64 - call function
	 * #,#totalsize,index,[d,double],[s,utf],[t],[f]
	 * 65 - create track object
	 */
	
	public void start(int port, int maxUsers) {
		if (t == null) {
			
			callAdd = new ArrayList<Boolean>();
			callIndex = new ArrayList<Integer>();
			callTrack = new ArrayList<Integer>();
			callFunctions = new ArrayList<ScriptObjectMirror>();
			
			users = new ArrayList<ConnectedUser>();
			syncObjects = new ArrayList<SyncObject>();
			
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
							SyncObject so;
							for (int i = 0; i < syncObjects.size(); i++) {
								so = syncObjects.get(i);
								if (so != null && so.node.changed) {
									so.node.changed = false;
									if (so.node instanceof KObj_Oriented) {
										KObj_Oriented arg = (KObj_Oriented) so.node;
										for (int j = 0; j < users.size(); j++) {
											output = users.get(i).output;
											output.writeByte(so.velocity ? 2 : 1);
											output.writeInt(i);
											output.writeFloat(arg.transform.m00);
											output.writeFloat(arg.transform.m01);
											output.writeFloat(arg.transform.m02);
											output.writeFloat(arg.transform.m03);
											
											output.writeFloat(arg.transform.m10);
											output.writeFloat(arg.transform.m11);
											output.writeFloat(arg.transform.m12);
											output.writeFloat(arg.transform.m13);
											
											output.writeFloat(arg.transform.m20);
											output.writeFloat(arg.transform.m21);
											output.writeFloat(arg.transform.m22);
											output.writeFloat(arg.transform.m23);
											
											output.writeFloat(arg.transform.m30);
											output.writeFloat(arg.transform.m31);
											output.writeFloat(arg.transform.m32);
											output.writeFloat(arg.transform.m33);
											
											if (so.velocity) {
												if (arg instanceof KObj_Solid) {
													output.writeFloat(((KObj_Solid) arg).velocity.x);
													output.writeFloat(((KObj_Solid) arg).velocity.y);
													output.writeFloat(((KObj_Solid) arg).velocity.z);
												} else {
													so.velocity = false;
												}
											}
											// #,index,m10,m11,m12,... vx,vy,vz
											//System.out.println("spam");
											//if (inpu)
										}
									}
									
								}
							}
							
							for (int i = 0; i < users.size(); i++) {
								
								input = users.get(i).input;
								output = users.get(i).output;
								
								for (int j = 0; j < callAdd.size(); j++) {
									System.out.println("Server: send func");
									if (callAdd.get(j)) {
										output.writeByte(65);
										//output.writeInt(j);
										System.out.println("boom: " + callIndex.get(j)
											+ " " + callTrack.get(j));
										output.writeInt(callIndex.get(j));
										output.writeInt(callTrack.get(j));
									} else {
										System.out.println("boom: " + callIndex.get(j) + " " + callTrack.get(j));
										output.writeByte(64);
										output.writeInt(callIndex.get(j));
									}
									// #,#totalsize,index,[d,double],[s,utf],[t],[f]
								}
								
								int av = input.available();
								if (av != 0) {
									byte[] msg = new byte[input.available()];
									input.read(msg);
									System.out.println("Server: data " + Arrays.toString(msg));
								}
								//if (inpu)
							}
							
							callAdd.clear();
							callTrack.clear();
							callFunctions.clear();
							callIndex.clear();
							
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
	
	public void callFunction(int index, ScriptObjectMirror callback) {
		callIndex.add(index);
		callTrack.add(0);
		callAdd.add(false);
		callFunctions.add(callback);
	}
	
	public void send(String bird) {
		
	}
	
	public void track(KObj_Node node, int function) {
		track(node, function, false, true, null);
	}
	
	public void track(KObj_Node node, int function, boolean syncVars, boolean syncVelocity, Object access) {
		if (t != null) {
			boolean dupe = false;
			for (int i = 0; i < syncObjects.size(); i++) {
				if (syncObjects.get(i).node == node) {
					dupe = true;
					i = syncObjects.size();
				}
			}
			if (!dupe) {
				SyncObject so = new SyncObject();
				so.access = null;
				if (access instanceof ScriptObjectMirror && ((ScriptObjectMirror) access).isArray()) {
					//System.out.println("woot" + Arrays.toString((boolean[]) ((ScriptObjectMirror) access).to(boolean[].class)));
					so.access = ((ScriptObjectMirror) access).to(boolean[].class);
				}
				//so.access = access;
				so.fresh = true;
				so.vars = syncVars;
				so.velocity = syncVelocity;
				so.node = node;
				syncObjects.add(so);
				
				callIndex.add(function);
				callTrack.add(syncObjects.size() - 1);
				callAdd.add(true);
				callFunctions.add(null);
				
			} else {
				System.out.println("Server: Duplicate sync object");
			}
		} else {
			System.out.println("Server: Not started");
		}
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

class SyncObject {

	public boolean[] access;
	public boolean fresh;
	public boolean vars;
	public boolean velocity;
	public KObj_Node node;
	
}

class ConnectedUser {
	
	public DataInputStream input;
	public DataOutputStream output;
	
	public ConnectedUser(DataInputStream input, DataOutputStream output) {
		this.input = input;
		this.output = output;
	}
	
}