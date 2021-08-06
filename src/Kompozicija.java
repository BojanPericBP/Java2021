import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Kompozicija extends Thread implements Serializable  {

	private static final long serialVersionUID = 1L;
	final int maxLokomotiva = 5;
	final int maxVagona = 5;

	ArrayList<Lokomotiva> lokomotive;
	ArrayList<Vagon> vagoni;
	long brzinaKretanja;
	long tmpBrzina;
	ZeljeznickaStanica odrediste; // odredisna stanica na koju kompozicija treba da stigne
	ZeljeznickaStanica polazak;
	// ZeljeznickaStanica sledecaStanica; // sledeca stanica prema kojoj kompozicija
	// ide a ne iz koje je krenula npr A-B-C sledecaStanica = B
	ZeljeznickaStanica prethodnaStanica;
	Object lock = new Object();

	public Kompozicija(int _brLokomotiva, int _brVagona, String _raspored, long _brzina, ZeljeznickaStanica _polazak,
			ZeljeznickaStanica _odrediste) throws Exception 
	{
		if (_brLokomotiva > maxLokomotiva || _brLokomotiva < 1 || _brVagona > maxVagona)
			throw new Exception("Kompozicija nije validna!");
		brzinaKretanja = _brzina;
		polazak = _polazak;
		odrediste = _odrediste;
		vagoni = new ArrayList<>(_brVagona);
		lokomotive = new ArrayList<>(_brLokomotiva);
		prethodnaStanica = polazak;
		tmpBrzina = brzinaKretanja;
		
		kreirajKompoziciju(_raspored);
	}

	@Override
	public void run() { // TODO usaglasavanje brzina vozova

		while (!prethodnaStanica.equals(odrediste)) // ako ne bude radilo provjeriti reference i uraditi sa
													// koordinatama
		{
			synchronized(this)
			{
				radSaRampom();				
			}
			
				ZeljeznickaStanica susjed = odrediSusjeda();
				if (kretanjeKompozicije()) // kompozicija usla u stanicu
				{

					ZeljeznickaStanica.matricaSusjedstva[prethodnaStanica.nazivStanice - 'A'][susjed.nazivStanice- 'A']--;
					prethodnaStanica = susjed; // npr prethodna je A ovo vrati B

					susjed = odrediSusjeda(); // prethodna je sada B pa ovo vrati C

					if (odrediste.koordinate.contains(lokomotive.get(0).trKoo)) // da li je u odredisnoj stanici
					{
						// TODO serijalizacija
						System.out.println("USAO U STANICU "+odrediste.nazivStanice);
					} 
					else // da li je u bilo kojoj stanici koja nije odredisna
					{
						try 
						{
							synchronized (this) 
							{
								prethodnaStanica.redUStanici.add(this);
								brzinaKretanja = tmpBrzina;
								wait();
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				synchronized (GUI.guiMapa) 
				{
					//SwingUtilities.updateComponentTreeUI(GUI.frame);
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
				
				synchronized(this)
				{
					radSaRampom();				
				}
			try 
			{
				sleep(brzinaKretanja);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}

	synchronized ZeljeznickaStanica odrediSusjeda() { // vrati referencu susjedne stanice ka kojoj se kompozicija krece

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

	private void kreirajKompoziciju(String raspored) 
	{
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

	synchronized void udjiUStanicu() 
	{
		int granicaLOK=1;
		int granicaVAG=0;
		while(granicaLOK!=lokomotive.size() || granicaVAG!=vagoni.size()) 
		{
			/////
			synchronized(this)
			{
				radSaRampom();				
			}
			for (int i = granicaLOK; i < lokomotive.size(); i++)
			{
				if(!lokomotive.get(i).move()) granicaLOK=i+1;
				synchronized(GUI.frame)
				{
					//SwingUtilities.updateComponentTreeUI(GUI.frame);	
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
				//try { Thread.sleep(brzinaKretanja); } catch (InterruptedException e) { e.printStackTrace(); }
			}
			
			for (int i = granicaVAG; i < vagoni.size(); i++)
			{
				if(!vagoni.get(i).move()) granicaVAG=i+1;
				synchronized(GUI.frame)
				{
					//SwingUtilities.updateComponentTreeUI(GUI.frame);
					GUI.frame.invalidate();
					GUI.frame.validate();
					GUI.frame.repaint();
				}
				//try { Thread.sleep(brzinaKretanja); } catch (InterruptedException e) { e.printStackTrace(); }
			}	
			try { Thread.sleep(brzinaKretanja); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}

	
	synchronized void radSaRampom()
	{
		//spustanje rampe
		boolean flag = true; //treba spustiti rampu
		for (int i = 18; i < 24; i++) {
			if(GUI.guiMapa[i][2].getComponents().length ==1)
			{
				if( (GUI.guiMapa[i][2].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[i][2].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
					flag = false;
				}
			}
		}
		
		synchronized(GUI.guiMapa)
		{
			obojiRampu(flag,20,2,21,2);
		}
		flag = true;
		for (int j = 11; j < 17; j++) {
			if(GUI.guiMapa[6][j].getComponents().length ==1)
			{
				if( (GUI.guiMapa[6][j].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[6][j].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
					flag = false;
				}
			}
		}
		
		synchronized(GUI.guiMapa)
		{
			obojiRampu(flag,6,13,6,14);
			
		}
		flag = true;
		for (int i = 18; i < 24; i++) {
			if(GUI.guiMapa[i][26].getComponents().length ==1)
			{
				if( (GUI.guiMapa[i][26].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.guiMapa[i][26].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
					flag = false;
				}
			}
		}
		synchronized(GUI.guiMapa)
		{
			obojiRampu(flag,20,26,21,26);
		}
		
	}
	
	synchronized private void obojiRampu(boolean flag,int i1, int j1, int i2,int j2)
	{
		if(flag)
		{
			GUI.guiMapa[i1][j1].setBackground(Color.orange);
			GUI.guiMapa[i2][j2].setBackground(Color.orange);
		}
		else {
			GUI.guiMapa[i1][j1].setBackground(Color.red);
			GUI.guiMapa[i2][j2].setBackground(Color.red);
		}
	}
	
	
	
	synchronized boolean kretanjeKompozicije() // true kad udje u stanicu
	{
		for (int i = 0; i < lokomotive.size(); i++) 
		{
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			if (prethodnaStanica.koordinate.contains(lokomotive.get(i).trKoo) && !prethodnaStanica.koordinate.contains(lokomotive.get(i-1).preKoo))  // U prvom koraku se nece ispitivati drgui uslov
			{
				lokomotive.get(i).trKoo = new Koordinate(lokomotive.get(i-1).preKoo);
				GUI.guiMapa[lokomotive.get(i).trKoo.i][lokomotive.get(i).trKoo.j].add(new JLabel(new ImageIcon("lokomotiva.png")));
				((JLabel)GUI.guiMapa[lokomotive.get(i).trKoo.i][lokomotive.get(i).trKoo.j].getComponent(0)).setName(brzinaKretanja+"k");
				
			}
			
			else if(i!=0 && GUI.guiMapa[lokomotive.get(i - 1).preKoo.i][lokomotive.get(i - 1).preKoo.j].getComponents().length != 0) 
				continue;
			else 
			{	
					boolean flag = lokomotive.get(i).move();
				 
					synchronized(GUI.frame)
					{
						//SwingUtilities.updateComponentTreeUI(GUI.frame);					
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
			if(i==0 && prethodnaStanica.koordinate.contains(vagoni.get(i).trKoo) && !prethodnaStanica.koordinate.contains(lokomotive.get(lokomotive.size()-1).preKoo))
			{
				vagoni.get(i).trKoo = new Koordinate(lokomotive.get(lokomotive.size()-1).preKoo);
				GUI.guiMapa[vagoni.get(i).trKoo.i][vagoni.get(i).trKoo.j].add(new JLabel(new ImageIcon("vagon.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trKoo.i][vagoni.get(i).trKoo.j].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					SwingUtilities.updateComponentTreeUI(GUI.frame);					
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
			//prvi vagon, nije u stanici i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			else if(i==0 && !prethodnaStanica.koordinate.contains(vagoni.get(i).trKoo) && !prethodnaStanica.koordinate.contains(lokomotive.get(lokomotive.size()-1).preKoo))
			{
				//vagoni.get(i).trKoo = new Koordinate(lokomotive.get(lokomotive.size()-1).preKoo);
				if(!vagoni.get(i).move()) 
				{
					synchronized(GUI.frame)
					{
						SwingUtilities.updateComponentTreeUI(GUI.frame);					
					}

					udjiUStanicu();
					return true; // vagon uso u stanicu
				}
			}
			
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			else if (i>0 && prethodnaStanica.koordinate.contains(vagoni.get(i).trKoo) && !prethodnaStanica.koordinate.contains(vagoni.get(i-1).preKoo))
			{
				vagoni.get(i).trKoo = new Koordinate(vagoni.get(i-1).preKoo);
				GUI.guiMapa[vagoni.get(i).trKoo.i][vagoni.get(i).trKoo.j].add(new JLabel(new ImageIcon("vagon.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trKoo.i][vagoni.get(i).trKoo.j].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					SwingUtilities.updateComponentTreeUI(GUI.frame);					
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
			else if (i > 0 && GUI.guiMapa[vagoni.get(i - 1).preKoo.i][vagoni.get(i - 1).preKoo.j].getComponents().length == 0  && !vagoni.get(i).move())
			{
				synchronized(GUI.frame)
				{
					SwingUtilities.updateComponentTreeUI(GUI.frame);					
				}

				udjiUStanicu();
				return true; // vagon uso u stanicu
			}
		}
		return false;
	}
	
}



