import java.util.ArrayList;

public class ZeljeznickaStanica {

	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;

	char nazivStanice;
	Koordinate koordinate;

	public ZeljeznickaStanica(char _nazivStanice, Koordinate _koordinate) {
		redUStanici = new ArrayList<>();
		nazivStanice = _nazivStanice;
		koordinate = _koordinate;
	}

	void kretanje()// sinhronizacija// kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{
		boolean flag = false;
		redUStanici.get(0).lokomotive.get(0).trKoo = new Koordinate(usmjeriKompoziciju());
		odrediSusjeda(redUStanici.get(0)).dolazneKompozicije.add(redUStanici.remove(0));
		for (Kompozicija k : dolazneKompozicije) {
			for (Lokomotiva lok : k.lokomotive)
				if(!lok.move())
				{
					udjiUStanicu(k);
					flag = true;
					
					break;
				}

			for (Vagon vagon : k.vagoni)
				if(!vagon.move() || flag)
				{
					udjiUStanicu(k);
					break;
				}
			
			flag = false;
		}
	}

	ZeljeznickaStanica odrediSusjeda(Kompozicija kompozicija) {

		if (nazivStanice == 'A') {
			return GUI.stanice.get(1);
		} else if (nazivStanice == 'B') {
			if (kompozicija.odrediste == GUI.stanice.get(0))
				return GUI.stanice.get(0);
			else
				return GUI.stanice.get(2);
		} else if (nazivStanice == 'D' || nazivStanice == 'E') {
			return GUI.stanice.get(2);
		} else {
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
			while (var.move());

		for (Vagon var : komp.vagoni)
			while (var.move());
		
		redUStanici.add(komp);
	}

	Koordinate usmjeriKompoziciju()// TODO testirati
	{
		if (nazivStanice == 'A')
			return new Koordinate(26, 2);

		else if (nazivStanice == 'B' && redUStanici.get(0).odrediste.koordinate == new Koordinate(27, 3))
			return new Koordinate(6, 5);

		else if (nazivStanice == 'B' && redUStanici.get(0).odrediste.koordinate == new Koordinate(27, 3))
			return new Koordinate(6, 8);

		else if (nazivStanice == 'C' && (redUStanici.get(0).odrediste.koordinate == new Koordinate(27, 3)
				|| redUStanici.get(0).odrediste.koordinate == new Koordinate(6, 6)))
			return new Koordinate(11, 19);

		else if (nazivStanice == 'C' && redUStanici.get(0).odrediste.koordinate == new Koordinate(1, 26))
			return new Koordinate(12, 21);

		else if (nazivStanice == 'C' && redUStanici.get(0).odrediste.koordinate == new Koordinate(25, 26))
			return new Koordinate(14, 20);

		else if (nazivStanice == 'D')
			return new Koordinate(1, 25);

		else if (nazivStanice == 'E')
			return new Koordinate(24, 26);

		return null;
	}

}
