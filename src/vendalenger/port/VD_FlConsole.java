/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.port;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import vendalenger.kondion.Kondion;

public class VD_FlConsole { /* The Fluffy console! Version 6! +20% Ram usage! */

	public static Color background;
	public static Color foreground;
	public static Font terminus;
	public static Font terminusDerived;

	public static class consoleWindow {
		public static JFrame consoleWindow;
		public static JButton enterButton;
		public static List<String> history;
		public static JTextField input;
		public static JPanel inputBar;
		public static JButton inputButton;

		public static JButton outputButton;
	}

	private static boolean exit = false;
	private static boolean frameActive = false;
	private static int inputTo = 0;
	private static int outputTo = 0;
	private static List<FluffyListener> listeners;
	private static Dimension size;
	private static final int fcVersion = 6;

	public static void addStream(FluffyListener listener) {
		listeners.add(listener);
		new Thread(listener).start();
	}

	/**
	 * Clear the current console window
	 */
	public static void clear() {
		// TODO
		listeners.get(outputTo).clear();
		// consoleWindow.jta.setText("");
	}

	/**
	 * Get the Fluffy Console version
	 *
	 * @return The version of Fluffy Console.
	 */
	public static int getFCVersion() {
		return fcVersion;
	}

	/**
	 * Hide the console window. If window is not initialized, nothing happens
	 */
	public static void hideConsole() {
		if (frameActive) {
			consoleWindow.consoleWindow.setVisible(false);
		}
	}

	/**
	 * Initialize the console window. Call this method before anything else.
	 *
	 * @param width
	 *            Width of the window in pixels.
	 * @param height
	 *            Height of the window in pixels.
	 */
	public static void initConsole(int width, int height, boolean system) {

		System.out.println("To see console output, please use -fc false");

		listeners = new ArrayList<FluffyListener>();
		size = new Dimension(width, height);

		// Create window

		// Load the terminus font

		try {
			terminus = Font.createFont(Font.TRUETYPE_FONT,
					VD_FlConsole.class.getResourceAsStream("terminusbold.ttf"));
			terminusDerived = terminus.deriveFont(12.0f);
		} catch (FontFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Image back = Toolkit.getDefaultToolkit().getImage(
				Kondion.class.getResource("kondion_fc.png"));

		// Initialization

		consoleWindow.enterButton = new JButton();
		consoleWindow.inputButton = new JButton();
		consoleWindow.outputButton = new JButton();
		consoleWindow.inputBar = new JPanel();
		consoleWindow.consoleWindow = new JFrame() {

			private static final long serialVersionUID = -1019922149890228914L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(back, getWidth() / 2 - 128, getHeight() / 2 - 128,
						256, 256, null);
			}

		};

		consoleWindow.input = new JTextField() {

			private static final long serialVersionUID = -6483898230273400318L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				String match = "";
				if (this.getText().startsWith("^")) {
					for (int i = 0; i < Command.commandList.size(); i++) {
						if (Command.commandList.get(i).key.startsWith(this
								.getText().substring(1))
								&& !Command.commandList.get(i).hidden) {
							if (match.length() > Command.commandList.get(i).key
									.length() || match.equals("")) {
								match = Command.commandList.get(i).key;
							}
						}
					}
				}
				g.setColor(getForeground().darker().darker());
				g.drawString("^" + match, -getScrollOffset(), 16);

				g.setColor(getForeground());
				g.drawString(this.getText(), -getScrollOffset(), 16);
			}
		};

		// Window

		consoleWindow.consoleWindow.getContentPane().setLayout(
				new BorderLayout());
		consoleWindow.consoleWindow.setResizable(true);
		consoleWindow.consoleWindow.setPreferredSize(size);
		consoleWindow.consoleWindow.setSize(size);
		consoleWindow.consoleWindow
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		consoleWindow.consoleWindow.setVisible(false);
		consoleWindow.consoleWindow.setTitle("Fluffy Console " + fcVersion);
		consoleWindow.consoleWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				hideConsole();
			}
		});

		// Paint the icon

		BufferedImage logo = new BufferedImage(48, 48,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = logo.getGraphics();
		g.setColor(new Color(0.1f, 0.1f, 0.1f));
		g.fillRect(0, 0, 48, 48);
		g.setColor(new Color(103, 255, 0));
		g.fillRect(28, 10, 10, 10);
		g.fillRect(8, 20, 10, 10);
		g.fillRect(18, 30, 10, 10);
		g.setColor(Color.white);
		g.setFont(terminus.deriveFont(Font.BOLD, 12));
		g.drawString("FC" + fcVersion, 8, 19);
		g.drawString("VDLG", 19, 29);
		g.dispose();
		consoleWindow.consoleWindow.setIconImage(logo);

		// Bottom bar

		consoleWindow.inputBar.setLayout(new BoxLayout(consoleWindow.inputBar,
				BoxLayout.LINE_AXIS));

		consoleWindow.enterButton.setText("^");
		consoleWindow.enterButton.setFont(terminusDerived);
		consoleWindow.enterButton.setBorder(BorderFactory.createEmptyBorder(2,
				8, 2, 8));
		consoleWindow.enterButton.addActionListener(e -> consoleWindow.input
				.getKeyListeners()[0].keyPressed(new KeyEvent(
				consoleWindow.input, 0, 0, 0, KeyEvent.VK_ENTER, '\n')));
		consoleWindow.inputButton.setText("I");
		consoleWindow.inputButton.setFont(terminusDerived);
		consoleWindow.inputButton.setBorder(BorderFactory.createEmptyBorder(2,
				8, 2, 8));

		consoleWindow.outputButton.setText("O");
		consoleWindow.outputButton.setFont(terminusDerived);
		consoleWindow.outputButton.setBorder(BorderFactory.createEmptyBorder(2,
				8, 2, 8));
		consoleWindow.outputButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				JPopupMenu j = new JPopupMenu();
				JMenuItem f;

				for (FluffyListener fluffyListener : listeners) {

					consoleWindow.consoleWindow.remove(fluffyListener.visual);

					f = new JMenuItem(fluffyListener.name);
					f.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.err.println(fluffyListener.name);
							outputTo = listeners.indexOf(fluffyListener);
							// consoleWindow.consoleWindow.remove(((BorderLayout)
							// consoleWindow.consoleWindow.getLayout()).getLayoutComponent(BorderLayout.CENTER));
							// consoleWindow.consoleWindow.remove(BorderLayout.CENTER);
							consoleWindow.consoleWindow.add(
									fluffyListener.visual, BorderLayout.CENTER);
							fluffyListener.println("Switched");
							consoleWindow.consoleWindow.revalidate();
							fluffyListener.visual.repaint();
						}
					});
					j.add(f);
				}
				// j.add(new JMenuItem("EGGS"));
				j.show(consoleWindow.outputButton, e.getX(), e.getY());
			}
		});

		consoleWindow.input.setFont(terminusDerived);
		consoleWindow.input.setBorder(BorderFactory.createEmptyBorder());
		consoleWindow.input.setText("^");
		consoleWindow.input
				.setPreferredSize(new Dimension(Short.MAX_VALUE, 24));
		setColor(Color.WHITE, new Color(14, 14, 14));
		consoleWindow.input.addKeyListener(new KeyListener() {
			int index = -1;

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println("> " + consoleWindow.input.getText());
					if (consoleWindow.history.contains(consoleWindow.input
							.getText())) {
						consoleWindow.history.remove(consoleWindow.input
								.getText());
					}
					index = -1;
					consoleWindow.history.add(0, consoleWindow.input.getText());
					Command.issue(consoleWindow.input.getText(), true);
					consoleWindow.input.setText("^");
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (consoleWindow.history.size() > 0) {
						if (consoleWindow.history.size() > index + 1) {
							index++;
						}
						consoleWindow.input.setText(consoleWindow.history
								.get(index));
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (consoleWindow.history.size() > 0) {
						if (0 < index - 1) {
							index--;
							consoleWindow.input.setText(consoleWindow.history
									.get(index));
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});

		// Add everything

		consoleWindow.inputBar.add(consoleWindow.input);
		consoleWindow.inputBar.add(consoleWindow.enterButton);
		consoleWindow.inputBar.add(consoleWindow.inputButton);
		consoleWindow.inputBar.add(consoleWindow.outputButton);

		consoleWindow.consoleWindow.add(consoleWindow.inputBar,
				BorderLayout.SOUTH);
		// consoleWindow.consoleWindow.add(new
		// JLabel("Hit the Output button to see outputs"),
		// BorderLayout.CENTER);

		// Others

		consoleWindow.history = new ArrayList<String>();
		frameActive = true;

		// End of window

		PipedOutputStream errPos = new PipedOutputStream();
		PipedOutputStream outPos = new PipedOutputStream();

		if (system) {
			try {
				System.setErr(new PrintStream(errPos, true));
				System.setOut(new PrintStream(outPos, true));
				PipedInputStream errPis = new PipedInputStream(errPos);
				PipedInputStream outPis = new PipedInputStream(outPos);

				FluffyListener sysErr = new FluffyListener(errPis, "System.err");
				FluffyListener sysOut = new FluffyListener(outPis, "System.out");

				sysErr.secondary.setBackground(Color.BLACK);
				sysErr.secondary.setForeground(Color.ORANGE);
				sysOut.secondary.setBackground(Color.BLACK);
				sysOut.secondary.setForeground(Color.CYAN);

				addStream(sysOut);
				addStream(sysErr);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Failed to initialize Fluffy Console");
			}
		} else {
			startScanner();
		}

	}

	/**
	 * Prints the object to the current selected stream
	 * 
	 * @param x
	 *            The object to print
	 */
	public static void print(Object x) {
		if (!listeners.isEmpty())
			listeners.get(outputTo).print(x);
		else
			System.out.println(x);
	}

	/**
	 * Prints the object to the current selected stream. Creates a new line
	 * 
	 * @param x
	 *            The object to print
	 */
	public static void println(Object x) {
		if (!listeners.isEmpty())
			listeners.get(outputTo).println(x);
		else
			System.out.println(x);
	}

	/**
	 * Change the Foreground and background.
	 *
	 * @param fore
	 *            Text color, can be null.
	 * @param back
	 *            Text highlighting, can be null.
	 */
	public static void setColor(Color fore, Color back) {
		if (fore != null) {
			foreground = fore;
			consoleWindow.enterButton.setForeground(fore);
			consoleWindow.inputButton.setForeground(fore);
			consoleWindow.outputButton.setForeground(fore);
			consoleWindow.inputBar.setForeground(fore);
			consoleWindow.input.setForeground(fore);
			// consoleWindow.jta.setForeground(fore);
		}
		if (back != null) {
			background = back;
			consoleWindow.consoleWindow.getContentPane().setBackground(back);
			consoleWindow.enterButton.setBackground(back);
			consoleWindow.inputButton.setBackground(back);
			consoleWindow.outputButton.setBackground(back);
			consoleWindow.inputBar.setBackground(back);
			consoleWindow.input.setBackground(back);
			// consoleWindow.jta.setBackground(back);
		}
	}

	/**
	 * Show the console window. If the window is not initialized, window will be
	 * initialized.
	 */
	public static void showConsole() {
		consoleWindow.consoleWindow.setVisible(true);
		consoleWindow.consoleWindow.getContentPane().repaint();
	}

	public static void startScanner() {
		Thread t = new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (!exit) {
				Command.issue(scanner.nextLine(), true);
			}
			scanner.close();
		});
		t.start();
	}

	/**
	 * Cause an error by trying to connect to port 235325 which does not exist.
	 */
	public static void testError() {
		try {
			new Socket("", 235325);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Toggle the visibility of the console window. Make sure that the console
	 * window is already initialized.
	 */
	public static void toggleConsole() {
		if (consoleWindow.consoleWindow.isVisible()) {
			consoleWindow.consoleWindow.setVisible(false);
		} else {
			consoleWindow.consoleWindow.setVisible(true);
		}
	}

	/**
	 * Make a beep sound from your default audio device. This function does NOT
	 * use your motherboard piezo (the one that beeps when your computer is
	 * powered on).
	 * 
	 * @param ms
	 *            Duration of the beep in Milliseconds
	 * @param hz
	 *            Frequency of the beep in Hertz
	 * @param rate
	 *            The sample rate. May produce a sine wave when it is equal to
	 *            the frequency.
	 */
	public static void quickBeep(int ms, int hz, int rate) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioFormat audFormat = new AudioFormat(rate, 8, 1, true, false);

			float value = rate / hz;

			byte[] data = new byte[ms * rate];
			for (int i = 0; i < data.length; i++) {
				// System.out.println(((i % 9) == 1) ? (byte)-127 : (byte)127);
				data[i] = ((i % value) < (value / 2)) ? (byte) -128
						: (byte) 127;
			}

			clip.open(audFormat, data, 0, data.length);

			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.START) {
						Timer t = new Timer(ms + 1, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								clip.close();
							}
						});
						t.setRepeats(false);
						t.start();
					}
				}
			});

			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}

class FluffyListener implements Runnable {

	public Component visual;
	public Component secondary;
	public List<StringEvent> listeners;
	public String name;
	public String output;
	public BufferedReader reader;
	public InputStream stream;

	/**
	 * Create an Input stream listener with a custom visual.
	 * 
	 * @param stream
	 *            The input stream to listen to
	 * @param name
	 *            The display name
	 * @param visual
	 *            A component used when this listener is selected
	 */
	public FluffyListener(InputStream stream, String n, Component visual) {
		reader = new BufferedReader(new InputStreamReader(stream));
		listeners = new ArrayList<StringEvent>();
		name = n;
		this.visual = visual;
	}

	/**
	 * Create an Input stream listener with a default text visual. A string
	 * event is added automatically.
	 * 
	 * @param stream
	 *            The input stream to listen to
	 * @param name
	 *            The display name
	 */
	public FluffyListener(InputStream stream, String n) {
		reader = new BufferedReader(new InputStreamReader(stream));
		listeners = new ArrayList<StringEvent>();
		name = n;
		secondary = new JTextArea();
		((JTextArea) secondary).setEditable(false);
		secondary.setFont(VD_FlConsole.terminusDerived);
		visual = new JScrollPane(secondary);
		((JScrollPane) visual).setBorder(BorderFactory.createEmptyBorder());

		addListener(new StringEvent(n + "_textconsole") {
			@Override
			public void onLineAdded(String line, String total) {
				((JTextArea) secondary).setText(total);
				/*
				 * System.err.println("B4: " + ((JTextArea)
				 * secondary).getCaretPosition()); if (((JTextArea)
				 * secondary).getCaretPosition() == ((JTextArea)
				 * secondary).getRows()) ((JTextArea)
				 * secondary).setCaretPosition(((JTextArea)
				 * secondary).getDocument().getLength());
				 * System.err.println(((JTextArea)
				 * secondary).getCaretPosition());
				 */
			}
		});
	}

	/**
	 * Listen to new lines in the input stream
	 * 
	 * @param e
	 *            Your string event
	 */
	public void addListener(StringEvent e) {
		listeners.add(e);
	}

	/**
	 * Output is set to nothing. String events are triggered.
	 */
	public void clear() {
		output = "";
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onLineAdded("", output);
		}
	}

	/**
	 * Print to output without interfering with the input stream
	 * 
	 * @param x
	 *            The string, int, float, ect.. to print
	 */
	public void print(Object x) {
		output += x;
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onLineAdded(x.toString(), output);
		}
	}

	/**
	 * Print a line to output without interfering with the input stream
	 * 
	 * @param x
	 *            The string, int, float, ect.. to print
	 */
	public void println(Object x) {
		output += x + "\n";
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onLineAdded(x.toString(), output);
		}
	}

	/**
	 * Don't use this
	 */
	@Override
	public void run() {
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				output += line + "\n";
				for (StringEvent stringEvent : listeners) {
					stringEvent.onLineAdded(line, output);
				}
			}
		} catch (IOException e) {
			// Restart when pipe break error... is this bad?
			run();
		}
	}

}

abstract class StringEvent {

	public String name;

	public StringEvent(String id) {
		name = id;
	}

	public abstract void onLineAdded(String line, String total);

}
