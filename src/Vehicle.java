import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

public abstract class Vehicle extends Thread
{
	static private int count = 0;
	
	String manType;
	String model;
	int manYear;
	volatile double maxSpeed;
	volatile long currSpeed;
	Point currPoint;
	Point prevPoint;
	int direction;
	char way;
	String imagePath;
	
	static 
	{
		try
		{
			Logger.getLogger(Vehicle.class.getName()).addHandler(new FileHandler("logs/Vehicle.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Vehicle(double _maxSpeed, char _way, String _imagePath)
	{
		manType = "marka" + count;
		model = "model" + count;
		manYear = 1990 + count++;
		currPoint = new Point();
		prevPoint = new Point();
		maxSpeed = _maxSpeed;
		currSpeed = (long)(maxSpeed + Math.random() * (1500-maxSpeed));
		way = _way;
		direction =  new Random().nextInt(2);
		imagePath=_imagePath;
	}

	public void setVehicle()
	{
		if(way == 'A')
		{
			if(direction == 0)
				currPoint = new Point(29, 8);
			else if(direction == 1)
				currPoint = new Point(21, 0);
		}
		else if(way == 'B')
		{
			if(direction == 0)
				currPoint = new Point(29, 14);
			else if(direction == 1)
				currPoint = new Point(0, 13);
		}
		else if(way == 'C')
		{
			if(direction == 0)
				currPoint = new Point(20, 29);
			else if(direction == 1)
				currPoint = new Point(29, 22);
		}
	}
	
	
	@Override
	public void run()
	{
		while (Main.isAlive && (currPoint.x != -1 || currPoint.y != -1))
		{
			
			Main.refreshGui();
			try 
			{ sleep(currSpeed); } 
			catch (Exception e) 
			{ Logger.getLogger(Train.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString()); }
			
				Point k = nextStep(currPoint,prevPoint);
				
				if (k.x == -1 && k.y == -1) // vozilo je na kraju mape i treba ga ukloniti
				{
					synchronized(Main.frame)
					{
						Main.trainMap[currPoint.x][currPoint.y].remove((JLabel) Main.trainMap[currPoint.x][currPoint.y].getComponents()[0]);
						Main.refreshGui();
						currPoint = k;
						Main.currVheicleCounter[way - 'A']--;
					 }
					try{ Thread.sleep((long) currSpeed); }
					catch (Exception e) {Logger.getLogger(Vehicle.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());}
				}
				
				else if (currPoint.equals(k) || (Main.trainMap[k.x][k.y].getBackground() == Color.red))// ili je ispred auto ili spustena rampa pa auto trebas da stoji
				{
					continue;
				}
				else if(Main.trainMap[k.x][k.y].getBackground() == Color.orange)//prelazak preko pruznog prelaza bez zadrzavanja
				{
					synchronized(this)
					{
						
					//ako na k2 nema nikoga napravi dva koraka bez da gubi monitor i sleep na svakom polju da bude manji za 1/2
						
					Point k2 = nextStep(k, currPoint); // uzmem dva koraka u naprijed
					if(!k.equals(k2))
					{
						Main.trainMap[k.x][k.y].add((JLabel) Main.trainMap[currPoint.x][currPoint.y].getComponents()[0]);
						Main.refreshGui();
						
						prevPoint.x = currPoint.x;
						prevPoint.y = currPoint.y;
						currPoint = k;
						
						try {sleep((long)maxSpeed/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						Main.trainMap[k2.x][k2.y].add((JLabel) Main.trainMap[k.x][k.y].getComponents()[0]);
						Main.refreshGui();
						try {sleep((long)maxSpeed/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						prevPoint.x = currPoint.x;
						prevPoint.y = currPoint.y;
						currPoint = k2;
					}
					}
				}
				else
				{
					synchronized(Main.frame)
					{
						Main.trainMap[k.x][k.y].add((JLabel) Main.trainMap[currPoint.x][currPoint.y].getComponents()[0]);
						prevPoint.x = currPoint.x;
						prevPoint.y = currPoint.y;
						currPoint = k;
						Main.refreshGui();
					}
				}

			}
		}


	synchronized Point nextStep(Point p, Point q)//
	{

		if (p.y < 29 && 
				(Main.trainMap[p.x][p.y + 1].getBackground().getRed() == direction ||
				(Main.trainMap[p.x][p.y].getBackground().getRed() == direction && (Main.trainMap[p.x][p.y + 1].getBackground() == Color.orange || Main.trainMap[p.x][p.y + 1].getBackground() == Color.red)) ||
				(Main.trainMap[p.x][p.y].getBackground() == Color.orange && Main.trainMap[p.x][p.y + 1].getBackground().getRed() == direction ))
				&& p.y + 1 != q.y) // provjera desno
		{
			if (Main.trainMap[p.x][p.y + 1].getComponents().length == 1)
				return p;
			return  new Point(p.x, p.y + 1);
		}

		else if (p.y > 0 && (Main.trainMap[p.x][p.y - 1].getBackground().getRed() == direction || (Main.trainMap[p.x][p.y].getBackground().getRed() == direction && Main.trainMap[p.x][p.y - 1].getBackground() == Color.orange || Main.trainMap[p.x][p.y - 1].getBackground() == Color.red
				|| Main.trainMap[p.x][p.y].getBackground() == Color.orange && Main.trainMap[p.x][p.y - 1].getBackground().getRed() == direction))
				&& p.y - 1 != q.y) // provjera lijevo
		{
			if (Main.trainMap[p.x][p.y - 1].getComponents().length == 1)
				return p;
			return new Point(p.x, p.y - 1);
		}

		else if (p.x > 0 && (Main.trainMap[p.x - 1][p.y].getBackground().getRed() == direction ||
				(Main.trainMap[p.x][p.y].getBackground().getRed() == direction && Main.trainMap[p.x - 1][p.y].getBackground() == Color.orange || Main.trainMap[p.x - 1][p.y].getBackground() == Color.red)
				|| (Main.trainMap[p.x][p.y].getBackground() == Color.orange && Main.trainMap[p.x - 1][p.y].getBackground().getRed() == direction))
				&& p.x - 1 != q.x) // provjera gore
		{
			if (Main.trainMap[p.x - 1][p.y].getComponents().length == 1)
				return p;
			return new Point(p.x - 1, p.y);
		}

 		else if (p.x < 29 && (Main.trainMap[p.x + 1][p.y].getBackground().getRed() == direction ||
 				(Main.trainMap[p.x][p.y].getBackground() == Color.orange && Main.trainMap[p.x + 1][p.y].getBackground().getRed() == direction
				|| Main.trainMap[p.x][p.y].getBackground().getRed() == direction && Main.trainMap[p.x + 1][p.y].getBackground() == Color.orange || Main.trainMap[p.x + 1][p.y].getBackground() == Color.red))
				&& p.x + 1 != q.x) // provjera dole
		{
			if (Main.trainMap[p.x + 1][p.y].getComponents().length == 1)
				return p;
			return  new Point(p.x + 1, p.y);
		}
		else
		{
			return new Point(-1, -1); //ako treba izaci sa mape vrati -1
		}
	}

}
