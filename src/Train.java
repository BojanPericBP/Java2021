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

public class Train extends Thread implements Serializable  
{
	private static final long serialVersionUID = 1L;
	static int count=0;

	int idComp;
	volatile long movingTime;
	ArrayList<Point> movingHistory;

	ArrayList<ElementOfComposition> train;
	volatile long speed;
	volatile long reducedSpeed;
	ArrayList<TrainStation> trinStationToVisit;
	TrainStation prevTrainStation;
	
	static 
	{
		try
		{
			Logger.getLogger(Train.class.getName()).addHandler(new FileHandler("Error logs/Kompozicija.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Train(String _rasporedL, String rasporedV, long _brzina, ArrayList<TrainStation> _linija) throws Exception 
	{
		idComp = count++;
		trinStationToVisit = _linija;
		prevTrainStation = trinStationToVisit.get(0);
		speed = _brzina <= 500 ? 500: _brzina;
		
		train = new ArrayList<>();
		reducedSpeed = speed;
		
		movingHistory = new ArrayList<Point>();
		makeComp(_rasporedL,rasporedV);
	}
	
	@Override
	public void run()
	{
		
		while (GUI.isAlive && prevTrainStation.nameStation != trinStationToVisit.get(trinStationToVisit.size()-1).nameStation) 
		{
		
			synchronized(this)
			{
				rampUpDown();				
			}

			TrainStation susjed = findNextStation();
			
			try 
			{
				sleep(speed);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(Train.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
			
			if (move()) // kompozicija usla u stanicu
			{
				TrainStation.schedulerMatrix[prevTrainStation.nameStation - 'A'][susjed.nameStation- 'A']--;
				prevTrainStation = susjed; // npr prethodna je A ovo vrati B

				susjed = findNextStation(); // prethodna je sada B pa ovo vrati C
				if (prevTrainStation.nameStation == trinStationToVisit.get(trinStationToVisit.size()-1).nameStation) // da li je u odredisnoj stanici
				{
					movingTime = System.currentTimeMillis() - movingTime;
					movingTime /=1000;
					try {
							
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serijalizacija/kompozicija"+idComp+".ser"));
					oos.writeObject(this);
					oos.close();
					}
					catch (Exception e) {
						e.printStackTrace();
						Logger.getLogger(Train.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
					}	
				} 
				else // da li je u bilo kojoj stanici koja nije odredisna
				{
					try 
					{
						synchronized (this) 
						{
							prevTrainStation.outgoingtrains.add(this);
							speed = reducedSpeed;
							wait();
						}
					} 
					catch (Exception e)
					{
						Logger.getLogger(Train.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
					}
				}
			}
			synchronized (GUI.trainMap) 
			{
				GUI.refreshGui();
			}
				
			synchronized(this)
			{
				rampUpDown();				
			}
		}
		
	}

	synchronized TrainStation findNextStation() { // vrati referencu susjedne stanice ka kojoj se kompozicija krece

		for(int i = 0; i<trinStationToVisit.size();++i)
			if(i < trinStationToVisit.size()-1 && trinStationToVisit.get(i).nameStation == prevTrainStation.nameStation)
				return trinStationToVisit.get(i+1);
		return trinStationToVisit.get(trinStationToVisit.size()-1);
	}

	private void makeComp(String rasporedLokomotiva, String rasporedVagona) throws Exception
	{
		//lokomotive vagoni //null vagoni // lokomotive null
		Exception e = new Exception("Wrong train format");
		List<String> locomotives = new ArrayList<>();
		List<String> wagons = new ArrayList<>();
		
		if(rasporedLokomotiva.equals("null"))
			throw e;
		
		if(!rasporedVagona.equals("null"))
		{
			wagons = Arrays.asList(rasporedVagona.split(","));
		}
		
		locomotives = Arrays.asList(rasporedLokomotiva.split(","));
		

		if(locomotives.size() > 1 && locomotives.contains("ml") && (locomotives.contains("tl") || locomotives.contains("pl")))
				throw e;
		else if(locomotives.contains("ml") && (wagons.contains("tv") || wagons.contains("pvs") || wagons.contains("pvr")))
			throw e;
		
		else if(locomotives.contains("ml") && wagons.contains(""))
			throw e;
		
		else if(locomotives.contains("pl") && (locomotives.contains("tl") || wagons.contains("tv") || wagons.contains("vpn")))
			throw e;
		
		else if(locomotives.contains("tl") && (locomotives.contains("pl") || wagons.contains("pvs") || wagons.contains("pvr") || wagons.contains("vpn")))
			throw e;
		
		else if(wagons.contains("tv") && (wagons.contains("pvr") || wagons.contains("pvs")) && !locomotives.contains("ul"))
			throw e;
		
		for(String s : locomotives)
		{
			switch (s) {
			case "tl": {
				train.add(new Locomotive("Teretna"));
				break;
			}
			case "pl":{
				train.add(new Locomotive("Putnicka"));
				break;
			}
			case "ul":{
				train.add(new Locomotive("Univerzalna"));
				break;
			}
			case "ml":{
				train.add(new Locomotive("Manevarska"));
				break;
			}
			}
		}
		
		for(String s : wagons)
		{
			switch (s) {
			case "pvr": {
				train.add(new PassengerWagonRestaurant());
				break;
			}
			case "pvs":{
				train.add(new PassengerWagonSleep());
				break;
			}
			case "vpn":{
				train.add(new Vagon(false));
				break;
			}
			case "tv":{
				train.add(new FreightWagon());
				break;
			}
			default:
				throw e;
			}
		}
	}
	
	synchronized void arriveToStation() 
	{
		int j = 1;
			while(j < train.size())
			{
				for (int i = j; i < train.size(); i++) 
				{
					synchronized(this)
					{ rampUpDown();	 }

					if(!train.get(i).move())
						j++;
					
					synchronized(GUI.frame)
					{ GUI.refreshGui(); }
				}
				try { Thread.sleep(speed); } catch (InterruptedException e) { Logger.getLogger(Train.class.getName()).log(Level.WARNING, 							e.fillInStackTrace().toString()); 
				}
			}
	}

	
	synchronized void rampUpDown()
	{
		boolean flag = true; //treba spustiti rampu
		for (int i = 18; i < 23; i++) {
			if(GUI.trainMap[i][2].getComponents().length ==1)
			{
				if( (GUI.trainMap[i][2].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.trainMap[i][2].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
					flag = false;
				}
			}
		}
		
		synchronized(GUI.trainMap)
		{
			tmpRamp(flag,20,2,21,2);
		}
		flag = true;
		for (int j = 11; j < 16; j++) {
			if(GUI.trainMap[6][j].getComponents().length ==1)
			{
				if( (GUI.trainMap[6][j].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.trainMap[6][j].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
						flag = false;
				}
			}
		}
		
		synchronized(GUI.trainMap)
		{ tmpRamp(flag,6,13,6,14); }
		
		flag = true;
		for (int i = 18; i < 23; i++) {
			if(GUI.trainMap[i][26].getComponents().length ==1)
			{
				if( (GUI.trainMap[i][26].getComponents()[0]) != null)
				{
					String s = ((JLabel)(GUI.trainMap[i][26].getComponents()[0])).getName(); //Prelaz a
					if(s != null && s.contains("k"))
					flag = false;
				}
			}
		}
		synchronized(GUI.trainMap)
		{
			tmpRamp(flag,20,26,21,26);
		}
	}
	
	synchronized private void tmpRamp(boolean flag,int i1, int j1, int i2,int j2)
	{
		if(flag)
		{
			GUI.trainMap[i1][j1].setBackground(Color.orange);
			GUI.trainMap[i2][j2].setBackground(Color.orange);
		}
		else {
			GUI.trainMap[i1][j1].setBackground(Color.red);
			GUI.trainMap[i2][j2].setBackground(Color.red);
		}
	}
	
	synchronized boolean move() // true kad udje u stanicu
	{
		for (int i = 0; i < train.size(); i++) 
		{
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			// U prvom koraku se nece ispitivati drgui uslov
			if (prevTrainStation.coordinates.contains(train.get(i).currentCoordinates) && !prevTrainStation.coordinates.contains(train.get(i-1).previousCoordinates))  
			{
				train.get(i).currentCoordinates = new Point(train.get(i-1).previousCoordinates);
				GUI.trainMap[train.get(i).currentCoordinates.x][train.get(i).currentCoordinates.y].add(new JLabel(new ImageIcon(train.get(i).imgPath)));//TODO img path dodati kao atribut elementa kompozicije
				((JLabel)GUI.trainMap[train.get(i).currentCoordinates.x][train.get(i).currentCoordinates.y].getComponent(0)).setName(speed+"k");
			}
			
			else if(i!=0 && GUI.trainMap[train.get(i - 1).previousCoordinates.x][train.get(i - 1).previousCoordinates.y].getComponents().length != 0) 
				continue;
			else 
			{	
					boolean flag = train.get(i).move();
					if(i == 0)
						movingHistory.add(new Point(train.get(0).currentCoordinates));
				 
					synchronized(GUI.frame)
					{				
						GUI.refreshGui();
					}
					
					if(!flag)
					{
						arriveToStation();
						return true;
					}
			}
		}
		return false;
	}
	
}