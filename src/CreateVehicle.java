import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CreateVehicle extends TimerTask{
	ArrayList<Vehicle> waitingVehicles;
	HashMap<Integer, Way> ways;
	static 
	{
		try
		{
			Logger.getLogger(CreateVehicle.class.getName()).addHandler(new FileHandler("Error logs/CreateVehicle.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public CreateVehicle() {
		waitingVehicles = new ArrayList<>();	
		ways = new HashMap<>();
	}
	
	@Override
	public void run() {
		
		readDataFromFile();
		createVehicle();
		setOnMap();
	}
	
	public void readDataFromFile()
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader("config/cars/carConfigFile.txt"));
			String[] lineParam;
			lineParam = br.readLine().split("#");
			ways.put(0,new Way('A',Integer.parseInt(lineParam[1]), Double.parseDouble(lineParam[2])));
			lineParam = br.readLine().split("#");
			ways.put(1,new Way('B',Integer.parseInt(lineParam[1]), Double.parseDouble(lineParam[2])));
			lineParam = br.readLine().split("#");
			ways.put(2,new Way('C',Integer.parseInt(lineParam[1]), Double.parseDouble(lineParam[2])));
			br.close();
			
		} catch (Exception e) {
			Logger.getLogger(CreateVehicle.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	public void createVehicle()
	{
		int wayIndex = new Random().nextInt(3);
		boolean isCar = new Random().nextBoolean();
		
		if(Main.currVheicleCounter[wayIndex] < ways.get(wayIndex).numOfVehicle)
		{
			if(isCar)
				waitingVehicles.add(new Car(ways.get(wayIndex).maxSpeed, ways.get(wayIndex).name, "resource/car.png"));
			else
				waitingVehicles.add(new Truck(ways.get(wayIndex).maxSpeed, ways.get(wayIndex).name, "resource/truck.png"));
		}
	}
	
	public void setOnMap()
	{
		Vehicle tempVehicle;
		
		for(int i=0; i<6; ++i)
		{
			if(!waitingVehicles.isEmpty())
			{
				tempVehicle = waitingVehicles.remove(i);
				tempVehicle.setVehicle();
				Main.currVheicleCounter[tempVehicle.way-'A']++;
				Main.trainMap[tempVehicle.currPoint.x][tempVehicle.currPoint.y].add(new JLabel(new ImageIcon(tempVehicle.imagePath)));
				tempVehicle.start();
			}
			else break;
		}
	}
	
	
	
}
