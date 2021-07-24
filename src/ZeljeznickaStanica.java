import java.util.ArrayList;

public class ZeljeznickaStanica {

	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;
	
	/*
	 * matrica susjedstva[i][j] = true;
	 * putanja od stanice i ka stanici j je zauzeta
	 * => putanja od j ka i slobodna ako je slobodno drugo polje polazne stanice (stanice j)
	 * matrica je konzistentna
	 * */
	boolean matricaSusjedstva[][];

	char nazivStanice;
	Koordinate koordinate;

	public ZeljeznickaStanica(char _nazivStanice, Koordinate _koordinate) {
		matricaSusjedstva = new boolean[5][5];
		for (int i = 0; i < matricaSusjedstva.length; i++) {
			for (int j = 0; j < matricaSusjedstva.length; j++) {
				matricaSusjedstva[i][j] = false; 
			}
		}
		redUStanici = new ArrayList<>();
		nazivStanice = _nazivStanice;
		koordinate = _koordinate;
	}

	void kretanje()// sinhronizacija// kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{
		boolean flag = false;

		for (Kompozicija kompozicija : redUStanici) {

			if (prugaJeSlobodna(kompozicija)) {
				kompozicija.lokomotive.get(0).trKoo = usmjeriKompoziciju(kompozicija)[0];
				redUStanici.remove(kompozicija);
			}
		}

		for (Kompozicija k : dolazneKompozicije) 
		{
			

			
		}
	}

	private boolean prugaJeSlobodna(Kompozicija komp) { //provjerava da li ima vozova u suprotnom smjeru i ako nema da li je slobodan pocetak pruge ka odredistu
		
		ZeljeznickaStanica susjed = odrediSusjeda(komp);
		//susjed i naziv stanice dovoljan za provjeru matrice
		
		if(!matricaSusjedstva[(nazivStanice-'A')][susjed.nazivStanice-'A'])
			{
				Koordinate kord = usmjeriKompoziciju(komp)[1];
				if(GUI.guiMapa[kord.i][kord.j].getComponents().length == 0)
					return true;
			}
		return false;
	}

	ZeljeznickaStanica odrediSusjeda(Kompozicija kompozicija) { // vrati referencu susjedne stanice kak kojoj se
																// kompozicija krece

		if (nazivStanice == 'A') {
			return GUI.stanice.get(1);
		}
		else if (nazivStanice == 'B') {
			if (kompozicija.odrediste == GUI.stanice.get(0))
				return GUI.stanice.get(0);
			else
				return GUI.stanice.get(2);
		} 
		else if (nazivStanice == 'D' || nazivStanice == 'E') {
			return GUI.stanice.get(2);
		}
		else {
			if (kompozicija.odrediste == GUI.stanice.get(0) || kompozicija.odrediste == GUI.stanice.get(1))
				return GUI.stanice.get(1);
			else if (kompozicija.odrediste == GUI.stanice.get(3))
				return GUI.stanice.get(3);
			else
				return GUI.stanice.get(4);
		}
	}
	
	

	void udjiUStanicu(Kompozicija komp) {
		for (Lokomotiva var : komp.lokomotive)
			while (var.move())
				;

		for (Vagon var : komp.vagoni)
			while (var.move())
				;

		redUStanici.add(komp);
	}

	Koordinate[] usmjeriKompoziciju(Kompozicija komp)
	{
		if (nazivStanice == 'A')
			return  (new Koordinate[] {new Koordinate(26, 2), new Koordinate(25,2)});//vraca niz od dvije koordinate

		else if (nazivStanice == 'B' && komp.odrediste.koordinate == new Koordinate(27, 3)) // ka A
			return (new Koordinate[] {new Koordinate(6, 5), new Koordinate(7, 5)}); //prva koordinata je pozicija na koju smjestam kompoziciju, a druga je za provjeru razmaka..

		else if (nazivStanice == 'B' && komp.odrediste.koordinate == new Koordinate(27, 3)) // ka C
			return (new Koordinate[] {new Koordinate(6, 8),new Koordinate(6, 9)}); 

		else if (nazivStanice == 'C' && (komp.odrediste.koordinate == new Koordinate(27, 3) || redUStanici.get(0).odrediste.koordinate == new Koordinate(6, 6)))// ka B
			return (new Koordinate[] {new Koordinate(11, 19),new Koordinate(10, 19)}); 

		else if (nazivStanice == 'C' && komp.odrediste.koordinate == new Koordinate(1, 26)) // ka D
			return (new Koordinate[] {new Koordinate(12, 21),new Koordinate(12, 22)}); 

		else if (nazivStanice == 'C' && komp.odrediste.koordinate == new Koordinate(25, 26)) // ka E
			return (new Koordinate[] {new Koordinate(14, 20),new Koordinate(15, 20)}); 

		else if (nazivStanice == 'D')
			return (new Koordinate[] {new Koordinate(1, 25),new Koordinate(1, 24)}); //ka C

		else if (nazivStanice == 'E')
			return (new Koordinate[] {new Koordinate(24, 26),new Koordinate(23, 26)}); //ka C

		return null;
	}

}
