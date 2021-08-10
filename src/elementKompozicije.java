import java.awt.Color;
import java.awt.Point;

import javax.swing.JLabel;

public abstract class elementKompozicije {

	Point trKoo;
	Point preKoo;
	public elementKompozicije() {
		
	}

	synchronized public boolean move() {
		if((GUI.guiMapa[trKoo.x-1][trKoo.y].getBackground() == Color.LIGHT_GRAY || GUI.guiMapa[trKoo.x-1][trKoo.y].getBackground() == Color.red || GUI.guiMapa[trKoo.x-1][trKoo.y].getBackground() == Color.yellow)
				&& (trKoo.x-1 != preKoo.x))//provjerava gore
		 {
			 preKoo = new Point(trKoo);
			 trKoo.x--;
			 if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() != Color.yellow) 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 
			 else if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground()  == Color.yellow) {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		 else if((GUI.guiMapa[trKoo.x+1][trKoo.y].getBackground() == Color.LIGHT_GRAY || GUI.guiMapa[trKoo.x+1][trKoo.y].getBackground() == Color.red || GUI.guiMapa[trKoo.x+1][trKoo.y].getBackground() == Color.yellow)
				 && (trKoo.x+1 != preKoo.x))//provjerava dole
		 {
			 preKoo = new Point(trKoo);
			 trKoo.x++;
			 if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() != Color.yellow) 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() == Color.yellow) {		//ovo srediti
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]); //ovo srediti
				 return false; // ovo srediti
			}
			 
			 return true;
		 }
		 else if((GUI.guiMapa[trKoo.x][trKoo.y+1].getBackground() == Color.LIGHT_GRAY || GUI.guiMapa[trKoo.x][trKoo.y+1].getBackground() == Color.red || GUI.guiMapa[trKoo.x][trKoo.y+1].getBackground() == Color.yellow)
				 && (trKoo.y+1 != preKoo.y))//provjerava desno
		 {
			 preKoo = new Point(trKoo);
			 trKoo.y++;
			 if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() != Color.yellow) 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 
			 else if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() == Color.yellow) {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.guiMapa[trKoo.x][trKoo.y-1].getBackground() == Color.LIGHT_GRAY || GUI.guiMapa[trKoo.x][trKoo.y-1].getBackground() == Color.red || GUI.guiMapa[trKoo.x][trKoo.y-1].getBackground() == Color.yellow)
				 && (trKoo.y-1 != preKoo.y))//provjerava lijevo
		 {
			 preKoo = new Point(trKoo);
			 trKoo.y--;
			 if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() != Color.yellow) 
				 GUI.guiMapa[trKoo.x][trKoo.y].add((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
			 
			 else if(GUI.guiMapa[trKoo.x][trKoo.y].getBackground() == Color.yellow) {
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		
		return false;
	}
	
}
