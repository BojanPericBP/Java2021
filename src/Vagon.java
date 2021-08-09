import java.awt.Point;
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
	Point trKoo;
	Point preKoo;
	
	static 
	{
		try
		{
			Logger.getLogger(Vagon.class.getName()).addHandler(new FileHandler("Error logs/Vagon.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Vagon(boolean _jePosebneNamjene) {
		IDVagona = count++;
		duzinaVagona = new Random().nextInt(4)+1;
		jePosebneNamjene = _jePosebneNamjene;
		trKoo = new Point();
		preKoo = new Point();
	}

	synchronized public boolean move() {
		
		 if((GUI.mapa[trKoo.x-1][trKoo.y] == 'p' || GUI.mapa[trKoo.x-1][trKoo.y] == 'x' || GUI.mapa[trKoo.x-1][trKoo.y] == 's')
			 && (trKoo.x-1 != preKoo.x))//provjerava gore// == 0
		 {
			 preKoo = new Point(trKoo);
			 trKoo.x--;
			 
			 if(GUI.mapa[trKoo.x][trKoo.y]!='s') 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.x][trKoo.y] == 's') {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.mapa[trKoo.x+1][trKoo.y] == 'p' || GUI.mapa[trKoo.x+1][trKoo.y] == 'x' || GUI.mapa[trKoo.x+1][trKoo.y] == 's')
			 && (trKoo.x+1 != preKoo.x))//provjerava dole
		 {
			 preKoo = new Point(trKoo);
			 trKoo.x++;
			 
			 if(GUI.mapa[trKoo.x][trKoo.y]!='s') 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.x][trKoo.y] == 's') {		//ovo srediti
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false; // ovo srediti
				}
			 return true;
		 }
		 else if((GUI.mapa[trKoo.x][trKoo.y+1] == 'p' || GUI.mapa[trKoo.x][trKoo.y+1] == 'x' || GUI.mapa[trKoo.x][trKoo.y+1] == 's')
			 && (trKoo.y+1 != preKoo.y))//provjerava desno// == 0
		 {
			 preKoo = new Point(trKoo);
			 trKoo.y++;
			 
			 if(GUI.mapa[trKoo.x][trKoo.y]!='s') 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.x][trKoo.y] == 's') {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.mapa[trKoo.x][trKoo.y-1] == 'p' || GUI.mapa[trKoo.x][trKoo.y-1] == 'x' || GUI.mapa[trKoo.x][trKoo.y-1] == 's')
			 && (trKoo.y-1 != preKoo.y))//provjerava lijevo// == 0
		 {
			 preKoo = new Point(trKoo);
			 trKoo.y--;
			 
			 if(GUI.mapa[trKoo.x][trKoo.y]!='s') 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.mapa[trKoo.x][trKoo.y] == 's') {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 return true;
		 }

		return false;
	}

}
