import java.util.Random;


public class Lokomotiva implements IMoveable{

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

	@Override
	public boolean move() {
		if(GUI.mapa[trKoo.i-1][trKoo.j] == 'p' || GUI.mapa[trKoo.i-1][trKoo.j] == 'x' || GUI.mapa[trKoo.i-1][trKoo.j] == 's')//provjerava gore// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i--;
			 
		 }
		 else if(GUI.mapa[trKoo.i+1][trKoo.j] == 'p' || GUI.mapa[trKoo.i+1][trKoo.j] == 'x' || GUI.mapa[trKoo.i+1][trKoo.j] == 's')//provjerava dole
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i++;
		 }
		 else if(GUI.mapa[trKoo.i][trKoo.j+1] == 'p' || GUI.mapa[trKoo.i][trKoo.j+1] == 'x' || GUI.mapa[trKoo.i][trKoo.j+1] == 's')//provjerava desno// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j++;
		 }
		 else if(GUI.mapa[trKoo.i][trKoo.j-1] == 'p' || GUI.mapa[trKoo.i][trKoo.j-1] == 'x' || GUI.mapa[trKoo.i][trKoo.j-1] == 's')//provjerava lijevo// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j--;
		 }
		return false;
	}
	
}
