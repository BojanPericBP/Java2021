import java.util.Random;

import javax.swing.JLabel;


public class Lokomotiva{

	static private int count = 0;
	
	char pogon; //dizel-1, parni-2, elektricni-3
	boolean jeUniverzalna;
	boolean jePutnicka;
	boolean jeTeretna;
	boolean jeManevarska;
	double snaga;
	int idLokomotive;
	Koordinate trKoo;
	Koordinate preKoo;
	
	public Lokomotiva(String tipLokomotive) {
		pogon = (char) (new Random().nextInt(2)+49);
		snaga = Math.random()*10;
		idLokomotive = count++;
		
		trKoo = new Koordinate(-1,-1);
		preKoo = new Koordinate(-1,-1);
		
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
				 GUI.guiMapa[preKoo.i][preKoo.j].remove((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]); //ovo srediti
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
