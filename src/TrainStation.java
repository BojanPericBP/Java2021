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
	static int schedulerMatrix[][]; //mat[i][j] = 0; putanja od stanice i ka stanici j je slobodna, matrica je konzistentna
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
		while (Main.isAlive)
		{
			Iterator<Train> iteratorKompozicija = outgoingtrains.iterator();

			while (iteratorKompozicija.hasNext()) // pronalazi kompoziciju za koju je slobodna odredjena pruga i
													// usmejru lokomotivu na odgovarajuce polje
			{
				Train tmpTrain = iteratorKompozicija.next();
				boolean isFree = false;
				TrainStation nextStation;
				Point point1 = null;
				Point point2 = null;
				synchronized (schedulerMatrix)
				{
					nextStation = tmpTrain.findNextStation();

					if (schedulerMatrix[nextStation.nameStation - 'A'][(nameStation - 'A')] == 0)
					{
						point1 = redirectTrain(tmpTrain)[1];
						point2 = redirectTrain(tmpTrain)[2];

						if (Main.trainMap[point1.x][point1.y].getComponents().length == 0
								&& Main.trainMap[point2.x][point2.y].getComponents().length == 0)
							isFree = true;
					}
				}
			
				if (isFree)
				{
					if (schedulerMatrix[nameStation - 'A'][nextStation.nameStation - 'A'] != 0)
					{
						long min = tmpTrain.speed;
						// prodjikroz dolazne dolazneKompozicije susjeda i uzmi min brzinu
						for (Train k : nextStation.incomingTrains)
						{
							if (k.prevTrainStation.nameStation == nameStation && k.speed > min)
								min = k.speed;
						}
						
						tmpTrain.reducedSpeed = tmpTrain.speed;
						tmpTrain.speed = min;
					}

					for (int i = 0; i < tmpTrain.train.size(); ++i)
					{
						tmpTrain.train.get(i).previousCoordinates = redirectTrain(tmpTrain)[0];
						tmpTrain.train.get(i).currentCoordinates = new Point(redirectTrain(tmpTrain)[0]);
					}
					
					if(tmpTrain.trinStationToVisit.get(0).coordinates.contains(tmpTrain.train.get(0).currentCoordinates))
						tmpTrain.movingTime = System.currentTimeMillis();
					
					tmpTrain.movingHistory.add(new Point(tmpTrain.train.get(0).currentCoordinates));
					tmpTrain.train.get(0).currentCoordinates = new Point(point1);
					
					synchronized (this)
					{
						schedulerMatrix[nameStation - 'A'][nextStation.nameStation - 'A']++;
					}
					 
					nextStation.incomingTrains.add(tmpTrain);
					
					synchronized (Main.frame)
					{
						Main.trainMap[tmpTrain.train.get(0).currentCoordinates.x][tmpTrain.train.get(0).currentCoordinates.y]
								.add(new JLabel(new ImageIcon("resource/train.png")));
						((JLabel) Main.trainMap[tmpTrain.train.get(0).currentCoordinates.x][tmpTrain.train
								.get(0).currentCoordinates.y].getComponent(0)).setName(tmpTrain.speed + "k");
						Main.refreshGui();
					}

					synchronized(this)
					{
						iteratorKompozicija.remove();
					}
					
					if (tmpTrain.isAlive())
					{
						synchronized (tmpTrain)
						{
							tmpTrain.notify();
						}
					}
					else
					{
						tmpTrain.start();
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

	synchronized Point[] redirectTrain(Train _train)
	{
		//nulte koordinate su koordinate stanice prva koordinata je pozicija na koju smjestam kompoziciju, adruga je za provjeru razmaka..
		if (nameStation == 'C' && (_train.findNextStation().nameStation == 'B'))
			return (new Point[] { new Point(13, 19), new Point(11, 19), new Point(10, 19) });
		
		else if (nameStation == 'C' && _train.findNextStation().nameStation == 'D') 
			return (new Point[] { new Point(13, 20), new Point(12, 21), new Point(12, 22) });
		
		else if (nameStation == 'C' && _train.findNextStation().nameStation == 'E') 
			return (new Point[] { new Point(13, 19), new Point(14, 20), new Point(15, 20) });
		
		else if (nameStation == 'B' && _train.findNextStation().nameStation == 'A')
			return (new Point[]{ new Point(6, 6), new Point(6, 5), new Point(7, 5) }); 
		
		else if (nameStation == 'B' && _train.findNextStation().nameStation == 'C')
			return (new Point[] { new Point(6, 7), new Point(6, 8), new Point(6, 9) });
		
		if (nameStation == 'A')
			return (new Point[]{ new Point(27, 2), new Point(26, 2), new Point(25, 2) });

		else if (nameStation == 'D')
			return (new Point[] { new Point(1, 26), new Point(1, 25), new Point(1, 24) });

		else if (nameStation == 'E')
			return (new Point[] { new Point(25, 26), new Point(24, 26), new Point(23, 26) });
		
		return null;
	}

}
