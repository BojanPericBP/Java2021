import java.awt.Point;

import javax.swing.JLabel;

public abstract class elementKompozicije {

	Point trKoo;
	Point preKoo;
	public elementKompozicije() {
		
	}

	synchronized public boolean move() {
		if((GUI.mapa[trKoo.x-1][trKoo.y] == 'p' || GUI.mapa[trKoo.x-1][trKoo.y] == 'x' || GUI.mapa[trKoo.x-1][trKoo.y] == 's')
				&& (trKoo.x-1 != preKoo.x))//provjerava gore
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
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]); //ovo srediti
				 return false; // ovo srediti
			}
			 
			 return true;
		 }
		 else if((GUI.mapa[trKoo.x][trKoo.y+1] == 'p' || GUI.mapa[trKoo.x][trKoo.y+1] == 'x' || GUI.mapa[trKoo.x][trKoo.y+1] == 's')
				 && (trKoo.y+1 != preKoo.y))//provjerava desno
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
				 && (trKoo.y-1 != preKoo.y))//provjerava lijevo
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
