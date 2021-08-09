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
	
	static 
	{
		try
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).addHandler(new FileHandler("Error logs/KreiranjeKompozicija.log"));
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
					@SuppressWarnings("unchecked")//TODO sta je ovo brteyy
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
			String[] podaci=podaciOKompoziciji.split(" ");

			String rasporedL = podaci[0];
			String rasporedV = podaci[1];
			long brzina = Long.parseLong(podaci[2]);
			char pocetnaStanica = podaci[3].charAt(0);
			char krajnjaStanica = podaci[4].charAt(0);
			
			try {
				Kompozicija tmp = new Kompozicija(rasporedL, rasporedV, brzina, GUI.stanice.get(pocetnaStanica-'A'), GUI.stanice.get(krajnjaStanica-'A'));
				GUI.stanice.get(pocetnaStanica-'A').redUStanici.add(tmp);				
			}
			catch (Exception e) {
				Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
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
