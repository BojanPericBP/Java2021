import java.io.Serializable;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Vagon implements Serializable 
{
	private static final long serialVersionUID = 1L;
	static private int count = 0;
	int duzinaVagona;
	int IDVagona;
	boolean jePosebneNamjene;
	Koordinate trKoo;
	Koordinate preKoo;
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
	
	public Vagon(boolean jePosebneNamjeneArg) {
		IDVagona = count++;
		duzinaVagona = new Random().nextInt(4)+1;
		jePosebneNamjene = jePosebneNamjeneArg;
		trKoo = new Koordinate(-1,-1);
		preKoo = new Koordinate(-1,-1);
	}

	synchronized public boolean move() 
	{
		
		 if((GUI.mapa[trKoo.i-1][trKoo.j] == 'p' || GUI.mapa[trKoo.i-1][trKoo.j] == 'x' || GUI.mapa[trKoo.i-1][trKoo.j] == 's')
			 && (trKoo.i-1 != preKoo.i))//provjerava gore// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i--;
			 
			 if(GUI.mapa[trKoo.i][trKoo.j]!='s') 
				 GUI.guiMapa[trKoo.i][trKoo.j].add((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
			 
			 
			 else if(GUI.mapa[trKoo.i][trKoo.j] == 's') {
				 GUI.guiMapa[preKoo.i][preKoo.j].remove((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.mapa[trKoo.i+1][trKoo.j] == 'p' || GUI.mapa[trKoo.i+1][trKoo.j] == 'x' || GUI.mapa[trKoo.i+1][trKoo.j] == 's')
			 && (trKoo.i+1 != preKoo.i))//provjerava dole
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i++;
			 
			 if(GUI.mapa[trKoo.i][trKoo.j]!='s') 
				 GUI.guiMapa[trKoo.i][trKoo.j].add((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.i][trKoo.j] == 's') {		//ovo srediti
				 GUI.guiMapa[preKoo.i][preKoo.j].remove((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
				 return false; // ovo srediti
				}
			 return true;
		 }
		 else if((GUI.mapa[trKoo.i][trKoo.j+1] == 'p' || GUI.mapa[trKoo.i][trKoo.j+1] == 'x' || GUI.mapa[trKoo.i][trKoo.j+1] == 's')
			 && (trKoo.j+1 != preKoo.j))//provjerava desno// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j++;
			 
			 if(GUI.mapa[trKoo.i][trKoo.j]!='s') 
				 GUI.guiMapa[trKoo.i][trKoo.j].add((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.i][trKoo.j] == 's') {
				 GUI.guiMapa[preKoo.i][preKoo.j].remove((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		 else if((GUI.mapa[trKoo.i][trKoo.j-1] == 'p' || GUI.mapa[trKoo.i][trKoo.j-1] == 'x' || GUI.mapa[trKoo.i][trKoo.j-1] == 's')
			 && (trKoo.j-1 != preKoo.j))//provjerava lijevo// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j--;
			 
			 if(GUI.mapa[trKoo.i][trKoo.j]!='s') 
				 GUI.guiMapa[trKoo.i][trKoo.j].add((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.i][trKoo.j] == 's') {
				 GUI.guiMapa[preKoo.i][preKoo.j].remove((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
				 return false;
			}
			 return true;
		 }

		return false;
	}

}
