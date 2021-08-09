import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;

public class FormaPodaci 
{
	static public JTextArea textArea;
	JFrame frame;
	static FileHandler handler;
	
	static {
		
		try {
			handler=new FileHandler("Error logs/FormaPodaci.log");
			Logger.getLogger(FormaPodaci.class.getName()).addHandler(handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FormaPodaci() 
	{
		frame = new JFrame("PODACI O KRETANJIMA SVIH KOMPOZICIJA");
		frame.setBounds(100,100,892,584);
		frame.getContentPane().setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 85, 774, 372);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setBackground(new Color(255,255,255));
		frame.setVisible(false);
	}
	
	public void prikaziPodatke() 
	{
		try 
		{
			textArea.setText("");
			File folder = new File("serijalizacija");
			File[] fajlovi = folder.listFiles();
			for(File f : fajlovi)
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.getAbsoluteFile()));
				Kompozicija k = (Kompozicija)ois.readObject();
				ois.close();
				textArea.append(" Kompozicija"+k.idKompozicije+"\n"+" Broj lokomotiva: "+k.lokomotive.size()+", Broj vagona: "+k.vagoni.size()+", Ukupno vrijeme kretanja: "+k.vrijemeKretanja+"s, "+
						" Usputne stanice: "+k.usputneStanice+"\n Istorija kretanja: ");
				k.istorijaKretanja.forEach( e -> textArea.append(e.toString()+" "));
				textArea.append("\n\n");
			}
		}
		catch (Exception e) 
		{
			Logger.getLogger(FormaPodaci.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
