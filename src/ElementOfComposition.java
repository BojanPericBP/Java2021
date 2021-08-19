import java.awt.Color;
import java.awt.Point;

import javax.swing.JLabel;

public abstract class ElementOfComposition {

	Point currentCoordinates;
	Point previousCoordinates;


	synchronized public boolean move() {
		if((GUI.trainMap[currentCoordinates.x-1][currentCoordinates.y].getBackground() == Color.LIGHT_GRAY || GUI.trainMap[currentCoordinates.x-1][currentCoordinates.y].getBackground() == Color.red || GUI.trainMap[currentCoordinates.x-1][currentCoordinates.y].getBackground() == Color.yellow)
				&& (currentCoordinates.x-1 != previousCoordinates.x))//provjerava gore
		 {
			 previousCoordinates = new Point(currentCoordinates);
			 currentCoordinates.x--;
			 if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() != Color.yellow) 
				 GUI.trainMap[currentCoordinates.x][currentCoordinates.y].add((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
			 
			 
			 else if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground()  == Color.yellow) {
				 GUI.trainMap[previousCoordinates.x][previousCoordinates.y].remove((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		 else if((GUI.trainMap[currentCoordinates.x+1][currentCoordinates.y].getBackground() == Color.LIGHT_GRAY || GUI.trainMap[currentCoordinates.x+1][currentCoordinates.y].getBackground() == Color.red || GUI.trainMap[currentCoordinates.x+1][currentCoordinates.y].getBackground() == Color.yellow)
				 && (currentCoordinates.x+1 != previousCoordinates.x))//provjerava dole
		 {
			 previousCoordinates = new Point(currentCoordinates);
			 currentCoordinates.x++;
			 if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() != Color.yellow) 
				 GUI.trainMap[currentCoordinates.x][currentCoordinates.y].add((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
			 
			 else if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() == Color.yellow) {		//ovo srediti
				 GUI.trainMap[previousCoordinates.x][previousCoordinates.y].remove((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]); //ovo srediti
				 return false; // ovo srediti
			}
			 
			 return true;
		 }
		 else if((GUI.trainMap[currentCoordinates.x][currentCoordinates.y+1].getBackground() == Color.LIGHT_GRAY || GUI.trainMap[currentCoordinates.x][currentCoordinates.y+1].getBackground() == Color.red || GUI.trainMap[currentCoordinates.x][currentCoordinates.y+1].getBackground() == Color.yellow)
				 && (currentCoordinates.y+1 != previousCoordinates.y))//provjerava desno
		 {
			 previousCoordinates = new Point(currentCoordinates);
			 currentCoordinates.y++;
			 if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() != Color.yellow) 
				 GUI.trainMap[currentCoordinates.x][currentCoordinates.y].add((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
			 
			 
			 else if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() == Color.yellow) {
				 GUI.trainMap[previousCoordinates.x][previousCoordinates.y].remove((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.trainMap[currentCoordinates.x][currentCoordinates.y-1].getBackground() == Color.LIGHT_GRAY || GUI.trainMap[currentCoordinates.x][currentCoordinates.y-1].getBackground() == Color.red || GUI.trainMap[currentCoordinates.x][currentCoordinates.y-1].getBackground() == Color.yellow)
				 && (currentCoordinates.y-1 != previousCoordinates.y))//provjerava lijevo
		 {
			 previousCoordinates = new Point(currentCoordinates);
			 currentCoordinates.y--;
			 if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() != Color.yellow) 
				 GUI.trainMap[currentCoordinates.x][currentCoordinates.y].add((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
			 
			 else if(GUI.trainMap[currentCoordinates.x][currentCoordinates.y].getBackground() == Color.yellow) {
				 GUI.trainMap[previousCoordinates.x][previousCoordinates.y].remove((JLabel) GUI.trainMap[previousCoordinates.x][previousCoordinates.y].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		
		return false;
	}
	
}
