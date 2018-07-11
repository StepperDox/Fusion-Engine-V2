package utility;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import net.miginfocom.swing.MigLayout;

public class Console {
	private static final int WIDTH = 800, HEIGHT = 600;
	private static final String TITLE = "Fusion Engine Console V2";
	private static JFrame frame;
	private static JTextPane tPane;
	private static JTextField tField;
	private static JButton button;
	private static ImageIcon icon = new ImageIcon("res/Textures/32x32 Icon.png");
	private static SimpleAttributeSet set = new SimpleAttributeSet();
	
	public static void callStart(){
		EventQueue.invokeLater(new Runnable(){
				public void run(){
					init();
					frame.setVisible(true);
					frame.setTitle(TITLE);
					frame.setSize(WIDTH, HEIGHT);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
					frame.setIconImage(icon.getImage());
				}
		});
	}
	
	private static void init(){
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow][]", "[grow][]"));
		
		StyleConstants.setForeground(set, Color.BLACK);
		
		tPane = new JTextPane();
		tPane.setCharacterAttributes(set, true);
		tPane.setEditable(false);
		tPane.setAutoscrolls(false);
		frame.getContentPane().add(tPane, "cell 0 0 2 1,grow");
		
		tField = new JTextField();
		frame.getContentPane().add(tField, "cell 0 1,growx");
		tField.setColumns(10);
		
		button = new JButton("Submit");
		button.addActionListener(new SubmitListener(tPane, tField));
		frame.getContentPane().add(button, "cell 1 1");
	}
	
	public static void callUpdate(){
		String[] lineData = tPane.getText().split("\n");
		if(lineData.length >= 33){
			tPane.setText("");
		}
	}
	
	public static void callExit(){
		frame.dispose();
	}
	
	public static void println(String line){
		Document d = tPane.getStyledDocument();
		try {
			d.insertString(d.getLength(), ("[FUSION V2] Info: " + line + "\n"), set);
		} catch (BadLocationException e) {
			printerr(e.getLocalizedMessage());
		}
	}
	
	public static void printerr(String error){
		StyleConstants.setForeground(set, Color.RED);
		StyleConstants.setBold(set, true);
		Document d = tPane.getStyledDocument();
		try {
			d.insertString(d.getLength(), ("[FUSION V2] ERROR: " + error + "\n"), set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		StyleConstants.setForeground(set, Color.BLACK);
		StyleConstants.setBold(set, false);
	}
	
	protected static JTextPane getPane(){
		return tPane;
	}
	
	protected static JTextField getField(){
		return tField;
	}
	
	protected static void setPane(JTextPane pane){
		tPane = pane;
	}
	
	protected static void setField(JTextField field){
		tField = field;
	}
}
