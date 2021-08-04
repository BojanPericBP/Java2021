import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ZeljeznickaStanica extends Thread {

	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;
	public static final long brzinaRasporedjivanja = 500;
	/*
	 * matrica susjedstva[i][j] = 0; putanja od stanice i ka stanici j je slobodna
	 * 
	 * matrica je konzistentna
	 */
	static int matricaSusjedstva[][];

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

		while (true) {

				Iterator<Kompozicija> iteratorKompozicija = redUStanici.iterator();

				while (iteratorKompozicija.hasNext()) // pronalazi kompoziciju za koju je slobodna odredjena pruga i
														// usmejru lokomotivu na odgovarajuce polje
				{
					Kompozicija kompozicija = iteratorKompozicija.next();
					ZeljeznickaStanica susjed;
					boolean jeSlobodna;
					
					synchronized (matricaSusjedstva) {
						
						jeSlobodna = prugaJeSlobodna(kompozicija);
					}
					
					if (jeSlobodna) {
						susjed = kompozicija.odrediSusjeda();
						
						for(int i = 0; i<kompozicija.lokomotive.size();++i)
						{
							kompozicija.lokomotive.get(i).preKoo = usmjeriKompoziciju(kompozicija)[0];
							kompozicija.lokomotive.get(i).trKoo = new Koordinate(usmjeriKompoziciju(kompozicija)[0]);
						}
						
						for(int i = 0; i<kompozicija.vagoni.size();++i)
						{
							kompozicija.vagoni.get(i).preKoo = new Koordinate (kompozicija.lokomotive.get(kompozicija.lokomotive.size()-1).preKoo);
							kompozicija.vagoni.get(i).trKoo = new Koordinate(kompozicija.lokomotive.get(kompozicija.lokomotive.size()-1).trKoo);
						}
						
						kompozicija.lokomotive.get(0).trKoo = usmjeriKompoziciju(kompozicija)[1];
						synchronized (this) 
						{
							matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A']++;
						}
						susjed.dolazneKompozicije.add(kompozicija);
						synchronized (GUI.frame) 
						{
							GUI.guiMapa[kompozicija.lokomotive.get(0).trKoo.i][kompozicija.lokomotive.get(0).trKoo.j].add(new JLabel(new ImageIcon(kompozicija.path)));
							SwingUtilities.updateComponentTreeUI(GUI.frame);
						}

						if (kompozicija.isAlive()) 
						{
							synchronized (kompozicija)
							{
								kompozicija.notify();
							}
						} 
						else 
						{
							kompozicija.start();
						}
						iteratorKompozicija.remove();
					}
			}

			try {
				Thread.sleep(brzinaRasporedjivanja);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	synchronized boolean prugaJeSlobodna(Kompozicija komp) // provjerava da li ima vozova u suprotnom smjeru i ako nema
															// da li je slobodan pocetak pruge ka odredistu
	{
		ZeljeznickaStanica susjed = komp.odrediSusjeda();
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

	synchronized Koordinate[] usmjeriKompoziciju(Kompozicija komp) {
		if (nazivStanice == 'A')
			return (new Koordinate[] { new Koordinate(27, 2), new Koordinate(26, 2), new Koordinate(25, 2) });// vraca
																												// niz
																												// od
																												// dvije
																												// koordinate

		// else if (nazivStanice == 'B' && komp.odrediste.koordinate.contains(new
		// Koordinate(27, 2))) // ka A
		else if (nazivStanice == 'B' && komp.odrediSusjeda().nazivStanice == 'A')
			return (new Koordinate[] { new Koordinate(6, 6), new Koordinate(6, 5), new Koordinate(7, 5) }); // prva
																											// koordinata
																											// je
																											// pozicija
																											// na
		// koju smjestam kompoziciju, a
		// druga je za provjeru
		// razmaka..

		// odredi susjeda.odrediste.koordinate.contains(new Koordinate(27, 2))

		else if (nazivStanice == 'B' && komp.odrediSusjeda().nazivStanice == 'C') // ka C
			return (new Koordinate[] { new Koordinate(6, 7), new Koordinate(6, 8), new Koordinate(6, 9) });

		else if (nazivStanice == 'C' && (komp.odrediSusjeda().nazivStanice == 'B')) // ka B
			return (new Koordinate[] { new Koordinate(12, 19), new Koordinate(11, 19), new Koordinate(10, 19) });

		else if (nazivStanice == 'C' && komp.odrediSusjeda().nazivStanice == 'D') // ka D
			return (new Koordinate[] { new Koordinate(12, 20), new Koordinate(12, 21), new Koordinate(12, 22) });

		else if (nazivStanice == 'C' && komp.odrediSusjeda().nazivStanice == 'E') // ka E
			return (new Koordinate[] { new Koordinate(13, 20), new Koordinate(14, 20), new Koordinate(15, 20) });

		else if (nazivStanice == 'D')
			return (new Koordinate[] { new Koordinate(1, 26), new Koordinate(1, 25), new Koordinate(1, 24) }); // ka C

		else if (nazivStanice == 'E')
			return (new Koordinate[] { new Koordinate(25, 26), new Koordinate(24, 26), new Koordinate(23, 26) }); // ka C

		return null;
	}

}
