import java.awt.Color;
import java.awt.Point;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Kompozicija extends Thread implements Serializable  
{
	static int count=0;
	int idKompozicije;
	private static final long serialVersionUID = 1L;

	public long vrijemeKretanja;
	ArrayList<Point> istorijaKretanja;
	
	ArrayList<Lokomotiva> lokomotive;
	ArrayList<Vagon> vagoni;
	long brzinaKretanja;
	long tmpBrzina;
	ArrayList<ZeljeznickaStanica> linija;
	ZeljeznickaStanica prethodnaStanica;
	
	static 
	{
		try
		{
			Logger.getLogger(Kompozicija.class.getName()).addHandler(new FileHandler("Error logs/Kompozicija.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Kompozicija(String _rasporedL, String rasporedV, long _brzina, ArrayList<ZeljeznickaStanica> _linija) throws Exception 
	{
		idKompozicije = count++;
		linija = _linija;
		prethodnaStanica = linija.get(0);
		brzinaKretanja = _brzina <= 500 ? 500: _brzina;
		
		vagoni = new ArrayList<>();
		lokomotive = new ArrayList<>();
		tmpBrzina = brzinaKretanja;
		
		istorijaKretanja = new ArrayList<Point>();
		kreirajKompoziciju(_rasporedL,rasporedV);
	}
	
	@Override
	public void run()
	{
		
		while (GUI.simulacijaUToku && prethodnaStanica.nazivStanice != linija.get(linija.size()-1).nazivStanice) 
		{
			try {
				Thread.sleep(brzinaKretanja);
			} catch (InterruptedException e) {
				Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
			
			synchronized(this)
			{
				radSaRampom();				
			}
			
				//ZeljeznickaStanica susjed = odrediSusjeda();
			ZeljeznickaStanica susjed = odrediSusjeda();
			
			if (kretanjeKompozicije()) // kompozicija usla u stanicu
			{
				ZeljeznickaStanica.matricaSusjedstva[prethodnaStanica.nazivStanice - 'A'][susjed.nazivStanice- 'A']--;
				prethodnaStanica = susjed; // npr prethodna je A ovo vrati B

				susjed = odrediSusjeda(); // prethodna je sada B pa ovo vrati C
				if (prethodnaStanica.nazivStanice == linija.get(linija.size()-1).nazivStanice) // da li je u odredisnoj stanici
				{
					vrijemeKretanja = System.currentTimeMillis() - vrijemeKretanja;
					vrijemeKretanja /=1000;
					try {
							
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serijalizacija/kompozicija"+idKompozicije+".ser"));
					oos.writeObject(this);
					oos.close();
					}
					catch (Exception e) {
						Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
					}	
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
						Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
					}
				}
			}
			synchronized (GUI.guiMapa) 
			{
				GUI.refreshGui();
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
				Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
		}
		
	}

	synchronized ZeljeznickaStanica odrediSusjeda() { // vrati referencu susjedne stanice ka kojoj se kompozicija krece

		for(int i = 0; i<linija.size();++i)
			if(i < linija.size()-1 && linija.get(i).nazivStanice == prethodnaStanica.nazivStanice)
				return linija.get(i+1);
		return linija.get(linija.size()-1);
	}

	private void kreirajKompoziciju(String rasporedLokomotiva, String rasporedVagona) throws Exception
	{
		//lokomotive vagoni //null vagoni // lokomotive null
		Exception e = new Exception("Wrong train format");
		List<String> trains = new ArrayList<>();
		List<String> trainCar = new ArrayList<>();
		
		if(rasporedLokomotiva.equals("null"))
			throw e;
		
		if(!rasporedVagona.equals("null"))
		{
			trainCar = Arrays.asList(rasporedVagona.split(","));
		}
		
		trains = Arrays.asList(rasporedLokomotiva.split(","));
		
		if(trains.size() > 1 && trains.contains("ml"))
			throw e;
		else if(trains.contains("ml") && trainCar.size()>0)
			throw e;
		
		else if(trains.contains("pl") && (trains.contains("tl") || trainCar.contains("tv")))
			throw e;
		
		else if(trains.contains("tl") && (trains.contains("pl") || trainCar.contains("pvs") || trainCar.contains("pvr")))
			throw e;
		
		else if(trainCar.contains("tv") && (trainCar.contains("pvr") || trainCar.contains("pvs")) && !trains.contains("ul"))
			throw e;
		
		for(String s : trains)
		{
			switch (s) {
			case "tl": {
				lokomotive.add(new Lokomotiva("Teretna"));
				break;
			}
			case "pl":{
				lokomotive.add(new Lokomotiva("Putnicka"));
				break;
			}
			case "ul":{
				lokomotive.add(new Lokomotiva("Univerzalna"));
				break;
			}
			case "ml":{
				lokomotive.add(new Lokomotiva("Manevarska"));
				break;
			}
			}
		}
		
		for(String s : trainCar)
		{
			switch (s) {
			case "pvr": {
				vagoni.add(new PutnickiVagonRestoran());
				break;
			}
			case "pvs":{
				vagoni.add(new PutnickiVagonSpavaci());
				break;
			}
			case "vpn":{
				vagoni.add(new Vagon(false));
				break;
			}
			case "tv":{
				vagoni.add(new TeretniVagon());
				break;
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
			synchronized(this)
			{
				radSaRampom();				
			}
			for (int i = granicaLOK; i < lokomotive.size(); i++)
			{
				if(!lokomotive.get(i).move()) granicaLOK=i+1;
				synchronized(GUI.frame)
				{
					GUI.refreshGui();
				}
			}
			
			for (int i = granicaVAG; i < vagoni.size(); i++)
			{
				if(!vagoni.get(i).move()) granicaVAG=i+1;
				synchronized(GUI.frame)
				{
					GUI.refreshGui();
				}
			}	
			try { Thread.sleep(brzinaKretanja); } catch (InterruptedException e) { Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString()); }
		}
	}

	
	synchronized void radSaRampom()
	{
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
				lokomotive.get(i).trKoo = new Point(lokomotive.get(i-1).preKoo);
				GUI.guiMapa[lokomotive.get(i).trKoo.x][lokomotive.get(i).trKoo.y].add(new JLabel(new ImageIcon("resource/train.png")));
				((JLabel)GUI.guiMapa[lokomotive.get(i).trKoo.x][lokomotive.get(i).trKoo.y].getComponent(0)).setName(brzinaKretanja+"k");
			}
			
			else if(i!=0 && GUI.guiMapa[lokomotive.get(i - 1).preKoo.x][lokomotive.get(i - 1).preKoo.y].getComponents().length != 0) 
				continue;
			else 
			{	
					boolean flag = lokomotive.get(i).move();
					if(i == 0)
					{
						istorijaKretanja.add(new Point(lokomotive.get(0).trKoo));
					}
				 
					synchronized(GUI.frame)
					{				
						GUI.refreshGui();
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
				vagoni.get(i).trKoo = new Point(lokomotive.get(lokomotive.size()-1).preKoo);
				GUI.guiMapa[vagoni.get(i).trKoo.x][vagoni.get(i).trKoo.y].add(new JLabel(new ImageIcon("resource/traincar.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trKoo.x][vagoni.get(i).trKoo.y].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					GUI.refreshGui();				
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			//prvi vagon, nije u stanici i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			else if(i==0 && !prethodnaStanica.koordinate.contains(vagoni.get(i).trKoo) && !prethodnaStanica.koordinate.contains(lokomotive.get(lokomotive.size()-1).preKoo))
			{
				if(!vagoni.get(i).move()) 
				{
					synchronized(GUI.frame)
					{
						GUI.refreshGui();					
					}

					udjiUStanicu();
					return true; // vagon uso u stanicu
				}
			}
			
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			else if (i>0 && prethodnaStanica.koordinate.contains(vagoni.get(i).trKoo) && !prethodnaStanica.koordinate.contains(vagoni.get(i-1).preKoo))
			{
				vagoni.get(i).trKoo = new Point(vagoni.get(i-1).preKoo);
				GUI.guiMapa[vagoni.get(i).trKoo.x][vagoni.get(i).trKoo.y].add(new JLabel(new ImageIcon("resource/traincar.png")));
				((JLabel)GUI.guiMapa[vagoni.get(i).trKoo.x][vagoni.get(i).trKoo.y].getComponent(0)).setName(brzinaKretanja+"k");
				synchronized(GUI.frame)
				{
					GUI.refreshGui();					
				}
				try {
					Thread.sleep(brzinaKretanja);
				} catch (InterruptedException e) {
					Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			else if (i > 0 && GUI.guiMapa[vagoni.get(i - 1).preKoo.x][vagoni.get(i - 1).preKoo.y].getComponents().length == 0  && !vagoni.get(i).move())
			{
				synchronized(GUI.frame)
				{
					GUI.refreshGui();						
				}

				udjiUStanicu();
				return true; // vagon uso u stanicu
			}
		}
		return false;
	}
	
}



