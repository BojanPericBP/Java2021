import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.logging.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FormaPodaci 
{
	static public JTextArea textArea;
	JFrame frame;
	static FileHandler handlerFajl;
	DefaultTableModel tableModel;
	
	static
	{
		try 
		{
			handlerFajl=new FileHandler("Error logs/FormaPodaci.log");
			Logger.getLogger(FormaPodaci.class.getName()).addHandler(handlerFajl);
		} catch (Exception e) { e.printStackTrace(); }
	}

	public FormaPodaci() 
	{
		frame = new JFrame("PODACI O KRETANJIMA SVIH KOMPOZICIJA");
		frame.setBounds(100,100,1500,600);
		frame.getContentPane().setLayout(new BorderLayout());
		
		tableModel = new DefaultTableModel();
	    JTable table = new JTable(tableModel);
	    tableModel.addColumn("Kompozicija");
	    tableModel.addColumn("Br lokomotiva");
	    tableModel.addColumn("Br vagona");
	    tableModel.addColumn("vrijeme kretanja");
	    tableModel.addColumn("Usputne stanice");
	    tableModel.addColumn("Istorija kretanja");
	    table.getColumnModel().getColumn(0).setMaxWidth(90);
	    table.getColumnModel().getColumn(0).setMinWidth(90);
	    table.getColumnModel().getColumn(1).setMinWidth(80);
	    table.getColumnModel().getColumn(1).setMaxWidth(80);
	    table.getColumnModel().getColumn(2).setMinWidth(60);
	    table.getColumnModel().getColumn(2).setMaxWidth(60);
	    table.getColumnModel().getColumn(3).setMinWidth(90);
	    table.getColumnModel().getColumn(3).setMaxWidth(90);
	    table.getColumnModel().getColumn(4).setMinWidth(95);
	    table.getColumnModel().getColumn(4).setMaxWidth(95);
	    frame.add(new JScrollPane(table));
	    frame.setVisible(false);
	}
	
	
	public void prikaziPodatke() 
	{
		tableModel.setRowCount(0);
		try 
		{
			File folder = new File("serijalizacija");
			File[] fajlovi = folder.listFiles();
			for(File f : fajlovi)
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.getAbsoluteFile()));
				Kompozicija k = (Kompozicija)ois.readObject();
				ois.close();
				
				StringBuffer istorija = new StringBuffer("");
				k.istorijaKretanja.forEach( e -> istorija.append(e.toString()) );

				tableModel.insertRow(tableModel.getRowCount(), new Object[] { "Kompozicija"+k.idKompozicije, k.lokomotive.size(), k.vagoni.size(), k.vrijemeKretanja+"s", k.usputneStanice, istorija.toString() });
			}
		}
		catch (Exception e) 
		{
			Logger.getLogger(FormaPodaci.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
