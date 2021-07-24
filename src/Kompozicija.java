import java.util.ArrayList;

public class Kompozicija /*extends Thread implements Serializable*/ {
	
	private static final long serialVersionUID = 1L;
	
	final int maxLokomotiva = 2;
	final int maxVagona = 5;
	
	ArrayList<Lokomotiva> lokomotive;
	ArrayList<Vagon> vagoni;
	double brzinaKretanja;
	ZeljeznickaStanica odrediste;  //odredisna stanica na koju kompozicija treba da stigne
	ZeljeznickaStanica polazak;
	
	boolean jeNaPruzi;
		
	public Kompozicija(int _brLokomotiva, int _brVagona, String _raspored, double _brzina, ZeljeznickaStanica _polazak, ZeljeznickaStanica _odrediste) throws Exception
	{
		if(_brLokomotiva > maxLokomotiva || _brLokomotiva < 1 || _brVagona > maxVagona)
			throw new Exception("Kompozicija nije validna!");
		brzinaKretanja = _brzina;
		polazak = _polazak;
		odrediste = _odrediste;
		vagoni = new ArrayList<>(_brVagona);
		lokomotive = new ArrayList<>(_brLokomotiva);
		kreirajKompoziciju(_raspored);
	}
	
	private void kreirajKompoziciju(String raspored)
	{
		String[] niz = raspored.split(".");
		
		for (String string : niz) {
			
			 if('V' == string.charAt(0)) //vagoni
			 {
				if('P'==string.charAt(1))//putnicki vagoni
				{
					if('S'==string.charAt(2))//spavaci
						{
							vagoni.add(new PutnickiVagonSpavaci());
						}
					
					else {//restoran
						vagoni.add(new PutnickiVagonRestoran());
					}
				}
				
				else if('T'==string.charAt(1))
				{//teretni
					vagoni.add(new TeretniVagon());
				}
				
				else 
				{//posebne namjene
					vagoni.add(new Vagon(true));
				}
				
			 }
			 else { //lokomotive
				if('P'==string.charAt(1))//putnicka lokomotiva
				{
					lokomotive.add(new Lokomotiva("putnicka"));
				}
				else if('T'==string.charAt(1))//teretna lokomotiva
				{
					lokomotive.add(new Lokomotiva("teretna"));
				}
				else if('U'==string.charAt(1)){//univerzalna lokomotiva
					lokomotive.add(new Lokomotiva("univerzalna"));
				}
				
				else if(string.equals("LM"))// manevarska lokomotiva
				{
					lokomotive.add(new Lokomotiva("manevarska"));
				}
			}
		}
	}
	
	
	void metoda()
	{
		boolean flag = false;
		for (Lokomotiva lok : lokomotive)
			if (!lok.move()) {
				//udjiUStanicu(k);
				flag = true;
				break;
			}

		for (Vagon vagon : vagoni)
			if (!vagon.move() || flag) {
				//udjiUStanicu(k);
				break;
			}
		flag = false;
	}
	
	public static void main(String argp[]) throws Exception
	{
		//Kompozicija kompozicija = new Kompozicija(2, 3, "VN,LU,VPS,VPR,LP", 45, 'a', 'b');
		//kompozicija.kompozicija.forEach(System.out::println);
		//System.out.println("TEST");
	}
	

	/*@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}*/
}
