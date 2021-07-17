public class Vozilo {
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
	
	void kretanje()//setovati trenutne i prethodne koordinate TODO uraditi i za pruzne prelaze
	{

		if( trKoo.j < 29 && (GUI.mapa[trKoo.i][trKoo.j+1] == smjer && trKoo.j+1 != preKoo.j)) //provjera desno
		{
			preKoo.i = trKoo.i;
			preKoo.j = trKoo.j;
			trKoo.j++;
		}
		
		else if( trKoo.j > 0 && (GUI.mapa[trKoo.i][trKoo.j-1] == smjer && trKoo.j-1 != preKoo.j)) //provjera lijevo
		{
			preKoo.i = trKoo.i;
			preKoo.j = trKoo.j;
			trKoo.j--;
		}
		else if( trKoo.i > 0 && (GUI.mapa[trKoo.i-1][trKoo.j] == smjer && trKoo.i-1 != preKoo.i))  //provjera gore
		{
			preKoo.i = trKoo.i;
			preKoo.j = trKoo.j;
			trKoo.i--;
		}
		
		else if(trKoo.i < 29 && (GUI.mapa[trKoo.i+1][trKoo.j] == smjer && trKoo.i+1 != preKoo.i)) //provjera dole
		{
			preKoo.i = trKoo.i;
			preKoo.j = trKoo.j;
			trKoo.i++;
		}
		else {//TODO treba izbrisati auto sa guia
			trKoo.i = trKoo.j = -1;
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
