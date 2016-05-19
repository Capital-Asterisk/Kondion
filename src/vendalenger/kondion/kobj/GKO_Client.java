package vendalenger.kondion.kobj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

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
								//System.out.println("Bite: " + bite);
								if (bite == 17) {
									// pong
									System.out.println("Client: Reply to ping");
									output.writeByte(18);
									// if ((bite - 1) * (bite - 2) == 0) {
								} else if (bite == 1 || bite == 2) {
									// sync object
									int ind = input.readInt();
									KObj_Oriented node = (syncObjects.size() > ind) ? (KObj_Oriented) syncObjects.get(ind) : null;
									if (node != null) {
										
										node.transform.m00 = input.readFloat();
										node.transform.m01 = input.readFloat();
										node.transform.m02 = input.readFloat();
										node.transform.m03 = input.readFloat();
										
										node.transform.m10 = input.readFloat();
										node.transform.m11 = input.readFloat();
										node.transform.m12 = input.readFloat();
										node.transform.m13 = input.readFloat();
										
										node.transform.m20 = input.readFloat();
										node.transform.m21 = input.readFloat();
										node.transform.m22 = input.readFloat();
										node.transform.m23 = input.readFloat();
									
										node.transform.m30 = input.readFloat();
										node.transform.m31 = input.readFloat();
										node.transform.m32 = input.readFloat();
										node.transform.m33 = input.readFloat();
										//System.out.println("00: " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat());
										//System.out.println("10: " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat());
										//System.out.println("20: " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat());
										//System.out.println("30: " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat());
										
										if (bite == 2 && node instanceof KObj_Solid) {
											// velocity
											//System.out.println("velocitu!");
											
											((KObj_Solid) node).velocity.x = input.readFloat();
											((KObj_Solid) node).velocity.y = input.readFloat();
											((KObj_Solid) node).velocity.z = input.readFloat();
											//System.out.println("Velocity: " + input.readFloat() + " " + input.readFloat() + " " + input.readFloat());
										}
										
									} else {
										input.skipBytes(4 * 4 * 4);
										if (bite == 2) {
											input.skipBytes(4 * 3);
											//((KObj_Solid) node).velocity.x = input.readFloat();
											//((KObj_Solid) node).velocity.y = input.readFloat();
											//((KObj_Solid) node).velocity.z = input.readFloat();
										}
									}
									
								} else if (bite == 64) {
									int ind = input.readInt();
									//System.out.println("bite");
									if (s != null) {
										ScriptObjectMirror array = (ScriptObjectMirror) s.get("func");
										if (array != null) {
											System.out.println("Client: " + array.getSlot(ind));
											
										}
										
									}
									
								} else if (bite == 65) {
									//System.out.println("bite");
									System.out.println("Avalibele: " + input.available());
									int ind = input.readInt();
									int ind2 = input.readInt();
									if (s != null) {
										ScriptObjectMirror array = (ScriptObjectMirror) s.get("func");
										if (array != null) {
											//System.out.println("WOOT: " + ind);
											ScriptObjectMirror func = (ScriptObjectMirror) array.getSlot(ind);
											KObj_Node nd = (KObj_Node) func.call(this, 0);
											if (nd != null) {
	
												while (syncObjects.size() <= ind2) {
													syncObjects.add(null);
												}
												syncObjects.set(ind2, nd);
											}
										 	
										}
										
									}
									
								}
								//System.out.println("Client: " + bite);
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
