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

public class FormaPodaci {
	static public JTextArea textArea;
	public FormaPodaci() {
	}
	
	static {
		
		try {
			Logger.getLogger(FormaPodaci.class.getName()).addHandler(new FileHandler("Error logs/FormaPodaci.log"));
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
			File folder = new File("serijalizacija");
			File[] fajlovi = folder.listFiles();
		
			for(File f : fajlovi)
			{
			
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.getAbsoluteFile()));
				Kompozicija k = (Kompozicija)ois.readObject();
				ois.close();
				textArea.append("Kompozicija: "+k.idKompozicije+" Vrijeme kretanja: "+k.vrijemeKretanja+"s "+
						" Usputne stanice: "+k.linija+" Istorija kretanja: ");
				k.istorijaKretanja.forEach( e -> textArea.append("("+e.x+","+e.y+")"));
				textArea.append("\n");
			}
		}
		catch (Exception e) {
			Logger.getLogger(FormaPodaci.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
