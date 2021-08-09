import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KreiranjeKompozicija extends Thread
{
	WatchService watcher;
	Path dir;
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
			dir = Paths.get("kompozicije");
			dir.register(watcher, ENTRY_MODIFY);
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
					Path fileName = ev.context();
					if (fileName.toString().trim().endsWith(".txt"))
					{
						List<String>content = Files.readAllLines(dir.resolve(fileName));
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
				Kompozicija tmp = new Kompozicija(brLokomotiva,  brVagona,  raspored,  brzina,  GUI.stanice.get(pocetnaStanica-'A'),  GUI.stanice.get(krajnjaStanica-'A'));
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
