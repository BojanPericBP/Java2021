import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ZeljeznickaStanica extends Thread implements Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;
	static int matricaSusjedstva[][]; //matrica susjedstva[i][j] = 0; putanja od stanice i ka stanici j je slobodna, matrica je konzistentna
	char nazivStanice;
	ArrayList<Point> koordinate;
	static 
	{
		try
		{
			Logger.getLogger(ZeljeznickaStanica.class.getName()).addHandler(new FileHandler("Error logs/ZeljeznickaStanica.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ZeljeznickaStanica(char _nazivStanice, ArrayList<Point> _koordinate)
	{
		matricaSusjedstva = new int[5][5];
		for (int i = 0; i < matricaSusjedstva.length; i++)
		{
			for (int j = 0; j < matricaSusjedstva.length; j++)
			{
				matricaSusjedstva[i][j] = 0;
			}
		}
		redUStanici = new ArrayList<>();
		dolazneKompozicije = new ArrayList<>();

		nazivStanice = _nazivStanice;
		koordinate = _koordinate;
	}

	@Override
	public void run() // kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{

		while (GUI.simulacijaUToku)
		{
			Iterator<Kompozicija> iteratorKompozicija = redUStanici.iterator();

			while (iteratorKompozicija.hasNext()) // pronalazi kompoziciju za koju je slobodna odredjena pruga i
													// usmejru lokomotivu na odgovarajuce polje
			{
				Kompozicija kompozicija = iteratorKompozicija.next();
				ZeljeznickaStanica susjed;
				boolean jeSlobodna;

				synchronized (matricaSusjedstva)
				{
					jeSlobodna = prugaJeSlobodna(kompozicija);
				}

				if (jeSlobodna)
				{
					susjed = kompozicija.odrediSusjeda();

					if (matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A'] != 0)
					{
						long min = kompozicija.brzinaKretanja;
						// prodjikroz dolazne dolazneKompozicije susjeda i uzmi
						for (Kompozicija k : susjed.dolazneKompozicije)
						{
							if (k.prethodnaStanica.nazivStanice == nazivStanice && k.brzinaKretanja > min)
							{
								min = k.brzinaKretanja;
							}
						}
						kompozicija.tmpBrzina = kompozicija.brzinaKretanja;
						kompozicija.brzinaKretanja = min;
					}

					for (int i = 0; i < kompozicija.lokomotive.size(); ++i)
					{
						kompozicija.lokomotive.get(i).preKoo = usmjeriKompoziciju(kompozicija)[0];
						kompozicija.lokomotive.get(i).trKoo = new Point(usmjeriKompoziciju(kompozicija)[0]);
					}

					for (int i = 0; i < kompozicija.vagoni.size(); ++i)
					{
						kompozicija.vagoni.get(i).preKoo = new Point(
								kompozicija.lokomotive.get(kompozicija.lokomotive.size() - 1).preKoo);
						kompozicija.vagoni.get(i).trKoo = new Point(
								kompozicija.lokomotive.get(kompozicija.lokomotive.size() - 1).trKoo);
					}
					if(kompozicija.polazak.koordinate.contains(kompozicija.lokomotive.get(0).trKoo))
						kompozicija.vrijemeKretanja = System.currentTimeMillis();
					kompozicija.lokomotive.get(0).trKoo = usmjeriKompoziciju(kompozicija)[1];
					kompozicija.istorijaKretanja.add(new Point(kompozicija.lokomotive.get(0).trKoo));
					synchronized (this)
					{
						matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A']++;
					}
					susjed.dolazneKompozicije.add(kompozicija);
					synchronized (GUI.frame)
					{
						GUI.guiMapa[kompozicija.lokomotive.get(0).trKoo.x][kompozicija.lokomotive.get(0).trKoo.y]
								.add(new JLabel(new ImageIcon("resource/train.png")));
						((JLabel) GUI.guiMapa[kompozicija.lokomotive.get(0).trKoo.x][kompozicija.lokomotive
								.get(0).trKoo.y].getComponent(0)).setName(kompozicija.brzinaKretanja + "k");
						SwingUtilities.updateComponentTreeUI(GUI.frame);
					}

					synchronized(this)
					{
						iteratorKompozicija.remove();
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
				}
			}

			try
			{
				Thread.sleep(300);
			}
			catch (Exception e)
			{
				Logger.getLogger(ZeljeznickaStanica.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
		}

	}

	synchronized boolean prugaJeSlobodna(Kompozicija komp) // provjerava da li ima vozova u suprotnom smjeru i ako nema
															// da li je slobodan pocetak pruge ka odredistu
	{
		ZeljeznickaStanica susjed = komp.odrediSusjeda();
		// susjed i naziv stanice dovoljan za provjeru matrice

		if (matricaSusjedstva[susjed.nazivStanice - 'A'][(nazivStanice - 'A')] == 0)
		{
			Point kord0 = usmjeriKompoziciju(komp)[1];
			Point kord1 = usmjeriKompoziciju(komp)[2];

			if (GUI.guiMapa[kord0.x][kord0.y].getComponents().length == 0
					&& GUI.guiMapa[kord1.x][kord1.y].getComponents().length == 0)
				return true;
		}
		return false;
	}

	synchronized Point[] usmjeriKompoziciju(Kompozicija komp)
	{
		if (nazivStanice == 'A')
			return (new Point[]
			{ new Point(27, 2), new Point(26, 2), new Point(25, 2) });// vraca
																						// niz
																						// od
																						// tri
																						// koordinate
 // ka A
		else if (nazivStanice == 'B' && komp.odrediSusjeda().nazivStanice == 'A')
			return (new Point[]
			{ new Point(6, 6), new Point(6, 5), new Point(7, 5) }); // prva
																					// koordinata
																					// je
																					// pozicija
																					// na
		// koju smjestam kompoziciju, a
		// druga je za provjeru
		// razmaka..

		// odredi susjeda.odrediste.koordinate.contains(new Point(27, 2))

		else if (nazivStanice == 'B' && komp.odrediSusjeda().nazivStanice == 'C') // ka C
			return (new Point[]
			{ new Point(6, 7), new Point(6, 8), new Point(6, 9) });

		else if (nazivStanice == 'C' && (komp.odrediSusjeda().nazivStanice == 'B')) // ka B
			return (new Point[]
			{ new Point(12, 19), new Point(11, 19), new Point(10, 19) });

		else if (nazivStanice == 'C' && komp.odrediSusjeda().nazivStanice == 'D') // ka D
			return (new Point[]
			{ new Point(12, 20), new Point(12, 21), new Point(12, 22) });

		else if (nazivStanice == 'C' && komp.odrediSusjeda().nazivStanice == 'E') // ka E
			return (new Point[]
			{ new Point(13, 20), new Point(14, 20), new Point(15, 20) });

		else if (nazivStanice == 'D')
			return (new Point[]
			{ new Point(1, 26), new Point(1, 25), new Point(1, 24) }); // ka C

		else if (nazivStanice == 'E')
			return (new Point[]
			{ new Point(25, 26), new Point(24, 26), new Point(23, 26) }); // ka
																							// C

		return null;
	}

}
