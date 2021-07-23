import javax.swing.JLabel;

public class Vozilo extends Thread {
	static private int count=0;
	String marka;
	String model;
	int godiste;
	double maxBrzina;
	double trenutnaBrzina;
	Koordinate trKoo;
	Koordinate preKoo;
	char smjer;
	char put;
	
	public Vozilo(double _maxBrzina, char _put)
	{ 
		maxBrzina = _maxBrzina;
		trenutnaBrzina = (0.5 + Math.random()*(maxBrzina-0.5))*1000;
		marka = "marka"+count;
		model = "model"+count;
		godiste = 1990 + count++;
		put = _put;
	}
	
	@Override
	public void run() {
		
		usaglasavanjeBrzine();
		
		Koordinate k = sledeciKorak();
		if(provjeraPruznogPrelaza(k))
		{
			GUI.guiMapa[k.i][k.j].add((JLabel)GUI.guiMapa[trKoo.i][trKoo.j].getComponents()[0]);
			preKoo.i = trKoo.i;
			preKoo.j = trKoo.j;
			trKoo = k;
		}
		
		//kada automobil nestane sa mape treba umanjiti broj vozila na putovima i skontati ko cuva referencu na neki niz trenutnih vozila na putovima
		
	}
	
	
	
	private boolean provjeraPruznogPrelaza(Koordinate sledeciKorak) {  //provjerava da li su na sledecem koraku pruzni prelaz i ako jeste da li moze preci preko pruge ili ne
		// TODO Auto-generated method stub
		return false;
	}

	private void usaglasavanjeBrzine() {
		
		//if(GUI.mapa[trKoo.i][trKoo.j].getCompone)
		
	}

	Koordinate sledeciKorak()//  TODO uraditi i za pruzne prelaze
	{

		if( trKoo.j < 29 && (GUI.mapa[trKoo.i][trKoo.j+1] == smjer && trKoo.j+1 != preKoo.j)) //provjera desno
		{
//			preKoo.i = trKoo.i;
//			preKoo.j = trKoo.j;
			return new Koordinate(trKoo.i,trKoo.j+1);
			
		}
		
		else if( trKoo.j > 0 && (GUI.mapa[trKoo.i][trKoo.j-1] == smjer && trKoo.j-1 != preKoo.j)) //provjera lijevo
		{
//			preKoo.i = trKoo.i;
//			preKoo.j = trKoo.j;
			
			return new Koordinate(trKoo.i,trKoo.j-1);
		}
		else if( trKoo.i > 0 && (GUI.mapa[trKoo.i-1][trKoo.j] == smjer && trKoo.i-1 != preKoo.i))  //provjera gore
		{
//			preKoo.i = trKoo.i;
//			preKoo.j = trKoo.j;
			return new Koordinate(trKoo.i-1,trKoo.j);
		}
		
		else if(trKoo.i < 29 && (GUI.mapa[trKoo.i+1][trKoo.j] == smjer && trKoo.i+1 != preKoo.i)) //provjera dole
		{
//			preKoo.i = trKoo.i;
//			preKoo.j = trKoo.j;
			return new Koordinate(trKoo.i+1,trKoo.j);
		}
		else {//TODO treba izbrisati auto sa guia
//			trKoo.i = trKoo.j = -1;
			return new Koordinate(-1,-1);
		}
	}
	

	public static void main(String arsdrd[])
	{
		/*Vozilo vozilo = new Vozilo(100, 'C');
		vozilo.smjer = '1';
		vozilo.trKoo = new Koordinate(29,22);
		vozilo.preKoo = new Koordinate(-1,-1);
		GUI g = new GUI();
		
		while(vozilo.trKoo.i != -1)
		{
			vozilo.kretanje();
			System.out.println("("+vozilo.trKoo.i+","+vozilo.trKoo.j+")");
		}*/
	}
	
}
