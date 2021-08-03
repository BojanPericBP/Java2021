import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Kompozicija extends Thread /* implements Serializable */ {

	private static final long serialVersionUID = 1L;
	public String path;
	final int maxLokomotiva = 2;
	final int maxVagona = 5;

	ArrayList<Lokomotiva> lokomotive;
	ArrayList<Vagon> vagoni;
	long brzinaKretanja;
	ZeljeznickaStanica odrediste; // odredisna stanica na koju kompozicija treba da stigne
	ZeljeznickaStanica polazak;
	// ZeljeznickaStanica sledecaStanica; // sledeca stanica prema kojoj kompozicija
	// ide a ne iz koje je krenula npr A-B-C sledecaStanica = B
	ZeljeznickaStanica prethodnaStanica;
	Object lock = new Object();

	public Kompozicija(int _brLokomotiva, int _brVagona, String _raspored, long _brzina, ZeljeznickaStanica _polazak,
			ZeljeznickaStanica _odrediste, String _path) throws Exception {
		if (_brLokomotiva > maxLokomotiva || _brLokomotiva < 1 || _brVagona > maxVagona)
			throw new Exception("Kompozicija nije validna!");
		brzinaKretanja = _brzina;
		polazak = _polazak;
		odrediste = _odrediste;
		vagoni = new ArrayList<>(_brVagona);
		lokomotive = new ArrayList<>(_brLokomotiva);
		prethodnaStanica = polazak;

		path = _path;

		kreirajKompoziciju(_raspored);
	}

	@Override
	public void run() {

		while (!prethodnaStanica.equals(odrediste)) // ako ne bude radilo provjeriti reference i uraditi sa
													// koordinatama
		{
				ZeljeznickaStanica susjed = odrediSusjeda();
				if (kretanjeKompozicije()) // kompozicija usla u stanicu
				{

					ZeljeznickaStanica.matricaSusjedstva[prethodnaStanica.nazivStanice - 'A'][susjed.nazivStanice- 'A']--;
					prethodnaStanica = susjed; // npr prethodna je A ovo vrati B

					susjed = odrediSusjeda(); // prethodna je sada B pa ovo vrati C

					if (odrediste.koordinate.contains(lokomotive.get(0).trKoo)) // da li je u odredisnoj stanici
					{
						// TODO serijalizacija
						System.out.println("USAO U STANICU");
					} 
					else // da li je u bilo kojoj stanici koja nije odredisna
					{
						try 
						{
							synchronized (this) 
							{
								System.out.println("Dodao kompoziciju u "+prethodnaStanica.nazivStanice);
								prethodnaStanica.redUStanici.add(this);
								wait();
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
//						System.out.println("Dodao kompoziciju u "+prethodnaStanica.nazivStanice);
//						prethodnaStanica.redUStanici.add(this);
					}
				}
				synchronized (GUI.guiMapa) 
				{
					System.out.println("Gui je osvjezen");
					SwingUtilities.updateComponentTreeUI(GUI.frame);
				}
			try 
			{
				sleep(500); // TODO vratiti na brzinu kretanja
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

	synchronized ZeljeznickaStanica odrediSusjeda() { // vrati referencu susjedne stanice kak
		// kojoj se
		// kompozicija krece

		if (prethodnaStanica.nazivStanice == 'A') {
			return GUI.stanice.get(1);
		} else if (prethodnaStanica.nazivStanice == 'B') {
			if (odrediste == GUI.stanice.get(0))
				return GUI.stanice.get(0);
			else
				return GUI.stanice.get(2);
		} else if (prethodnaStanica.nazivStanice == 'D' || prethodnaStanica.nazivStanice == 'E') {
			return GUI.stanice.get(2);
		} else {
			if (odrediste == GUI.stanice.get(0) || odrediste == GUI.stanice.get(1))
				return GUI.stanice.get(1);
			else if (odrediste == GUI.stanice.get(2))
				return GUI.stanice.get(2);
			else if (odrediste == GUI.stanice.get(3))
				return GUI.stanice.get(3);
			else
				return GUI.stanice.get(4);
		}
	}

	private void kreirajKompoziciju(String raspored) {
		String[] niz = raspored.split(";");

		for (String string : niz) {

			if ('V' == string.charAt(0)) // vagoni
			{
				if ('P' == string.charAt(1))// putnicki vagoni
				{
					if ('S' == string.charAt(2))// spavaci
					{
						vagoni.add(new PutnickiVagonSpavaci());
					}

					else {// restoran
						vagoni.add(new PutnickiVagonRestoran());
					}
				}

				else if ('T' == string.charAt(1)) {// teretni
					vagoni.add(new TeretniVagon());
				}

				else {// posebne namjene
					vagoni.add(new Vagon(true));
				}

			} else { // lokomotive
				if ('P' == string.charAt(1))// putnicka lokomotiva
				{
					lokomotive.add(new Lokomotiva("putnicka"));
				} else if ('T' == string.charAt(1))// teretna lokomotiva
				{
					lokomotive.add(new Lokomotiva("teretna"));
				} else if ('U' == string.charAt(1)) {// univerzalna lokomotiva
					lokomotive.add(new Lokomotiva("univerzalna"));
				}

				else if (string.equals("LM"))// manevarska lokomotiva
				{
					lokomotive.add(new Lokomotiva("manevarska"));
				}
			}
		}
	}

	synchronized void udjiUStanicu() {
		for (int i = 1; i < lokomotive.size(); i++)
			while (lokomotive.get(i).move())
				;

		for (Vagon var : vagoni)
			while (var.move())
				;
	}

	synchronized boolean kretanjeKompozicije() // true kad udje u stanicu
	{
		for (Lokomotiva lok : lokomotive)
			if (!lok.move()) {
				udjiUStanicu();
				return true; // vagon uso u stanicu
			}

		for (Vagon vagon : vagoni)
			if (!vagon.move()) {
				udjiUStanicu();
				break;
			}

		return false;
	}
}
