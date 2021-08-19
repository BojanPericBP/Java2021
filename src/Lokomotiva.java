import java.io.Serializable;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Lokomotiva implements Serializable
{
	private static final long serialVersionUID = 1L;
	static char[] moguciPogoni = {'D', 'P', 'E'}; //D - dizel, P - parni, E - elektricni
	char pogon; 
	boolean jeUniverzalna;
	boolean jePutnicka;
	boolean jeTeretna;
	boolean jeManevarska;
	double snaga;
	int idLokomotive;
	static private int brojacLokomotiva = 0;
	Koordinate trKoo;
	Koordinate preKoo;
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Lokomotiva.log");
			Logger.getLogger(Lokomotiva.class.getName()).addHandler(handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Lokomotiva(String tipLokomotive) 
	{
		pogon = moguciPogoni[new Random().nextInt(3)];
		snaga = Math.random()*10;
		idLokomotive = brojacLokomotiva++;
		
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

	synchronized public boolean move() 
	{
		if((GUI.mapa[trKoo.i-1][trKoo.j] == 'p' || GUI.mapa[trKoo.i-1][trKoo.j] == 'x' || GUI.mapa[trKoo.i-1][trKoo.j] == 's')
				&& (trKoo.i-1 != preKoo.i))//provjerava gore// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i--;
			 if(GUI.mapa[trKoo.i][trKoo.j]!='s') 
				 GUI.guiMapa[trKoo.i][trKoo.j].add((JLabel) GUI.guiMapa[preKoo.i][preKoo.j].getComponents()[0]);
			 
			 
			 else if(GUI.mapa[trKoo.i][trKoo.j] == 's') 
			 {
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
