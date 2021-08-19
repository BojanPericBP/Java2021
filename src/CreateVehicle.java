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
			Logger.getLogger(KreiranjeVozila.class.getName()).addHandler(new FileHandler("Error logs/KreiranjeVozila.log"));
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
			Logger.getLogger(KreiranjeVozila.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	public void createVehicle()
	{
		int wayIndex = new Random().nextInt(3);
		boolean isCar = new Random().nextBoolean();
		
		if(isCar)
			if(GUI.trenutniBrVozilaNaPutevima[wayIndex] < ways.get(wayIndex).numOfVehicle)
				waitingVehicles.add(new Automobil(ways.get(wayIndex).maxSpeed, ways.get(wayIndex).name, "resource/car.png"));
		else 
			if(GUI.trenutniBrVozilaNaPutevima[wayIndex] < ways.get(wayIndex).numOfVehicle)
				waitingVehicles.add(new Kamion(ways.get(wayIndex).maxSpeed, ways.get(wayIndex).name, "resource/car.png"));
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
				GUI.trenutniBrVozilaNaPutevima[i]++;
				GUI.guiMapa[tempVehicle.currPoint.x][tempVehicle.currPoint.y].add(new JLabel(new ImageIcon(tempVehicle.imagePath)));
				((JLabel)GUI.guiMapa[tempVehicle.currPoint.x][tempVehicle.currPoint.y].getComponents()[0]).setName(""+(long)tempVehicle.currSpeed);
				tempVehicle.start();
			}
			else break;
		}
	}
	
	
	
}
