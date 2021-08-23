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

public class CreateTrains extends Thread
{
	Path dir;
	WatchService watcher;
	
	static 
	{
		try
		{
			Logger.getLogger(CreateTrains.class.getName()).addHandler(new FileHandler("logs/CreateComposition.log"));
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public CreateTrains()
	{
		try
		{
			watcher = FileSystems.getDefault().newWatchService();
			dir = Paths.get("config/compositions");
			dir.register(watcher, ENTRY_MODIFY);
		}
		catch (IOException e)
		{
			Logger.getLogger(CreateTrains.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	private void tempFunc()
	{
		try
		{
			while (Main.isAlive)
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
					create(content.get(2));
				}
				boolean valid = key.reset();
				if (!valid)
					break;
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(CreateTrains.class.getName()).log(Level.WARNING, ex.fillInStackTrace().toString());
		}
	}
	@Override
	public void run()
	{
		tempFunc();
	}
	
	private void create(String podaciOKompoziciji) throws Exception
	{
		try
		{
			String[] data = podaciOKompoziciji.split(" ");

			String rasporedL = data[0];
			String rasporedV = data[1];
			
			long speed = Long.parseLong(data[2]);
			String[] tmpString = data[3].split("-");
			ArrayList<TrainStation> _linija = new ArrayList<>();
			
			for(String s : tmpString)
			{
				for(int i=0; i< Main.trainStations.size();++i)
				if(Main.trainStations.get(i).nameStation == s.charAt(0))
				{
					_linija.add(Main.trainStations.get(i));
					break;
				}
			}

			Train tmp = new Train(rasporedL, rasporedV, speed,_linija);
			Main.trainStations.get(_linija.get(0).nameStation-'A').outgoingtrains.add(tmp);		
			
		}
		catch (Exception e)
		{
			Logger.getLogger(CreateTrains.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
}
