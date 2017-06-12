import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*; 
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.lang.Integer;
import java.nio.channels.ClosedChannelException;
 
class GPanel extends JPanel implements MouseListener {
 
    OthelloBoard board;
    int gameLevel;
    ImageIcon button_black, button_white;
    JLabel score_black, score_white;
    String gameTheme;
    Move hint = null;
    boolean inputEnabled, active;
    Selector selector;
    SelectionKey key;

    public GPanel (OthelloBoard board, JLabel score_black, JLabel score_white, String theme, int level, Selector selector) {
        super();
        this.board = board;
        this.score_black = score_black;
        this.score_white = score_white;
        gameLevel = level;
        setTheme(theme);
        addMouseListener(this);
        inputEnabled = true;
        active = true;
        this.selector = selector;
        this.key = key;
    }
 
    public void setTheme(String gameTheme){
        hint = null;
        this.gameTheme = gameTheme;
        if (gameTheme.equals("Classic")) {
            button_black = new ImageIcon("button_black.jpg");
            button_white = new ImageIcon("button_white.jpg");
            setBackground(Color.green);
        }else if(gameTheme.equals("Electric")) {
            button_black = new ImageIcon("button_blu.jpg");
            button_white = new ImageIcon("button_red.jpg");
            setBackground(Color.white);
        }else{
            gameTheme = "Flat"; // default theme "Flat"
            setBackground(Color.green); 
        }
        repaint();
    }
 
    public void setLevel(int level) {
        if ((level > 1) && (level < 7)) gameLevel = level;
    }
 
    public void drawPanel(Graphics g) {
//	int currentWidth = getWidth();
//      int currentHeight = getHeight();
        for (int i = 1 ; i < 8 ; i++) {
            g.drawLine(i * Othello.Square_L, 0, i * Othello.Square_L, Othello.Height);
        }
        g.drawLine(Othello.Width, 0, Othello.Width, Othello.Height);
        for (int i = 1 ; i < 8 ; i++) {
            g.drawLine(0, i * Othello.Square_L, Othello.Width, i * Othello.Square_L);
        }
        g.drawLine(0, Othello.Height, Othello.Width, Othello.Height);
         for (int i = 0 ; i < 8 ; i++)
            for (int j = 0 ; j < 8 ; j++)
                switch (board.get(i,j)) {
                    case white:  
                        if (gameTheme.equals("Flat"))
                        {	g.setColor(Color.white);
                                g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L-1, Othello.Square_L-1);
                        }
                        else g.drawImage(button_white.getImage(), 1 + i * Othello.Square_L, 1 + j * Othello.Square_L, this); 
                        break;
                case black:  
                        if (gameTheme.equals("Flat"))
                        {	g.setColor(Color.black);
                                g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L-1, Othello.Square_L-1);
                        }
                        else g.drawImage(button_black.getImage(), 1 + i * Othello.Square_L, 1 + j * Othello.Square_L, this); 
                        break;
                }
        if (hint != null) {
                g.setColor(Color.darkGray);
                g.drawOval(hint.i * Othello.Square_L+3, hint.j * Othello.Square_L+3, Othello.Square_L-6, Othello.Square_L-6);
        }
	}	
 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPanel(g);
	}
 
	public Dimension getPreferredSize() {
		return new Dimension(Othello.Width,Othello.Height);
	}
 
	public void showWinner() {
		inputEnabled = false;
		active = false;
		if (board.counter[0] > board.counter[1])
		JOptionPane.showMessageDialog(this, "You win!","Othello",JOptionPane.INFORMATION_MESSAGE);
		else if (board.counter[0] < board.counter[1])
		   JOptionPane.showMessageDialog(this, "I win!","Othello",JOptionPane.INFORMATION_MESSAGE);
		   else JOptionPane.showMessageDialog(this, "Drawn!","Othello",JOptionPane.INFORMATION_MESSAGE);
	}
 
	public void setHint(Move hint) {
		this.hint = hint;
	}
 
	public void clear() {
		board.clear();
		score_black.setText(Integer.toString(board.getCounter(TKind.black)));
		score_white.setText(Integer.toString(board.getCounter(TKind.white)));
		inputEnabled = true;
		active = true;
	}
 
	public void computerMove() {
		if (board.gameEnd()) {
			showWinner();
			return;
		}
		Move move = new Move();
		if (board.findMove(TKind.white,gameLevel,move)) {
			board.move(move,TKind.white);
			score_black.setText(Integer.toString(board.getCounter(TKind.black)));
			score_white.setText(Integer.toString(board.getCounter(TKind.white)));
			repaint();
			if (board.gameEnd()) showWinner();
			else if (!board.userCanMove(TKind.black)) {
				   JOptionPane.showMessageDialog(this, "You pass...","Othello",JOptionPane.INFORMATION_MESSAGE);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							computerMove();
						}
					});
				}
		}
		else if (board.userCanMove(TKind.black))
		   JOptionPane.showMessageDialog(this, "I pass...","Othello",JOptionPane.INFORMATION_MESSAGE);
		   else showWinner();
	}
 
	public void mouseClicked(MouseEvent e) {
// generato quando il mouse viene premuto e subito rilasciato (click)
 
		if (inputEnabled) {
                    try {
                        hint = null;
                        int i = e.getX() / Othello.Square_L;
                        int j = e.getY() / Othello.Square_L;
                        System.out.println(i+", "+j);
                        String datos = "Falso";
                        
                        selector.select();
                        SelectionKey key = null;
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            key = (SelectionKey) iterator.next();
                            iterator.remove();
                            SocketChannel client = (SocketChannel) key.channel();
                            if (key.isConnectable()) {
                                try {
                                    if (client.isConnectionPending()) {
                                        System.out.println("Intentando establecer la conexion");
                                        try {
                                            client.finishConnect();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    client.register(selector, SelectionKey.OP_WRITE);
                                    client.configureBlocking(false);
                                    System.out.println("Conexion establecida");
                                    continue;
                                } catch (ClosedChannelException ex) {
                                    Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if(key.isWritable()){
                                try {
                                    System.out.println("Listo para escribir");
                                    SocketChannel sc =(SocketChannel)key.channel();
                                    
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                                    oos.writeObject(board);
                                    ByteBuffer bi = ByteBuffer.wrap(baos.toByteArray());
                                    ByteBuffer bO = ByteBuffer.allocate(2000);
                                    bO.clear();
                                    bO.putInt(i);
                                    bO.putInt(j);
                                    bO.put(bi);
                                    bO.flip();
                                    sc.write(bO);
                                    oos.close();
                                    baos.close();
                                    
                                    ByteBuffer bS = ByteBuffer.allocate(20);
                                    bS.clear();
                                    sc.read(bS);
                                    bS.flip();
                                    datos = new String(bS.array());
                                    System.out.println(datos);
                                    continue;
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
//                        if(datos.indexOf("verdaderoo") == 0){
//                            score_black.setText(Integer.toString(board.getCounter(TKind.black)));
//                            score_white.setText(Integer.toString(board.getCounter(TKind.white)));
//                            repaint();
//                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                                public void run() {
//                                    Cursor savedCursor = getCursor();
//                                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//                                    computerMove();
//                                    setCursor(savedCursor);
//                                }
//                            });
//                        }
                        if ((i < 8) && (j < 8) && (board.get(i,j) == TKind.nil) && (board.move(new Move(i,j),TKind.black) != 0)) {
                            score_black.setText(Integer.toString(board.getCounter(TKind.black)));
                            score_white.setText(Integer.toString(board.getCounter(TKind.white)));
                            repaint();
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    Cursor savedCursor = getCursor();
                                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    computerMove();
                                    setCursor(savedCursor);
                                }
                            });
                        }
                        else JOptionPane.showMessageDialog(this, "Illegal move","Othello",JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
	}

	public void mouseEntered(MouseEvent e) {
// generato quando il mouse entra nella finestra
	}
 
 
	public void mouseExited(MouseEvent e) {
// generato quando il mouse esce dalla finestra
	}
 
 
	public void mousePressed(MouseEvent e) {
// generato nell'istante in cui il mouse viene premuto
	}
 
 
	public void mouseReleased(MouseEvent e) {
// generato quando il mouse viene rilasciato, anche a seguito di click
	}
 
 
};
 
public class Othello extends JFrame implements ActionListener{
 
	JEditorPane editorPane;
 
	static final String WindowTitle = "Othello";
	static final String ABOUTMSG = WindowTitle+"\n\n26-12-2006\njavalc6";
 
	static GPanel gpanel;
	static JMenuItem hint;
	static boolean helpActive = false;
 
	static final int Square_L = 33; // length in pixel of a square in the grid
	static final int  Width = 8 * Square_L; // Width of the game board
	static final int  Height = 8 * Square_L; // Width of the game board
 
	OthelloBoard board;
	static JLabel score_black, score_white;
	JMenu level, theme;
 
	public Othello() {
		super(WindowTitle);
 
            try {
                score_black=new JLabel("2"); // the game start with 2 black pieces
                score_black.setForeground(Color.blue);
                score_black.setFont(new Font("Dialog", Font.BOLD, 16));
                score_white=new JLabel("2"); // the game start with 2 white pieces
                score_white.setForeground(Color.red);
                score_white.setFont(new Font("Dialog", Font.BOLD, 16));
                
                
                Selector selector = Selector.open();
                SocketChannel connectionClient = SocketChannel.open();
                connectionClient.configureBlocking(false);
                connectionClient.connect(new InetSocketAddress("127.0.0.1", 9000));
                connectionClient.register(selector, SelectionKey.OP_CONNECT);
                
//                selector.select();
//                SelectionKey key = null;
//                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                while (iterator.hasNext()) {
//                    key = (SelectionKey) iterator.next();
//                    iterator.remove();
//                    SocketChannel client = (SocketChannel) key.channel();
//                    if (key.isConnectable()) {
//                        if (client.isConnectionPending()) {
//                            System.out.println("Intentando establecer la conexion");
//                            try {
//                                    client.finishConnect();
//                            } catch (IOException e) {
//                                    e.printStackTrace();
//                            }
//                        }
//                        client.register(selector, SelectionKey.OP_WRITE); 
//                        client.configureBlocking(false);
//                        System.out.println("Conexion establecida");
//                        continue;
//                    }
//                    if(key.isWritable()){
//                        try {
//                            int i = 5, j=7;
//                            System.out.println("writable");
//                            SocketChannel sc =(SocketChannel)key.channel();
//                            ByteBuffer b = ByteBuffer.allocate(8);
//                            b.clear();
//                            b.putInt(i);
//                            b.putInt(j);
//                            b.flip();
//                            sc.write(b);
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
                
                board = new OthelloBoard();
                gpanel = new GPanel(board, score_black, score_white,"Electric", 3, selector);
                
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setupMenuBar();
                gpanel.setMinimumSize(new Dimension(Othello.Width,Othello.Height));
                
                JPanel status = new JPanel();
                status.setLayout(new BorderLayout());
                status.add(score_black, BorderLayout.WEST);
                status.add(score_white, BorderLayout.EAST);
//		status.setMinimumSize(new Dimension(100, 30));
                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gpanel, status);
                splitPane.setOneTouchExpandable(false);
                getContentPane().add(splitPane);
                
                pack();
                setVisible(true);
                setResizable(false);
            } catch (IOException ex) {
                Logger.getLogger(Othello.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
 
 
 
// voci del menu di primo livello
// File Edit Help
//
	void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(buildGameMenu());
		menuBar.add(buildHelpMenu());
		setJMenuBar(menuBar);	
	}
 
  
 
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
		String action = source.getText();
		if (action.equals("Classic")) gpanel.setTheme(action);
		else if (action.equals("Electric")) gpanel.setTheme(action);
			else if (action.equals("Flat")) gpanel.setTheme(action);
	}
 
    protected JMenu buildGameMenu() {
		JMenu game = new JMenu("Game");
		JMenuItem newWin = new JMenuItem("New");
		level = new JMenu("Level");
		theme = new JMenu("Theme");
		JMenuItem undo = new JMenuItem("Undo");
		hint = new JMenuItem("Hint");
		undo.setEnabled(false);
		JMenuItem quit = new JMenuItem("Quit");
 
// build level sub-menu
		ActionListener newLevel = new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem)(e.getSource());
				gpanel.setLevel(Integer.parseInt(source.getText()));
		   }}; 
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("2");
		group.add(rbMenuItem);
		level.add(rbMenuItem).addActionListener(newLevel);
		rbMenuItem = new JRadioButtonMenuItem("3", true);
		group.add(rbMenuItem);
		level.add(rbMenuItem).addActionListener(newLevel);
		rbMenuItem = new JRadioButtonMenuItem("4");
		group.add(rbMenuItem);
		level.add(rbMenuItem).addActionListener(newLevel);
		rbMenuItem = new JRadioButtonMenuItem("5");
		group.add(rbMenuItem);
		level.add(rbMenuItem).addActionListener(newLevel);
		rbMenuItem = new JRadioButtonMenuItem("6");
		group.add(rbMenuItem);
		level.add(rbMenuItem).addActionListener(newLevel);
 
// build theme sub-menu
		group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Classic");
		group.add(rbMenuItem);
		theme.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		rbMenuItem = new JRadioButtonMenuItem("Electric", true);
		group.add(rbMenuItem);
		theme.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		rbMenuItem = new JRadioButtonMenuItem("Flat");
		group.add(rbMenuItem);
		theme.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
 
// Begin "New"
		newWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gpanel.clear();
				hint.setEnabled(true);
				repaint();
			}});
// End "New"
 
// Begin "Quit"
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					System.exit(0);
			}});
// End "Quit"
 
 
// Begin "Hint"
		hint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
 
				if (gpanel.active)	{
					Move move = new Move();
					if (board.findMove(TKind.black,gpanel.gameLevel,move))
						gpanel.setHint(move);
						repaint();
	/*					if (board.move(move,TKind.black) != 0) {
							score_black.setText(Integer.toString(board.getCounter(TKind.black)));
							score_white.setText(Integer.toString(board.getCounter(TKind.white)));
							repaint();
							javax.swing.SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Cursor savedCursor = getCursor();
									setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
									gpanel.computerMove();
									setCursor(savedCursor);		
								}
							});
						}	
	*/
				} else hint.setEnabled(false);
			}});
// End "Hint"
 
 
		game.add(newWin);
		game.addSeparator();
		game.add(undo);
		game.add(hint);
		game.addSeparator();
		game.add(level);
		game.add(theme);
		game.addSeparator();
		game.add(quit);
		return game;
    }
 
 
	protected JMenu buildHelpMenu() {
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About "+WindowTitle+"...");
		JMenuItem openHelp = new JMenuItem("Help Topics...");
 
// Begin "Help"
		openHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        createEditorPane();
			}});
// End "Help"
 
// Begin "About"
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(Othello.class.getResource("reversi.jpg"));
				JOptionPane.showMessageDialog(null, ABOUTMSG, "About "+WindowTitle,JOptionPane.PLAIN_MESSAGE, icon);
			}});
// End "About"
 
		help.add(openHelp);
		help.add(about);
 
		return help;
    }
 
    protected void createEditorPane() {
        if (helpActive) return;
		editorPane = new JEditorPane(); 
        editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (e instanceof HTMLFrameHyperlinkEvent) {
					((HTMLDocument)editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
						(HTMLFrameHyperlinkEvent)e);
					} else {
					try {
						editorPane.setPage(e.getURL());
					} catch (java.io.IOException ioe) {
						System.out.println("IOE: " + ioe);
					}
					}
				}
				}
			});
        java.net.URL helpURL = Othello.class.getResource("HelpFile.html");
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
				new HelpWindow(editorPane);
            } catch (java.io.IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: HelpFile.html");
        }
 
        return;
    }
 
 
 
	public class HelpWindow extends JFrame{
 
		public HelpWindow(JEditorPane editorPane) {
		super("Help Window");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Othello.helpActive = false;
				setVisible(false);
			}
		});
 
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
							JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(editorScrollPane);
		setSize(600,400);
		setVisible(true);
		helpActive = true;
		}
	}
 
    public HyperlinkListener createHyperLinkListener1() {
	return new HyperlinkListener() {
	    public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    if (e instanceof HTMLFrameHyperlinkEvent) {
			((HTMLDocument)editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
			    (HTMLFrameHyperlinkEvent)e);
		    } else {
			try {
			    editorPane.setPage(e.getURL());
			} catch (java.io.IOException ioe) {
			    System.out.println("IOE: " + ioe);
			}
		    }
		}
	    }
	};
    }
 
	public static void main(String[] args) {
		try {
		UIManager.setLookAndFeel(
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) { }
		Othello app = new Othello();
	}
 
}
