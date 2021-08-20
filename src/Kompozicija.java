import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import javax.swing.*;

public class Kompozicija extends Thread implements Serializable  
{
	private static final long serialVersionUID = 1L;
	private static int brojacKompozicija = 0;
	final static int MAX_BROJ_LOKOMOTIVA = 5;
	final static int MAX_BR_VAGONA = 5;
	int idKompozicije;
	public long vrijemeKretanja;
	ArrayList<Koordinate> istorijaKretanja;
	String usputneStanice;
	ArrayList<Lokomotiva> lokomotive;
	ArrayList<Vagon> vagoni;
	long brzinaKretanja;
	long uskladjenaBrzina;
	ZeljeznickaStanica odrediste;
	ZeljeznickaStanica polazak;
	ZeljeznickaStanica prethodnaStanica;
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Kompozicija.log");
			Logger.getLogger(Kompozicija.class.getName()).addHandler(handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Kompozicija( long brzinaArg, String rasporedArg, int brLokomotivaArg, int brVagonaArg, ZeljeznickaStanica polazakArg, ZeljeznickaStanica odredisteArg) throws Exception 
	{
		idKompozicije = brojacKompozicija++;
		if (brLokomotivaArg > MAX_BROJ_LOKOMOTIVA || brLokomotivaArg < 1 || brVagonaArg > MAX_BR_VAGONA || brVagonaArg < 0)
			throw new Exception("Kompozicija nije validna!");

		brzinaKretanja = brzinaArg <= 500 ? 500: brzinaArg;
		uskladjenaBrzina = brzinaKretanja;
		polazak = polazakArg;
		prethodnaStanica = polazak;
		vagoni = new ArrayList<>(brVagonaArg);
		odrediste = odredisteArg;
		lokomotive = new ArrayList<>(brLokomotivaArg);
		
		istorijaKretanja = new ArrayList<Koordinate>();
		usputneStanice = polazakArg.nazivStanice+" ";
		kreirajKompoziciju(rasporedArg);
	}
	
	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && !prethodnaStanica.equals(odrediste)) 
		{
				ZeljeznickaStanica susjed = odrediSusjeda();
				try 
				{
					sleep(brzinaKretanja);
				} 
				catch (Exception e) 
				{
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
				
				if (kretanjeKompozicije()) // kompozicija usla u stanicu kad udje u if
				{

					ZeljeznickaStanica.matricaSusjedstva[prethodnaStanica.nazivStanice - 'A'][susjed.nazivStanice- 'A']--;
					prethodnaStanica = susjed; 

					susjed = odrediSusjeda();
					usputneStanice+=prethodnaStanica.nazivStanice+" ";
					
					if (odrediste.koordinate.contains(lokomotive.get(0).trenutneKoordinate)) // u odredisnoj stanici
					{
						vrijemeKretanja = System.currentTimeMillis() - vrijemeKretanja;
						vrijemeKretanja /= 1000;
						try 
						{
							ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serijalizacija/kompozicija"+idKompozicije+".ser"));
							oos.writeObject(this);
							oos.close();
						}
						catch (Exception e) {
							Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
						}
						
					} 
					else // u bilo kojoj stanici (koja nije odredisna)
					{
						try 
						{
							synchronized (this) 
							{
								prethodnaStanica.redUStanici.add(this);
								brzinaKretanja = uskladjenaBrzina;
								wait();
							}
						} 
						catch (Exception e)
						{
							Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
						}
					}
				}
				synchronized (GUI.guiMapa) 
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
				
				synchronized(this)
				{
					radSaRampom();				
				}
				/*
				try 
				{
					sleep(brzinaKretanja);
				} 
				catch (Exception e) 
				{
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}*/
		}
		
	}

	synchronized ZeljeznickaStanica odrediSusjeda() // vrati referencu susjedne stanice ka kojoj se kompozicija krece
	{ 
		if (prethodnaStanica.nazivStanice == 'A') 
		{
			return GUI.stanice.get(1);
		} 
		else if (prethodnaStanica.nazivStanice == 'B') 
		{
			if (odrediste == GUI.stanice.get(0))
				return GUI.stanice.get(0);
			else
				return GUI.stanice.get(2);
		} 
		else if (prethodnaStanica.nazivStanice == 'D' || prethodnaStanica.nazivStanice == 'E') 
		{
			return GUI.stanice.get(2);
		} 
		else 
		{
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

	private void kreirajKompoziciju(String raspored) throws Exception
	{
		String[] niz = raspored.split(";");
		List<String> kompozicija = Arrays.asList(niz);
		
		if(kompozicija.get(0).startsWith("V")) //kompozicija ne moze pocinjat vagonom, bez obzira na duzinu
			throw new Exception("Neispravan format kompozicije");
		
		if(kompozicija.size() == 1 && !(kompozicija.get(0).startsWith("L"))) //ako kompozicija ima samo 1 element, on mora biti lokomotiva (moze bilo koja)
		{
			throw new Exception("Neispravan format kompozicije");
		}
		
		else if(kompozicija.size() > 1 && kompozicija.contains("LM") && (kompozicija.contains("LP") || kompozicija.contains("LT") /*|| kompozicija.contains("LU")*/
				|| kompozicija.contains("VPS") || kompozicija.contains("VPR") || kompozicija.contains("VT")) )
		{
			//manevarska lokomotiva moze samo sa drugim manevarskim lokomotivama i univerzalnom i vagonom posebne namjene, ne moze sa ostalim lokomotivama i ostalim vagonima
			throw new Exception("Neispravan format kompozicije");
		}
		
		else if(kompozicija.contains("LP") && (kompozicija.contains("LT") || kompozicija.contains("VT") || kompozicija.contains("VN"))) 
		{	
			//putnicka lokomotiva moze sa:    drugim putnickim lokomotivama, univerzalnom lokomotivom, putnickim vagonima
			//putnicka lokomotiva ne moze sa: manevarskom lokomotivom, teretnom lokomotivom, teretnim vagonima, vagonima posebne namjene 
			throw new Exception("Neispravan format kompozicije");
		}
		else if(kompozicija.contains("LT") && (kompozicija.contains("LP") || kompozicija.contains("VPS") || kompozicija.contains("VPR") || kompozicija.contains("VN")) )
		{
			// teretna lokomotiva moze sa:    drugim teretnim lokomotivama, teretnim vagonima, univerzalnom lokomotivom
			// teretna lokomotiva ne moze sa: manevarskom lokomotivom, putnickom lokomotivom, putnickim vagonima, vagonima posebne namjene
			throw new Exception("Neispravan format kompozicije");
		}
		
		//Vagoni posebne namjene samo sa kompozicijama koje imaju samo univerzalne lokomotive
		
		int br = 0;
		boolean flag=true;
		for (int i = 0; i < niz.length && flag; i++) 
		{
			if(!niz[i].startsWith("L")) 
			{
				flag = false;
				br=i+1;
			}
		}
		for (int i = br; i < niz.length && br>0; i++) {
			if(niz[i].startsWith("L")) throw new Exception("Neispravan format kompozicije");
		}
		
		
		for (String string : niz) 
		{
			if ('V' == string.charAt(0)) // vagoni
			{
				if ('P' == string.charAt(1))// putnicki vagoni
				{
					if ('S' == string.charAt(2))// spavaci
					{
						vagoni.add(new PutnickiVagonSpavaci());
					}

					else if ('R' == string.charAt(2))// restoran
					{
						vagoni.add(new PutnickiVagonRestoran());
					}
					else throw new Exception();
				}
				else if ('T' == string.charAt(1)) {// teretni
					vagoni.add(new TeretniVagon());
				}

				else if('N' == string.charAt(1)){// posebne namjene
					vagoni.add(new Vagon(true));
				}
				else throw new Exception();

			} 
			else if('L' == string.charAt(0)) // lokomotive
			{ 
				if ('P' == string.charAt(1))// putnicka lokomotiva
				{
					lokomotive.add(new Lokomotiva("putnicka"));
				} 
				else if ('T' == string.charAt(1))// teretna lokomotiva
				{
					lokomotive.add(new Lokomotiva("teretna"));
				} 
				else if ('U' == string.charAt(1)) // univerzalna lokomotiva
				{
					lokomotive.add(new Lokomotiva("univerzalna"));
				}
				else if (string.equals("LM"))// manevarska lokomotiva
				{
					lokomotive.add(new Lokomotiva("manevarska"));
				}
				else throw new Exception();
			}
			else throw new Exception();
		}
	}

	synchronized void udjiUStanicu() 
	{
		int granicaLOK=1;
		int granicaVAG=0;
		while(granicaLOK!=lokomotive.size() || granicaVAG!=vagoni.size()) 
		{
			synchronized(this)
			{
				radSaRampom();				
			}
			for (int i = granicaLOK; i < lokomotive.size(); i++)
			{
				if(!lokomotive.get(i).move()) granicaLOK=i+1;
				synchronized(GUI.frame)
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
			}
			
			for (int i = granicaVAG; i < vagoni.size(); i++)
			{
				if(!vagoni.get(i).move()) granicaVAG=i+1;
				synchronized(GUI.frame)
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
			}	
			try { Thread.sleep(brzinaKretanja); } catch (InterruptedException e) { Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString()); }
		}
	}

	
	synchronized void radSaRampom()
	{
		boolean trebaDignuti = true;
		int pocetakA = 19, krajA = 23;
		for(int i=0; i<lokomotive.size(); i++) //trazi bar jednu elektricnu
		{
			if(lokomotive.get(i).pogon == 'E') 
			{	
				pocetakA = 18; krajA = 24;
				break;
			}
		}	
		for (int i = pocetakA; i < krajA; i++) 
		{
			if(GUI.guiMapa[i][2].getComponents().length == 1)
			{
				if( (GUI.guiMapa[i][2].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[i][2].getComponents()[0])).getName(); //Prelaz na putu A
					if(s != null && s.contains("k"))
						trebaDignuti = false;
				}
			}
		}
		
		synchronized(GUI.guiMapa)
		{
			obojiRampu(trebaDignuti,20,2,21,2);
		}
		
		//========================================================================================================================0
		trebaDignuti = true;
		int pocetakB = 12, krajB = 16;
		for(int i=0; i<lokomotive.size(); i++) //trazi bar jednu elektricnu
		{
			if(lokomotive.get(i).pogon == 'E') 
			{	
				pocetakB = 11; krajB = 17;
				break;
			}
		}
		for (int j = pocetakB; j < krajB; j++) {
			if(GUI.guiMapa[6][j].getComponents().length == 1)
			{
				if( (GUI.guiMapa[6][j].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[6][j].getComponents()[0])).getName(); //Prelaz na putu B
					if(s != null && s.contains("k"))
						trebaDignuti = false;
				}
			}
		}
		
		synchronized(GUI.guiMapa)
		{
			obojiRampu(trebaDignuti,6,13,6,14);
			
		}
		//========================================================================================================================0
		trebaDignuti = true;
		int pocetakC = 19, krajC = 23;
		for(int i=0; i<lokomotive.size(); i++) //trazi bar jednu elektricnu
		{
			if(lokomotive.get(i).pogon == 'E') 
			{	
				pocetakC = 18; krajC = 24;
				break;
			}
		}
		for (int i = pocetakC; i < krajC; i++) 
		{
			if(GUI.guiMapa[i][26].getComponents().length == 1)
			{
				if( (GUI.guiMapa[i][26].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[i][26].getComponents()[0])).getName(); //Prelaz na putu C
					if(s != null && s.contains("k"))
						trebaDignuti = false;
				}
			}
		}
		synchronized(GUI.guiMapa)
		{
			obojiRampu(trebaDignuti,20,26,21,26);
		}
		
	}
	
	synchronized private void obojiRampu(boolean trebaPodignuti,int i1, int j1, int i2,int j2)
	{
		if(trebaPodignuti)
		{
			GUI.guiMapa[i1][j1].setBackground(GUI.DIGNUTA_RAMPA);
			GUI.guiMapa[i2][j2].setBackground(GUI.DIGNUTA_RAMPA);
		}
		else 
		{
			GUI.guiMapa[i1][j1].setBackground(GUI.SPUSTENA_RAMPA);
			GUI.guiMapa[i2][j2].setBackground(GUI.SPUSTENA_RAMPA);
		}
	}
	
	
	
	synchronized boolean kretanjeKompozicije() // true kad udje u stanicu
	{
		for (int i = 0; i < lokomotive.size(); i++) 
		{
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			if (prethodnaStanica.koordinate.contains(lokomotive.get(i).trenutneKoordinate) && !prethodnaStanica.koordinate.contains(lokomotive.get(i-1).prethodneKoordinate))  // U prvom koraku se nece ispitivati drgui uslov
			{
				lokomotive.get(i).trenutneKoordinate = new Koordinate(lokomotive.get(i-1).prethodneKoordinate);
				GUI.guiMapa[lokomotive.get(i).trenutneKoordinate.i][lokomotive.get(i).trenutneKoordinate.j].add(new JLabel(new ImageIcon("SLIKE/lokomotiva.png")));
				((JLabel)GUI.guiMapa[lokomotive.get(i).trenutneKoordinate.i][lokomotive.get(i).trenutneKoordinate.j].getComponent(0)).setName(brzinaKretanja+"k");
				
			}
			
			else if(i!=0 && GUI.guiMapa[lokomotive.get(i - 1).prethodneKoordinate.i][lokomotive.get(i - 1).prethodneKoordinate.j].getComponents().length != 0) 
				continue;
			else 
			{	
					boolean flag = lokomotive.get(i).move();
					if(i == 0)
					{
						istorijaKretanja.add(new Koordinate(lokomotive.get(0).trenutneKoordinate));
					}
				 
					synchronized(GUI.frame)
					{				
						GUI.frame.invalidate();
						GUI.frame.validate();
						GUI.frame.repaint();
					}
					
					if(!flag)
					{
						udjiUStanicu();
						return true;
					}
			}
		}
		
		for (int i = 0; i < vagoni.size(); i++) 
		{
			
			//prvi vagon, u stanici je  i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			if(i==0 && prethodnaStanica.koordinate.contains(vagoni.get(i).trenutneKoordinate) && !prethodnaStanica.koordinate.contains(lokomotive.get(lokomotive.size()-1).prethodneKoordinate))
			{
				vagoni.get(i).trenutneKoordinate = new Koordinate(lokomotive.get(lokomotive.size()-1).prethodneKoordinate);
				GUI.guiMapa[vagoni.get(i).trenutneKoordinate.i][vagoni.get(i).trenutneKoordinate.j].add(new JLabel(new ImageIcon("SLIKE/vagon.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trenutneKoordinate.i][vagoni.get(i).trenutneKoordinate.j].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();					
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			//prvi vagon, nije u stanici i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			else if(i==0 && !prethodnaStanica.koordinate.contains(vagoni.get(i).trenutneKoordinate) && !prethodnaStanica.koordinate.contains(lokomotive.get(lokomotive.size()-1).prethodneKoordinate))
			{
				if(!vagoni.get(i).move()) 
				{
					synchronized(GUI.frame)
					{
						GUI.frame.invalidate();
						GUI.frame.validate();
						GUI.frame.repaint();				
					}

					udjiUStanicu();
					return true; // vagon uso u stanicu
				}
			}
			
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			else if (i>0 && prethodnaStanica.koordinate.contains(vagoni.get(i).trenutneKoordinate) && !prethodnaStanica.koordinate.contains(vagoni.get(i-1).prethodneKoordinate))
			{
				vagoni.get(i).trenutneKoordinate = new Koordinate(vagoni.get(i-1).prethodneKoordinate);
				GUI.guiMapa[vagoni.get(i).trenutneKoordinate.i][vagoni.get(i).trenutneKoordinate.j].add(new JLabel(new ImageIcon("SLIKE/vagon.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trenutneKoordinate.i][vagoni.get(i).trenutneKoordinate.j].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();					
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			else if (i > 0 && GUI.guiMapa[vagoni.get(i - 1).prethodneKoordinate.i][vagoni.get(i - 1).prethodneKoordinate.j].getComponents().length == 0  && !vagoni.get(i).move())
			{
				synchronized(GUI.frame)
				{
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();					
				}

				udjiUStanicu();
				return true; // vagon uso u stanicu
			}
		}
		return false;
	}
	
}



