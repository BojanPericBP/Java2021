import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")

public class KreiranjeKompozicija extends Thread
{
	Path dir;
	WatchService watcher;
	
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
			dir = Paths.get("config/compositions");
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
			while (GUI.isAlive)
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
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					List<String>content = Files.readAllLines(dir.resolve(fileName));
					kreiraj(content.get(2));
				}
				boolean valid = key.reset();
				if (!valid)
					break;
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	
	@Override
	public void run()
	{
		pokreni();
	}
	
	private void kreiraj(String podaciOKompoziciji)
	{
		try
		{
			String[] podaci = podaciOKompoziciji.split(" ");

			String rasporedL = podaci[0];
			String rasporedV = podaci[1];
			long brzina = Long.parseLong(podaci[2]);
			String[] linijaString = podaci[3].split("-");
			ArrayList<TrainStation> _linija = new ArrayList<>();
			for(String s : linijaString)
			{
				for(int i=0; i< GUI.trainStations.size();++i)
				if(GUI.trainStations.get(i).nameStation == s.charAt(0))
				{
					_linija.add(GUI.trainStations.get(i));
					break;
				}
			}

			Train tmp = new Train(rasporedL, rasporedV, brzina,_linija);
			GUI.trainStations.get(_linija.get(0).nameStation-'A').outgoingtrains.add(tmp);		
			
		}
		catch (Exception e)
		{
			Logger.getLogger(KreiranjeKompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	
	
}
