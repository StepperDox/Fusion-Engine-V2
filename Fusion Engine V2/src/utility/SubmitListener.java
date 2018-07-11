package utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class SubmitListener implements ActionListener {
	private JTextPane tPane;
	private JTextField tField;

	public SubmitListener(JTextPane pane, JTextField field) {
		tPane = pane;
		tField = field;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String line = tField.getText();
		String[] sLine = line.split(" ");
		tField.setText(null);
		if (sLine[0].equalsIgnoreCase("clear")) {
			tPane.setText(null);
		} else if (sLine[0].equalsIgnoreCase("pause")) {
			try {
				Thread.sleep((long) Float.parseFloat(sLine[1]));
			} catch (NumberFormatException e) {
				Console.printerr(e.getLocalizedMessage());
			} catch (InterruptedException e) {
				Console.printerr(e.getLocalizedMessage());
			}
		} else if (sLine[0].equalsIgnoreCase("cfg")) {
			Console.println("Preparing to edit Config...");
			execCommand(sLine);
		} else if (sLine[0].equalsIgnoreCase("stop") || sLine[0].equalsIgnoreCase("exit")
				|| sLine[0].equalsIgnoreCase("close")) {
			System.exit(0);
		}
	}

	private void execCommand(String[] cLine){
		try{
			if(cLine[2] == null || cLine[3] == null){
				Console.printerr("You must add variables to make this a valid command!");
				return;
			}else if(cLine[2] == null && cLine[3] == null){
				Console.printerr("You must add variables to make this a valid command!");
				return;
			}else if(cLine[1].equalsIgnoreCase("repair")){
				ConfigManager.buildNewBase();
				Console.println("Successfully Repaired Config File!");
			}else if(cLine[1].equalsIgnoreCase("edit") && cLine[2] != null && cLine[3] != null){
				ConfigManager.setData(cLine[2], cLine[3]);
				Console.println("Sucessfully changed " + cLine[2] + " to " + cLine[3]);
			}else if(cLine[1].equalsIgnoreCase("add") && cLine[2] != null && cLine[3] != null){
				ConfigManager.addData(cLine[2], cLine[3]);
				Console.println("Sucessfully added " + cLine[2] + ", value of " + cLine[3]);
			}else{
				Console.printerr("Supported subcommands are: ");
				Console.printerr("repair");
				Console.printerr("edit <ID> <VALUE>");
				Console.printerr("add <ID> <VALUE>");
			}
		}catch(ArrayIndexOutOfBoundsException e){
			Console.printerr("You must add variables to make this a valid command!");
			Console.printerr("Supported subcommands are: ");
			Console.printerr("repair");
			Console.printerr("edit <ID> <VALUE>");
			Console.printerr("add <ID> <VALUE>");
		}
	}
}
