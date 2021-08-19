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

public class Composition extends Thread implements Serializable  
{
	private static final long serialVersionUID = 1L;
	static int count=0;

	int idComp;
	volatile long movingTime;
	ArrayList<Point> movingHistory;
	
	ArrayList<Locomotive> locomotive;
	ArrayList<Vagon> wagon;
	volatile long speed;
	volatile long reducedSpeed;
	ArrayList<TrainStation> trinStationToVisit;
	TrainStation prevTrainStation;
	
	static 
	{
		try
		{
			Logger.getLogger(Composition.class.getName()).addHandler(new FileHandler("Error logs/Kompozicija.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Composition(String _rasporedL, String rasporedV, long _brzina, ArrayList<TrainStation> _linija) throws Exception 
	{
		idComp = count++;
		trinStationToVisit = _linija;
		prevTrainStation = trinStationToVisit.get(0);
		speed = _brzina <= 500 ? 500: _brzina;
		
		wagon = new ArrayList<>();
		locomotive = new ArrayList<>();
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

			
				//ZeljeznickaStanica susjed = odrediSusjeda();
			TrainStation susjed = findNextStation();
			
			try 
			{
				sleep(speed);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(Composition.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
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
						Logger.getLogger(Composition.class.getName()).log(Level.WARNING,e.fillInStackTrace().toString());
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
						Logger.getLogger(Composition.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
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
		List<String> trains = new ArrayList<>();
		List<String> trainCar = new ArrayList<>();
		
		if(rasporedLokomotiva.equals("null"))
			throw e;
		
		if(!rasporedVagona.equals("null"))
		{
			trainCar = Arrays.asList(rasporedVagona.split(","));
		}
		
		trains = Arrays.asList(rasporedLokomotiva.split(","));
		

		if(trains.size() > 1 && trains.contains("ml") && (trains.contains("tl") || trains.contains("pl")))
				throw e;
		else if(trains.contains("ml") && (trainCar.contains("tv") || trainCar.contains("pvs") || trainCar.contains("pvr")))
			throw e;
		
		else if(trains.contains("ml") && trainCar.contains(""))
			throw e;
		
		else if(trains.contains("pl") && (trains.contains("tl") || trainCar.contains("tv") || trainCar.contains("vpn")))
			throw e;
		
		else if(trains.contains("tl") && (trains.contains("pl") || trainCar.contains("pvs") || trainCar.contains("pvr") || trainCar.contains("vpn")))
			throw e;
		
		else if(trainCar.contains("tv") && (trainCar.contains("pvr") || trainCar.contains("pvs")) && !trains.contains("ul"))
			throw e;
		
		for(String s : trains)
		{
			switch (s) {
			case "tl": {
				locomotive.add(new Locomotive("Teretna"));
				break;
			}
			case "pl":{
				locomotive.add(new Locomotive("Putnicka"));
				break;
			}
			case "ul":{
				locomotive.add(new Locomotive("Univerzalna"));
				break;
			}
			case "ml":{
				locomotive.add(new Locomotive("Manevarska"));
				break;
			}
			}
		}
		
		for(String s : trainCar)
		{
			switch (s) {
			case "pvr": {
				wagon.add(new PassengerWagonRestaurant());
				break;
			}
			case "pvs":{
				wagon.add(new PassengerWagonSleep());
				break;
			}
			case "vpn":{
				wagon.add(new Vagon(false));
				break;
			}
			case "tv":{
				wagon.add(new FreightWagon());
				break;
			}
			default:
				throw e;
			}
		}
		
	}
	
	synchronized void arriveToStation() 
	{
		int granicaLOK=1;
		int granicaVAG=0;
		while(granicaLOK!=locomotive.size() || granicaVAG!=wagon.size()) 
		{
			synchronized(this)
			{
				rampUpDown();				
			}
			for (int i = granicaLOK; i < locomotive.size(); i++)
			{
				if(!locomotive.get(i).move()) granicaLOK=i+1;
				synchronized(GUI.frame)
				{
					GUI.refreshGui();
				}
			}
			
			for (int i = granicaVAG; i < wagon.size(); i++)
			{
				if(!wagon.get(i).move()) granicaVAG=i+1;
				synchronized(GUI.frame)
				{
					GUI.refreshGui();
				}
			}	
			try { Thread.sleep(speed); } catch (InterruptedException e) { Logger.getLogger(Composition.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString()); }
		}
	}

	
	synchronized void rampUpDown()
	{
		boolean flag = true; //treba spustiti rampu
		for (int i = 18; i < 24; i++) {
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
		for (int j = 11; j < 17; j++) {
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
		{
			tmpRamp(flag,6,13,6,14);
		}
		
		flag = true;
		for (int i = 18; i < 24; i++) {
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
		for (int i = 0; i < locomotive.size(); i++) 
		{
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			if (prevTrainStation.coordinates.contains(locomotive.get(i).currentCoordinates) && !prevTrainStation.coordinates.contains(locomotive.get(i-1).previousCoordinates))  // U prvom koraku se nece ispitivati drgui uslov
			{
				locomotive.get(i).currentCoordinates = new Point(locomotive.get(i-1).previousCoordinates);
				GUI.trainMap[locomotive.get(i).currentCoordinates.x][locomotive.get(i).currentCoordinates.y].add(new JLabel(new ImageIcon("resource/train.png")));
				((JLabel)GUI.trainMap[locomotive.get(i).currentCoordinates.x][locomotive.get(i).currentCoordinates.y].getComponent(0)).setName(speed+"k");
			}
			
			else if(i!=0 && GUI.trainMap[locomotive.get(i - 1).previousCoordinates.x][locomotive.get(i - 1).previousCoordinates.y].getComponents().length != 0) 
				continue;
			else 
			{	
					boolean flag = locomotive.get(i).move();
					if(i == 0)
					{
						movingHistory.add(new Point(locomotive.get(0).currentCoordinates));
					}
				 
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
		
		for (int i = 0; i < wagon.size(); i++) 
		{
			
			//prvi vagon, u stanici je  i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			if(i==0 && prevTrainStation.coordinates.contains(wagon.get(i).currentCoordinates) && !prevTrainStation.coordinates.contains(locomotive.get(locomotive.size()-1).previousCoordinates))
			{
				wagon.get(i).currentCoordinates = new Point(locomotive.get(locomotive.size()-1).previousCoordinates);
				GUI.trainMap[wagon.get(i).currentCoordinates.x][wagon.get(i).currentCoordinates.y].add(new JLabel(new ImageIcon("resource/traincar.png")));
				((JLabel)GUI.trainMap[wagon.get(i).currentCoordinates.x][wagon.get(i).currentCoordinates.y].getComponent(0)).setName(speed+"k");
				synchronized(GUI.frame)
				{
					GUI.refreshGui();				
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					
					Logger.getLogger(Composition.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			//prvi vagon, nije u stanici i nije zadnja lokomotiva zauzela mjesto gdje on treba ici
			else if(i==0 && !prevTrainStation.coordinates.contains(wagon.get(i).currentCoordinates) && !prevTrainStation.coordinates.contains(locomotive.get(locomotive.size()-1).previousCoordinates))
			{
				if(!wagon.get(i).move()) 
				{
					synchronized(GUI.frame)
					{
						GUI.refreshGui();					
					}

					arriveToStation();
					return true; // vagon uso u stanicu
				}
			}
			
			//Da li (jeUStanici && nijePrethodni na prvom polju pruge)
			else if (i>0 && prevTrainStation.coordinates.contains(wagon.get(i).currentCoordinates) && !prevTrainStation.coordinates.contains(wagon.get(i-1).previousCoordinates))
			{
				wagon.get(i).currentCoordinates = new Point(wagon.get(i-1).previousCoordinates);
				GUI.trainMap[wagon.get(i).currentCoordinates.x][wagon.get(i).currentCoordinates.y].add(new JLabel(new ImageIcon("resource/traincar.png")));
				((JLabel)GUI.trainMap[wagon.get(i).currentCoordinates.x][wagon.get(i).currentCoordinates.y].getComponent(0)).setName(speed+"k");
				synchronized(GUI.frame)
				{
					GUI.refreshGui();					
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					Logger.getLogger(Composition.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
				}
			}
			
			else if (i > 0 && GUI.trainMap[wagon.get(i - 1).previousCoordinates.x][wagon.get(i - 1).previousCoordinates.y].getComponents().length == 0  && !wagon.get(i).move())
			{
				synchronized(GUI.frame)
				{
					GUI.refreshGui();						
				}

				arriveToStation();
				return true; // vagon uso u stanicu
			}
		}
		return false;
	}
	
}



