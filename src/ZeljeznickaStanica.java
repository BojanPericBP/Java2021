import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ZeljeznickaStanica extends Thread {

	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;

	/*
	 * matrica susjedstva[i][j] = 0; putanja od stanice i ka stanici j je slobodna
	 * 
	 * matrica je konzistentna
	 */
	int matricaSusjedstva[][];

	char nazivStanice;
	ArrayList<Koordinate> koordinate;

	public ZeljeznickaStanica(char _nazivStanice, ArrayList<Koordinate> _koordinate) {
		matricaSusjedstva = new int[5][5];
		for (int i = 0; i < matricaSusjedstva.length; i++) {
			for (int j = 0; j < matricaSusjedstva.length; j++) {
				matricaSusjedstva[i][j] = 0;
			}
		}
		redUStanici = new ArrayList<>();
		dolazneKompozicije = new ArrayList<>();

		nazivStanice = _nazivStanice;
		koordinate = _koordinate;
	}

	@Override
	public void run() // sinhronizacija// kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{

		while (!dolazneKompozicije.isEmpty() || !redUStanici.isEmpty()) 
		{

			//synchronized (GUI.guiMapa) {

				Iterator<Kompozicija> iteratorKompozicija = redUStanici.iterator();

				while (iteratorKompozicija.hasNext() ) // pronalazi kompoziciju za koju je slobodna odredjena pruga i
														// usmejru
														// lokomotivu na odgovarajuce polje
				{
					Kompozicija kompozicija = iteratorKompozicija.next();
					ZeljeznickaStanica susjed;

					if (prugaJeSlobodna(kompozicija)) {
						susjed = odrediSusjeda(kompozicija);
						kompozicija.lokomotive.get(0).trKoo = usmjeriKompoziciju(kompozicija)[1];
						kompozicija.lokomotive.get(0).preKoo = usmjeriKompoziciju(kompozicija)[0];

						matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A']++;
						susjed.dolazneKompozicije.add(kompozicija);
						GUI.guiMapa[kompozicija.lokomotive.get(0).trKoo.i][kompozicija.lokomotive.get(0).trKoo.j]
								.add(new JLabel(new ImageIcon(kompozicija.path)));
						SwingUtilities.updateComponentTreeUI(GUI.frame);
						iteratorKompozicija.remove();
					}
				}
			//}
			
			//synchronized (GUI.guiMapa) {
				Iterator<Kompozicija> iDolazneKompozicije = dolazneKompozicije.iterator();

				while (iDolazneKompozicije.hasNext()) {
					Kompozicija k = iDolazneKompozicije.next();
					ZeljeznickaStanica susjed = null;
					if (k.kretanjeKompozicije()) {
						susjed = odrediSusjeda(k);
						if (k.odrediste.koordinate.contains(k.lokomotive.get(0).trKoo)) {
							// serijalizacija
							// ukloniti sa mape
							// eventualno udji u stanicu

							System.out.println("USAO U STANICU");
						} else {
							redUStanici.add(k);
						}

						iDolazneKompozicije.remove();

						matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A']--;

					}

					SwingUtilities.updateComponentTreeUI(GUI.frame);
				}
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//}
		//}

	}

	private boolean prugaJeSlobodna(Kompozicija komp) { // provjerava da li ima vozova u suprotnom smjeru i ako nema da
														// li je slobodan pocetak pruge ka odredistu

		ZeljeznickaStanica susjed = odrediSusjeda(komp);
		// susjed i naziv stanice dovoljan za provjeru matrice

		if (matricaSusjedstva[susjed.nazivStanice - 'A'][(nazivStanice - 'A')] == 0) {
			Koordinate kord0 = usmjeriKompoziciju(komp)[1];
			Koordinate kord1 = usmjeriKompoziciju(komp)[2];

			if (GUI.guiMapa[kord0.i][kord0.j].getComponents().length == 0
					&& GUI.guiMapa[kord1.i][kord1.j].getComponents().length == 0)
				return true;
		}
		return false;
	}

	synchronized ZeljeznickaStanica odrediSusjeda(Kompozicija kompozicija) { // vrati referencu susjedne stanice kak
																				// kojoj se
		// kompozicija krece

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
			else if (kompozicija.odrediste == GUI.stanice.get(2))
				return GUI.stanice.get(2);
			else if (kompozicija.odrediste == GUI.stanice.get(3))
				return GUI.stanice.get(3);
			else
				return GUI.stanice.get(4);
		}
	}

	Koordinate[] usmjeriKompoziciju(Kompozicija komp) {
		if (nazivStanice == 'A')
			return (new Koordinate[] { new Koordinate(27, 2), new Koordinate(26, 2), new Koordinate(25, 2) });// vraca
																												// niz
																												// od
																												// dvije
																												// koordinate

		// else if (nazivStanice == 'B' && komp.odrediste.koordinate.contains(new
		// Koordinate(27, 2))) // ka A
		else if (nazivStanice == 'B' && odrediSusjeda(komp).nazivStanice == 'A')
			return (new Koordinate[] { new Koordinate(6, 6), new Koordinate(6, 5), new Koordinate(7, 5) }); // prva
																											// koordinata
																											// je
																											// pozicija
																											// na
		// koju smjestam kompoziciju, a
		// druga je za provjeru
		// razmaka..

		// odredi susjeda.odrediste.koordinate.contains(new Koordinate(27, 2))

		else if (nazivStanice == 'B' && odrediSusjeda(komp).nazivStanice == 'C') // ka C
			return (new Koordinate[] { new Koordinate(6, 7), new Koordinate(6, 8), new Koordinate(6, 9) });

		else if (nazivStanice == 'C' && (odrediSusjeda(komp).nazivStanice == 'B')) // ka B
			return (new Koordinate[] { new Koordinate(12, 19), new Koordinate(11, 19), new Koordinate(10, 19) });

		else if (nazivStanice == 'C' && odrediSusjeda(komp).nazivStanice == 'D') // ka D
			return (new Koordinate[] { new Koordinate(12, 20), new Koordinate(12, 21), new Koordinate(12, 22) });

		else if (nazivStanice == 'C' && odrediSusjeda(komp).nazivStanice == 'E') // ka E
			return (new Koordinate[] { new Koordinate(13, 20), new Koordinate(14, 20), new Koordinate(15, 20) });

		else if (nazivStanice == 'D')
			return (new Koordinate[] { new Koordinate(1, 26), new Koordinate(1, 25), new Koordinate(1, 24) }); // ka C

		else if (nazivStanice == 'E')
			return (new Koordinate[] { new Koordinate(25, 26), new Koordinate(24, 26), new Koordinate(23, 26) }); // ka
																													// C

		return null;
	}

}
