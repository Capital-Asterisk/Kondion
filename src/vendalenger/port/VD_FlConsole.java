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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

class FluffyListener implements Runnable {

	public List<StringEvent> listeners;
	public String name;
	public String output;
	public BufferedReader reader;
	public InputStream stream;

	/**
	 * Create an Input stream listener
	 * 
	 * @param stream
	 *            The input stream to listen to
	 * @param name
	 *            The display name
	 */
	public FluffyListener(InputStream stream, String name) {
		reader = new BufferedReader(new InputStreamReader(stream));
		listeners = new ArrayList<StringEvent>();
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
		System.out.print(x);
	}

	/**
	 * Print a line to output without interfering with the input stream
	 * 
	 * @param x
	 *            The string, int, float, ect.. to print
	 */
	public void println(Object x) {
		System.out.println(x);
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
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).onLineAdded(line, output);
				}
			}
		} catch (IOException e) { // Restart when pipe break error.
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

public class VD_FlConsole { /* The Fluffy console! Version 4! */

	public static class consoleWindow {
		public static JFrame consoleWindow;
		public static JButton enterButton;
		public static List<String> history;
		public static JTextField input;
		public static JPanel inputBar;
		public static JButton inputButton;
		public static JScrollPane jsp;
		public static JTextArea jta;
		public static JButton outputButton;
	}

	public static boolean exit = false;
	protected static int fcVersion = 4;
	public static boolean frameActive = false;
	public static List<FluffyListener> listeners;
	public static Dimension size;

	public static Font terminus;

	public static void addStream(FluffyListener listener) {
		listeners.add(listener);
	}

	/**
	 * Clear the console window. Make sure that the console window is already
	 * initialized.
	 */
	public static void clear() {
		consoleWindow.jta.setText("");
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

		listeners = new ArrayList<FluffyListener>();
		size = new Dimension(width, height);

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
				StringEvent e = new StringEvent("console_window") {
					@Override
					public void onLineAdded(String line, String total) {
						consoleWindow.jta.setText(total);
						consoleWindow.jta.setCaretPosition(consoleWindow.jta
								.getDocument().getLength());

					}
				};
				sysErr.addListener(e);
				sysOut.addListener(e);
				new Thread(sysErr).start();
				new Thread(sysOut).start();
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
	 * Change the Foreground and background.
	 *
	 * @param fore
	 *            Text color, can be null.
	 * @param back
	 *            Text highlighting, can be null.
	 */
	public static void setColor(Color fore, Color back) {
		if (fore != null) {
			consoleWindow.enterButton.setForeground(fore);
			consoleWindow.inputButton.setForeground(fore);
			consoleWindow.outputButton.setForeground(fore);
			consoleWindow.inputBar.setForeground(fore);
			consoleWindow.input.setForeground(fore);
			consoleWindow.jta.setForeground(fore);
		}
		if (back != null) {
			consoleWindow.enterButton.setBackground(back);
			consoleWindow.inputButton.setBackground(back);
			consoleWindow.outputButton.setBackground(back);
			consoleWindow.inputBar.setBackground(back);
			consoleWindow.input.setBackground(back);
			consoleWindow.jta.setBackground(back);
		}
	}

	/**
	 * Show the console window. If the window is not initialized, window will be
	 * initialized.
	 */
	public static void showConsole() {
		if (!frameActive) {
			try { /* Load a font */
				terminus = Font.createFont(Font.TRUETYPE_FONT,
						VD_FlConsole.class
								.getResourceAsStream("terminusbold.ttf"));
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			consoleWindow.enterButton = new JButton();
			consoleWindow.inputButton = new JButton();
			consoleWindow.outputButton = new JButton();
			consoleWindow.inputBar = new JPanel();
			consoleWindow.consoleWindow = new JFrame();
			consoleWindow.input = new JTextField() {
				private static final long serialVersionUID = 1L;

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
			consoleWindow.jta = new JTextArea() {
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {/* Disable anti aliasing */
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
					g2.setRenderingHint(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
					super.paintComponent(g2);
				}
			};

			/* Window and icon */

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

			/* Paint the icon */

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
			g.setFont(consoleWindow.jta.getFont().deriveFont(Font.BOLD, 12));
			g.drawString("FC" + fcVersion, 8, 19);
			g.drawString("VDLG", 19, 29);
			g.dispose();
			consoleWindow.consoleWindow.setIconImage(logo);

			consoleWindow.jta.setEditable(false);
			consoleWindow.jta.setFont(terminus.deriveFont(12.0f));
			consoleWindow.jsp = new JScrollPane(consoleWindow.jta);
			// jsp.getHorizontalScrollBar().setVa
			consoleWindow.jsp.setBorder(BorderFactory.createEmptyBorder());

			/* Bottom bar */

			consoleWindow.inputBar.setLayout(new BoxLayout(
					consoleWindow.inputBar, BoxLayout.LINE_AXIS));

			consoleWindow.enterButton.setText("^");
			consoleWindow.enterButton.setFont(consoleWindow.jta.getFont());
			consoleWindow.enterButton.setBorder(BorderFactory
					.createEmptyBorder(2, 8, 2, 8));
			consoleWindow.enterButton
					.addActionListener(e -> consoleWindow.input
							.getKeyListeners()[0].keyPressed(new KeyEvent(
							consoleWindow.input, 0, 0, 0, KeyEvent.VK_ENTER,
							'\n')));
			consoleWindow.inputButton.setText("I");
			consoleWindow.inputButton.setFont(consoleWindow.jta.getFont());
			consoleWindow.inputButton.setBorder(BorderFactory
					.createEmptyBorder(2, 8, 2, 8));
			consoleWindow.outputButton.setText("O");
			consoleWindow.outputButton.setFont(consoleWindow.jta.getFont());
			consoleWindow.outputButton.setBorder(BorderFactory
					.createEmptyBorder(2, 8, 2, 8));

			consoleWindow.input.setFont(consoleWindow.jta.getFont());
			consoleWindow.input.setBorder(BorderFactory.createEmptyBorder());
			consoleWindow.input.setText("^");
			consoleWindow.input.setPreferredSize(new Dimension(Short.MAX_VALUE,
					24));
			setColor(Color.GREEN, Color.BLACK);
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
						consoleWindow.history.add(0,
								consoleWindow.input.getText());
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
								consoleWindow.input
										.setText(consoleWindow.history
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

			/* Add everything */

			consoleWindow.inputBar.add(consoleWindow.input);
			consoleWindow.inputBar.add(consoleWindow.enterButton);
			consoleWindow.inputBar.add(consoleWindow.inputButton);
			consoleWindow.inputBar.add(consoleWindow.outputButton);

			consoleWindow.consoleWindow.add(consoleWindow.inputBar,
					BorderLayout.SOUTH);
			consoleWindow.consoleWindow.add(consoleWindow.jsp,
					BorderLayout.CENTER);

			/* Stuff thats not swingy */

			consoleWindow.history = new ArrayList<String>();
			frameActive = true;
		}
		consoleWindow.consoleWindow.setVisible(true);
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
}
