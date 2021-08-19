import java.io.Serializable;
import java.util.Random;
import java.util.logging.*;
import javax.swing.JLabel;

public class Vagon implements Serializable 
{
	public Vagon(boolean jePosebneNamjeneArg) 
	{
		idVagona = count++;
		jePosebneNamjene = jePosebneNamjeneArg;
		trenutneKoordinate = new Koordinate(-1,-1);
		prethodneKoordinate = new Koordinate(-1,-1);
	}
	
	static FileHandler handler;
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Vagon.log");
			Logger.getLogger(Vagon.class.getName()).addHandler(handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = 1L;
	static private int count = 0;
	int duzinaVagona = new Random().nextInt(8)+1;
	int idVagona;
	boolean jePosebneNamjene;
	Koordinate trenutneKoordinate;
	Koordinate prethodneKoordinate;

	synchronized public boolean move() 
	{
		 if((GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 'p' || GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 'x' || GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 's') && (trenutneKoordinate.i-1 != prethodneKoordinate.i))//provjerava gore
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.i--;
			 
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') 
			 {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			 }
			 return true;
		 }
		 else if((GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 'p' || GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 'x' || GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 's') && (trenutneKoordinate.i+1 != prethodneKoordinate.i))//provjerava dole
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.i++;
			 
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') 
			 {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			 }
			 return true;
		 }
		 else if((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 'p' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 'x' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 's') && (trenutneKoordinate.j+1 != prethodneKoordinate.j))//provjerava desno
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.j++;
			 
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') 
			 {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			 } 
			 return true;
		 }
		 else if((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 'p' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 'x' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 's') && (trenutneKoordinate.j-1 != prethodneKoordinate.j))//provjerava lijevo// == 0
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.j--;
			 
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') 
			 {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			 }
			 return true;
		 }

		return false;
	}

}
