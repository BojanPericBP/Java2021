import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;

public class DataForm {
	static public JTextArea textArea;
	public DataForm() {
	}
	
	static {
		
		try {
			Logger.getLogger(DataForm.class.getName()).addHandler(new FileHandler("logs/DataForm.log"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @wbp.parser.entryPoint
	 */
	void showWindow()
	{
		JFrame frame = new JFrame("Podaci o kretanjima");
		frame.setBounds(100,100,892,584);
		frame.getContentPane().setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 85, 774, 372);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		prikaziPodatke();
		frame.setVisible(true);
	}
	
	private void prikaziPodatke() {
		try 
		{
			File dir = new File("serijalizacija");
			File[] fajles = dir.listFiles();
		
			for(File f : fajles)
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.getAbsoluteFile()));
				Train k = (Train)ois.readObject();
				ois.close();
				
				textArea.append("Kompozicija: "+k.idComp+" Vrijeme kretanja: "+k.movingTime+"s "+
						"Usputne stanice: "+k.trinStationToVisit+" SIstorija kretanja: ");
				k.movingHistory.forEach( e -> textArea.append("("+e.x+","+e.y+")"));
				textArea.append("\n");
			}
		}
		catch (Exception e) {
			Logger.getLogger(DataForm.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
