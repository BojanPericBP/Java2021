import java.io.Serializable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Lokomotiva implements Serializable
{
	private static final long serialVersionUID = 1L;
	boolean dizelPogon;
	boolean elektroPogon;
	boolean parniPogon;
	boolean jeUniverzalna;
	boolean jePutnicka;
	boolean jeTeretna;
	boolean jeManevarska;
	double snaga;
	int idLokomotive;
	static private int brojacLokomotiva = 0;
	Koordinate trenutneKoordinate;
	Koordinate prethodneKoordinate;
	static FileHandler handler;
	String slika;
	
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
		idLokomotive = brojacLokomotiva++;
		
		//TODO izbor pogona
		
		elektroPogon=true;
		//dizelPogon=true;
		//parniPogon=true;
		
		snaga = Math.random()*10;
		
		trenutneKoordinate = new Koordinate(-1,-1);
		prethodneKoordinate = new Koordinate(-1,-1);
		
		if(elektroPogon) 
		{ 
			slika="SLIKE/elektricna.png";
		}
		else 
		{ 
			slika="SLIKE/lokomotiva.png"; 
		}
		
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
		if((GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 'p' || GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 'x' || GUI.planGrada[trenutneKoordinate.i-1][trenutneKoordinate.j] == 's')
				&& (trenutneKoordinate.i-1 != prethodneKoordinate.i))//provjerava gore// == 0
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
		 else if((GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 'p' || GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 'x' || GUI.planGrada[trenutneKoordinate.i+1][trenutneKoordinate.j] == 's')
				 && (trenutneKoordinate.i+1 != prethodneKoordinate.i))//provjerava dole
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.i++;
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') {		//ovo srediti
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]); //ovo srediti
				 return false; // ovo srediti
			}
			 
			 
			 return true;
		 }
		 else if((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 'p' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 'x' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j+1] == 's')
				 && (trenutneKoordinate.j+1 != prethodneKoordinate.j))//provjerava desno// == 0
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.j++;
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			}
			 return true;
		 }
		 else if((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 'p' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 'x' || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j-1] == 's')
				 && (trenutneKoordinate.j-1 != prethodneKoordinate.j))//provjerava lijevo// == 0
		 {
			 prethodneKoordinate = new Koordinate(trenutneKoordinate);
			 trenutneKoordinate.j--;
			 if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j]!='s') 
				 GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].add((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
			 
			 else if(GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j] == 's') {
				 GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].remove((JLabel) GUI.guiMapa[prethodneKoordinate.i][prethodneKoordinate.j].getComponents()[0]);
				 return false;
			}
			 
			 return true;
		 }
		
		return false;
	}
	
}
