import java.util.Random;

public class Vagon implements IMoveable{
	
	static private int count = 0;

	int duzinaVagona;
	int IDVagona;
	boolean jePosebneNamjene;
	
	
	Koordinate trKoo;
	Koordinate preKoo;
	
	public Vagon(boolean _jePosebneNamjene) {
		IDVagona = count++;
		duzinaVagona = new Random().nextInt(4)+1;
		jePosebneNamjene = _jePosebneNamjene;
	}
	
	@Override
	public boolean move() {
		
		 if(GUI.mapa[trKoo.i-1][trKoo.j] == 'p' || GUI.mapa[trKoo.i-1][trKoo.j] == 'x' )//provjerava gore// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i--;
			 return true;
		 }
		 else if(GUI.mapa[trKoo.i+1][trKoo.j] == 'p' || GUI.mapa[trKoo.i+1][trKoo.j] == 'x')//provjerava dole
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i++;
			 return true;
		 }
		 else if(GUI.mapa[trKoo.i][trKoo.j+1] == 'p' || GUI.mapa[trKoo.i][trKoo.j+1] == 'x')//provjerava desno// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j++;
			 return true;
		 }
		 else if(GUI.mapa[trKoo.i][trKoo.j-1] == 'p' || GUI.mapa[trKoo.i][trKoo.j-1] == 'x')//provjerava lijevo// == 0
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j--;
			 return true;
		 }

		 else if(GUI.mapa[trKoo.i-1][trKoo.j] == 's')
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i--;
			 return false;
		 }
		 else if(GUI.mapa[trKoo.i+1][trKoo.j] == 's')
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.i++;
			 return false;
		 }
			 
		 else if(GUI.mapa[trKoo.i][trKoo.j+1] == 's')
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j++;
			 return false;
		 }
		 
		 else if(GUI.mapa[trKoo.i][trKoo.j-1] == 's')
		 {
			 preKoo = new Koordinate(trKoo);
			 trKoo.j--;
			 return false;
		 }
		return false;
	}

}
