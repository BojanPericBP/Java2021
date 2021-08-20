import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TrainStation extends Thread implements Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<Train> outgoingtrains;
	ArrayList<Train> incomingTrains;
	static int schedulerMatrix[][]; //matrica susjedstva[i][j] = 0; putanja od stanice i ka stanici j je slobodna, matrica je konzistentna
	char nameStation;
	ArrayList<Point> coordinates;
	
	static 
	{
		schedulerMatrix = new int[5][5];
		for (int i = 0; i < schedulerMatrix.length; i++)
		{
			for (int j = 0; j < schedulerMatrix.length; j++)
				schedulerMatrix[i][j] = 0;
		}
		
		try
		{
			Logger.getLogger(TrainStation.class.getName()).addHandler(new FileHandler("Error logs/ZeljeznickaStanica.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String toString() {

		return nameStation+"";
	}

	public TrainStation(char _nameStation, ArrayList<Point> _coordinates)
	{

		outgoingtrains = new ArrayList<>();
		incomingTrains = new ArrayList<>();

		nameStation = _nameStation;
		coordinates = _coordinates;
	}
	
	@Override
	public void run() // kompozicija je vec na prvom polju izvan pruge tj lokomotiva
	{

		while (GUI.isAlive)
		{
			Iterator<Train> iteratorKompozicija = outgoingtrains.iterator();

			while (iteratorKompozicija.hasNext()) // pronalazi kompoziciju za koju je slobodna odredjena pruga i
													// usmejru lokomotivu na odgovarajuce polje
			{
				Train kompozicija = iteratorKompozicija.next();
				boolean jeSlobodna = false;
				TrainStation susjed;
				Point kord0=null;
				Point kord1=null;
				synchronized (schedulerMatrix)
				{
					//jeSlobodna = prugaJeSlobodna(kompozicija);
					
					susjed = kompozicija.findNextStation();
					// susjed i naziv stanice dovoljan za provjeru matrice

					if (schedulerMatrix[susjed.nameStation - 'A'][(nameStation - 'A')] == 0)
					{
						kord0 = usmjeriKompoziciju(kompozicija)[1];
						kord1 = usmjeriKompoziciju(kompozicija)[2];

						if (GUI.trainMap[kord0.x][kord0.y].getComponents().length == 0
								&& GUI.trainMap[kord1.x][kord1.y].getComponents().length == 0)
							jeSlobodna = true;
					}
				}
			
				if (jeSlobodna)
				{
					if (schedulerMatrix[nameStation - 'A'][susjed.nameStation - 'A'] != 0)
					{
						long min = kompozicija.speed;
						// prodjikroz dolazne dolazneKompozicije susjeda i uzmi min brzinu
						for (Train k : susjed.incomingTrains)
						{
							if (k.prevTrainStation.nameStation == nameStation && k.speed > min)
								min = k.speed;
						}
						
						kompozicija.reducedSpeed = kompozicija.speed;
						kompozicija.speed = min;
					}

					for (int i = 0; i < kompozicija.locomotive.size(); ++i)
					{
						//kompozicija.locomotive.get(i).previousCoordinates = usmjeriKompoziciju(kompozicija)[0];
						kompozicija.locomotive.get(i).previousCoordinates = usmjeriKompoziciju(kompozicija)[0];
						kompozicija.locomotive.get(i).currentCoordinates = new Point(usmjeriKompoziciju(kompozicija)[0]);
					}

					for (int i = 0; i < kompozicija.wagon.size(); ++i)
					{
						kompozicija.wagon.get(i).previousCoordinates = new Point(kompozicija.locomotive.get(kompozicija.locomotive.size() - 1).previousCoordinates);
						kompozicija.wagon.get(i).currentCoordinates = new Point(kompozicija.locomotive.get(kompozicija.locomotive.size() - 1).currentCoordinates);
					}
					
					if(kompozicija.trinStationToVisit.get(0).coordinates.contains(kompozicija.locomotive.get(0).currentCoordinates))
						kompozicija.movingTime = System.currentTimeMillis();
					
					kompozicija.locomotive.get(0).currentCoordinates = new Point(kord0);
					kompozicija.movingHistory.add(new Point(kompozicija.locomotive.get(0).currentCoordinates));
					
					synchronized (this)
					{
						schedulerMatrix[nameStation - 'A'][susjed.nameStation - 'A']++;
					}
					
					susjed.incomingTrains.add(kompozicija);
					synchronized (GUI.frame)
					{
						GUI.trainMap[kompozicija.locomotive.get(0).currentCoordinates.x][kompozicija.locomotive.get(0).currentCoordinates.y]
								.add(new JLabel(new ImageIcon("resource/train.png")));
						((JLabel) GUI.trainMap[kompozicija.locomotive.get(0).currentCoordinates.x][kompozicija.locomotive
								.get(0).currentCoordinates.y].getComponent(0)).setName(kompozicija.speed + "k");
						GUI.refreshGui();
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
				Thread.sleep(300);
			}
			catch (Exception e)
			{
				Logger.getLogger(TrainStation.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
		}

	}

	synchronized Point[] usmjeriKompoziciju(Train komp)
	{
		if (nameStation == 'A')
			return (new Point[]
			{ new Point(27, 2), new Point(26, 2), new Point(25, 2) });// vraca niz od tri koordinate

		else if (nameStation == 'B' && komp.findNextStation().nameStation == 'A')
			return (new Point[]
			{ new Point(6, 6), new Point(6, 5), new Point(7, 5) }); //nulte koordinate su koordinate stanice prva koordinata je pozicija na koju smjestam kompoziciju, adruga je za provjeru razmaka..

		else if (nameStation == 'B' && komp.findNextStation().nameStation == 'C')
			return (new Point[]
			{ new Point(6, 7), new Point(6, 8), new Point(6, 9) });

		else if (nameStation == 'C' && (komp.findNextStation().nameStation == 'B'))
			return (new Point[]
			{ new Point(13, 19), new Point(11, 19), new Point(10, 19) });

		else if (nameStation == 'C' && komp.findNextStation().nameStation == 'D') 
			return (new Point[]
			{ new Point(13, 20), new Point(12, 21), new Point(12, 22) });

		else if (nameStation == 'C' && komp.findNextStation().nameStation == 'E') 
			return (new Point[]
			{ new Point(13, 19), new Point(14, 20), new Point(15, 20) });

		else if (nameStation == 'D')
			return (new Point[]
			{ new Point(1, 26), new Point(1, 25), new Point(1, 24) });

		else if (nameStation == 'E')
			return (new Point[]
			{ new Point(25, 26), new Point(24, 26), new Point(23, 26) });
		return null;
	}

}
