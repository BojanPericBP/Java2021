import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

import javax.swing.JLabel;


public class Lokomotiva implements Serializable{

	static private int count = 0;
	private static final long serialVersionUID = 1L;
	char pogon; //dizel-1, parni-2, elektricni-3
	boolean jeUniverzalna;
	boolean jePutnicka;
	boolean jeTeretna;
	boolean jeManevarska;
	double snaga;
	int idLokomotive;
	Point trKoo;
	Point preKoo;
	
	public Lokomotiva(String tipLokomotive) {
		pogon = (char) (new Random().nextInt(2)+49);
		snaga = Math.random()*10;
		idLokomotive = count++;
		
		trKoo = new Point(-1,-1);
		preKoo = new Point(-1,-1);
		
		if(tipLokomotive == "univerzalna")
			jeUniverzalna = true;
		else if(tipLokomotive == "putnicka")
			jePutnicka = true;
		else if(tipLokomotive == "teretna")
			jeTeretna = true;
		else if(tipLokomotive == "manevarska")
			jeManevarska = true;
		else 
			System.out.println("Tip lokomotive uz datoteci nije ispravno naveden! ");
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
				 GUI.guiMapa[preKoo.x][preKoo.y].remove((JLabel) GUI.guiMapa[preKoo.x][preKoo.y].getComponents()[0]); //ovo srediti
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
