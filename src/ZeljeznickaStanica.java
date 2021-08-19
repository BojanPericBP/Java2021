import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;
import javax.swing.*;

public class ZeljeznickaStanica extends Thread implements Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<Kompozicija> redUStanici;
	ArrayList<Kompozicija> dolazneKompozicije;
	public static final long brzinaRasporedjivanja = 200;
	static int matricaSusjedstva[][]; //putanja OD stanice i KA stanici j
	char nazivStanice;
	ArrayList<Koordinate> koordinate; 
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/ZeljeznickaStanica.log");
			Logger.getLogger(ZeljeznickaStanica.class.getName()).addHandler(handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	static 
	{
		matricaSusjedstva = new int[5][5];
		for (int i = 0; i < matricaSusjedstva.length; i++)
		{
			for (int j = 0; j < matricaSusjedstva.length; j++)
			{
				matricaSusjedstva[i][j] = 0;
			}
		}
	}
	public ZeljeznickaStanica(char nazivStaniceArg)
	{
		redUStanici = new ArrayList<>();
		dolazneKompozicije = new ArrayList<>();

		nazivStanice = nazivStaniceArg;
		koordinate = new ArrayList<>();
	}

	@Override
	public void run() // kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{
		while (GUI.simulacijaUToku)
		{
			Iterator<Kompozicija> iteratorKompozicija = redUStanici.iterator();

			while (iteratorKompozicija.hasNext()) // pronalazi kompoziciju, za koju je slobodna odredjena pruga i usmjri lokomotivu na odgovarajuce polje
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
						
						for (Kompozicija k : susjed.dolazneKompozicije) // prodji kroz dolazne dolazneKompozicije susjeda i uzmi najmanju brzinu
						{
							if (k.prethodnaStanica.nazivStanice == nazivStanice && k.brzinaKretanja > min)
							{
								min = k.brzinaKretanja;
							}
						}
						kompozicija.uskladjenaBrzina = kompozicija.brzinaKretanja;
						kompozicija.brzinaKretanja = min;
					}

					for (Lokomotiva l : kompozicija.lokomotive)
					{
						l.trenutneKoordinate = usmjeriKompoziciju(kompozicija)[0];
						l.prethodneKoordinate = new Koordinate(usmjeriKompoziciju(kompozicija)[0]);
					}

					for (Vagon v : kompozicija.vagoni)
					{
						v.prethodneKoordinate = new Koordinate(kompozicija.lokomotive.get(kompozicija.lokomotive.size() - 1).prethodneKoordinate);
						v.trenutneKoordinate = new Koordinate(kompozicija.lokomotive.get(kompozicija.lokomotive.size() - 1).trenutneKoordinate);
					}
					
					if(kompozicija.polazak.koordinate.contains(kompozicija.lokomotive.get(0).trenutneKoordinate)) //ako je tek pocelo kretanja, pocinje mjeriti vrijeme
						kompozicija.vrijemeKretanja = System.currentTimeMillis();
					
					kompozicija.lokomotive.get(0).trenutneKoordinate = usmjeriKompoziciju(kompozicija)[1]; //prva lokomotiva izlazi iz stanice
					kompozicija.istorijaKretanja.add(new Koordinate(kompozicija.lokomotive.get(0).trenutneKoordinate));
					synchronized (this)
					{
						matricaSusjedstva[nazivStanice - 'A'][susjed.nazivStanice - 'A']++;
					}
					susjed.dolazneKompozicije.add(kompozicija);
					synchronized (GUI.frame)
					{
						GUI.guiMapa[kompozicija.lokomotive.get(0).trenutneKoordinate.i][kompozicija.lokomotive.get(0).trenutneKoordinate.j].add(new JLabel(new ImageIcon("SLIKE/lokomotiva.png")));
						((JLabel) GUI.guiMapa[kompozicija.lokomotive.get(0).trenutneKoordinate.i][kompozicija.lokomotive.get(0).trenutneKoordinate.j].getComponent(0)).setName(kompozicija.brzinaKretanja + "k");
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
				Thread.sleep(brzinaRasporedjivanja);
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
		ZeljeznickaStanica susjed = komp.odrediSusjeda(); // susjed i naziv stanice dovoljan za provjeru matrice

		if (matricaSusjedstva[susjed.nazivStanice - 'A'][(nazivStanice - 'A')] == 0)
		{
			Koordinate kord0 = usmjeriKompoziciju(komp)[1];
			Koordinate kord1 = usmjeriKompoziciju(komp)[2];

			if (GUI.guiMapa[kord0.i][kord0.j].getComponents().length == 0
					&& GUI.guiMapa[kord1.i][kord1.j].getComponents().length == 0)
				return true;
		}
		return false;
	}

	synchronized Koordinate[] usmjeriKompoziciju(Kompozicija komp)
	{
		char susjednaStanica = komp.odrediSusjeda().nazivStanice;
		
		if (nazivStanice == 'A')
			return (new Koordinate[] { new Koordinate(27, 2), new Koordinate(26, 2), new Koordinate(25, 2) });

		else if (nazivStanice == 'B' && susjednaStanica == 'A')
			return (new Koordinate[] { new Koordinate(6, 6), new Koordinate(6, 5), new Koordinate(7, 5) }); 

		else if (nazivStanice == 'B' && susjednaStanica == 'C')
			return (new Koordinate[] { new Koordinate(6, 7), new Koordinate(6, 8), new Koordinate(6, 9) });

		else if (nazivStanice == 'C' && susjednaStanica == 'B')
			return (new Koordinate[] { new Koordinate(12, 19), new Koordinate(11, 19), new Koordinate(10, 19) });

		else if (nazivStanice == 'C' && susjednaStanica == 'D')
			return (new Koordinate[] { new Koordinate(12, 20), new Koordinate(12, 21), new Koordinate(12, 22) });

		else if (nazivStanice == 'C' && susjednaStanica == 'E')
			return (new Koordinate[] { new Koordinate(13, 20), new Koordinate(14, 20), new Koordinate(15, 20) });

		else if (nazivStanice == 'D')
			return (new Koordinate[] { new Koordinate(1, 26), new Koordinate(1, 25), new Koordinate(1, 24) });

		else if (nazivStanice == 'E')
			return (new Koordinate[] { new Koordinate(25, 26), new Koordinate(24, 26), new Koordinate(23, 26) });

		return null;
	}

}
