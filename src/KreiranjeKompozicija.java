import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.logging.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;


public class KreiranjeKompozicija extends Thread
{
	WatchService watcher;
	Path putanjaFoldera;
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/KreiranjeKompozicija.log");
			Logger.getLogger(KreiranjeKompozicija.class.getName()).addHandler(handler);
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public KreiranjeKompozicija()
	{
		try
		{
			watcher = FileSystems.getDefault().newWatchService();
			putanjaFoldera = Paths.get("kompozicije");
			putanjaFoldera.register(watcher, ENTRY_MODIFY);
		}
		catch (IOException e)
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	public void pokreni()
	{
		try
		{
			while (GUI.simulacijaUToku)
			{
				WatchKey key;
				try
				{
					key = watcher.take();
				}
				catch (InterruptedException ex)
				{
					return;
				}

				for (WatchEvent<?> event : key.pollEvents())
				{
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path nazivDatoteke = ev.context();
					if (nazivDatoteke.toString().trim().endsWith(".txt"))
					{
						List<String>content = Files.readAllLines(putanjaFoldera.resolve(nazivDatoteke));
						kreiraj(content.get(2));
					}
				}
				boolean valid = key.reset();
				if (!valid) { break; }
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	
	private void kreiraj(String podaciOKompoziciji)
	{
		try
		{
			String[] podaci=podaciOKompoziciji.trim().split(",");
		
			int brLokomotiva = Integer.parseInt(podaci[0].trim());
			int brVagona = Integer.parseInt(podaci[1].trim());
			String raspored = podaci[2].trim();
			long brzina = Long.parseLong(podaci[3].trim());
			char pocetnaStanica = podaci[4].trim().charAt(0);
			char krajnjaStanica = podaci[5].trim().charAt(0);
			
			try 
			{
				Kompozicija tmp = new Kompozicija(brzina, raspored, brLokomotiva, brVagona, GUI.stanice.get(pocetnaStanica-'A'),  GUI.stanice.get(krajnjaStanica-'A'));
				GUI.stanice.get(pocetnaStanica-'A').redUStanici.add(tmp);				
			}
			catch (Exception ex) 
			{
				Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
			}
			
		}
		catch (Exception e)
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
		
	}
	
	@Override
	public void run()
	{
		pokreni();
	}
	
}
