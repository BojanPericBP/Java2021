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
			Logger.getLogger(Vehicle.class.getName()).addHandler(new FileHandler("Error logs/Vozila.log"));
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
			if(direction == '0')
				currPoint = new Point(29, 8);
			else if(direction == '1')
				currPoint = new Point(29, 14);
		}
		else if(way == 'B')
		{
			if(direction == '0')
				currPoint = new Point(29, 14);
			else if(direction == '1')
				currPoint = new Point(0, 13);
		}
		else if(way == 'C')
		{
			if(direction == '0')
				currPoint = new Point(20, 29);
			else if(direction == '1')
				currPoint = new Point(29, 22);
		}
	}
	
	
	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && (currPoint.x != -1 || currPoint.y != -1))
		{
			
			try 
			{
				sleep(currSpeed);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(Kompozicija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}
			
			GUI.refreshGui();

				Point k = sledeciKorak(currPoint,prevPoint);
				
				if (k.x == -1 && k.y == -1)
				{
					synchronized(GUI.frame)
					{
					GUI.guiMapa[currPoint.x][currPoint.y].remove((JLabel) GUI.guiMapa[currPoint.x][currPoint.y].getComponents()[0]);
					currPoint = k;
					GUI.trenutniBrVozilaNaPutevima[way - 'A']--;
					GUI.refreshGui();
					 }
					try{Thread.sleep((long) currSpeed);}
					catch (Exception e)
					{Logger.getLogger(Vehicle.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());}
				}
				
				else if (currPoint.equals(k) || (GUI.guiMapa[k.x][k.y].getBackground() == Color.red))
				{
					continue;
				}
				else if(GUI.guiMapa[k.x][k.y].getBackground() == Color.orange)//prelazak preko pruznog prelaza bez zadrzavanja
				{
					synchronized(this)
					{
						
					//ako na k2 nema nikoga napravi dva koraka bez da gubi monitor i sleep na svakom polju da bude /2
					Point k2 = sledeciKorak(k, currPoint);
					if(!k.equals(k2))
					{
						GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[currPoint.x][currPoint.y].getComponents()[0]);
						GUI.refreshGui();
						
						prevPoint.x = currPoint.x;
						prevPoint.y = currPoint.y;
						currPoint = k;
						
						try {sleep((long)maxSpeed/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						GUI.guiMapa[k2.x][k2.y].add((JLabel) GUI.guiMapa[k.x][k.y].getComponents()[0]);
						GUI.refreshGui();
						try {sleep((long)maxSpeed/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						prevPoint.x = currPoint.x;
						prevPoint.y = currPoint.y;
						currPoint = k2;
					}
					}
				}
				else
				{
					synchronized(GUI.frame)
					{
					//usaglasavanjeBrzine(k);
					GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[currPoint.x][currPoint.y].getComponents()[0]);
					prevPoint.x = currPoint.x;
					prevPoint.y = currPoint.y;
					currPoint = k;
					GUI.refreshGui();
					}
					
				}

			}
		}


	synchronized Point sledeciKorak(Point p, Point q)//
	{

		if (p.y < 29 && 
				(GUI.guiMapa[p.x][p.y + 1].getBackground().getRed() == direction ||
				(GUI.guiMapa[p.x][p.y].getBackground().getRed() == direction && (GUI.guiMapa[p.x][p.y + 1].getBackground() == Color.orange || GUI.guiMapa[p.x][p.y + 1].getBackground() == Color.red)) ||
				(GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x][p.y + 1].getBackground().getRed() == direction ))
				&& p.y + 1 != q.y) // provjera desno
		{
			if (GUI.guiMapa[p.x][p.y + 1].getComponents().length == 1)
				return p;
			return  new Point(p.x, p.y + 1);
		}

		else if (p.y > 0 && (GUI.guiMapa[p.x][p.y - 1].getBackground().getRed() == direction || (GUI.guiMapa[p.x][p.y].getBackground().getRed() == direction && GUI.guiMapa[p.x][p.y - 1].getBackground() == Color.orange || GUI.guiMapa[p.x][p.y - 1].getBackground() == Color.red
				|| GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x][p.y - 1].getBackground().getRed() == direction))
				&& p.y - 1 != q.y) // provjera lijevo
		{
			if (GUI.guiMapa[p.x][p.y - 1].getComponents().length == 1)
				return p;
			return new Point(p.x, p.y - 1);
		}

		else if (p.x > 0 && (GUI.guiMapa[p.x - 1][p.y].getBackground().getRed() == direction ||
				(GUI.guiMapa[p.x][p.y].getBackground().getRed() == direction && GUI.guiMapa[p.x - 1][p.y].getBackground() == Color.orange || GUI.guiMapa[p.x - 1][p.y].getBackground() == Color.red)
				|| (GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x - 1][p.y].getBackground().getRed() == direction))
				&& p.x - 1 != q.x) // provjera gore
		{
			if (GUI.guiMapa[p.x - 1][p.y].getComponents().length == 1)
				return p;
			return new Point(p.x - 1, p.y);
		}

 		else if (p.x < 29 && (GUI.guiMapa[p.x + 1][p.y].getBackground().getRed() == direction ||
 				(GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x + 1][p.y].getBackground().getRed() == direction
				|| GUI.guiMapa[p.x][p.y].getBackground().getRed() == direction && GUI.guiMapa[p.x + 1][p.y].getBackground() == Color.orange || GUI.guiMapa[p.x + 1][p.y].getBackground() == Color.red))
				&& p.x + 1 != q.x) // provjera dole
		{
			if (GUI.guiMapa[p.x + 1][p.y].getComponents().length == 1)
				return p;
			return  new Point(p.x + 1, p.y);
		}
		else
		{
			return new Point(-1, -1);
		}
	}

}
