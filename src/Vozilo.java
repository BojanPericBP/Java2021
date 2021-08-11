import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;

public abstract class Vozilo extends Thread
{
	static private int count = 0;
	String marka;
	String model;
	int godiste;
	volatile double maxBrzina;
	volatile double trenutnaBrzina;
	Point trKoo;
	Point preKoo;
	int smjer;
	char put;
	String putanjaSlike;
	
	static 
	{
		try
		{
			Logger.getLogger(Vozilo.class.getName()).addHandler(new FileHandler("Error logs/Vozila.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Vozilo(double _maxBrzina, char _put, String _putanjaSlike)
	{
		maxBrzina = _maxBrzina;
		trenutnaBrzina = (maxBrzina + Math.random() * (1500-maxBrzina));
		marka = "marka" + count;
		model = "model" + count;
		godiste = 1990 + count++;
		put = _put;
		trKoo = new Point();
		preKoo = new Point();
		smjer =  new Random().nextInt(2);
		putanjaSlike=_putanjaSlike;
	}

	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && (trKoo.x != -1 || trKoo.y != -1))
		{
			GUI.refreshGui();

				Point k = sledeciKorak(trKoo,preKoo);
				
				if (k.x == -1 && k.y == -1)
				{
					synchronized(GUI.frame)
					{
					GUI.guiMapa[trKoo.x][trKoo.y].remove((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
					trKoo = k;
					GUI.trenutniBrVozilaNaPutevima[put - 'A']--;
					GUI.refreshGui();
					 }
					try{Thread.sleep((long) trenutnaBrzina);}
					catch (Exception e)
					{Logger.getLogger(Vozilo.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());}
				}
				
				else if (trKoo.equals(k) || (GUI.guiMapa[k.x][k.y].getBackground() == Color.red))
				{
					continue;
				}
				else if(GUI.guiMapa[k.x][k.y].getBackground() == Color.orange)//prelazak preko pruznog prelaza bez zadrzavanja
				{
					synchronized(this)
					{
						
					//ako na k2 nema nikoga napravi dva koraka bez da gubi monitor i sleep na svakom polju da bude /2
					Point k2 = sledeciKorak(k, trKoo);
					if(!k.equals(k2))
					{
						GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
						GUI.refreshGui();
						
						preKoo.x = trKoo.x;
						preKoo.y = trKoo.y;
						trKoo = k;
						
						try {sleep((long)maxBrzina/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						GUI.guiMapa[k2.x][k2.y].add((JLabel) GUI.guiMapa[k.x][k.y].getComponents()[0]);
						GUI.refreshGui();
						try {sleep((long)maxBrzina/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						preKoo.x = trKoo.x;
						preKoo.y = trKoo.y;
						trKoo = k2;
					}
					}
				}
				else
				{
					synchronized(GUI.frame)
					{
					//usaglasavanjeBrzine(k);
					GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
					preKoo.x = trKoo.x;
					preKoo.y = trKoo.y;
					trKoo = k;
					GUI.refreshGui();
					}
					try{Thread.sleep((long) trenutnaBrzina);}
					catch (Exception e)
					{Logger.getLogger(Vozilo.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());}
				}

			}
		}


	synchronized Point sledeciKorak(Point p, Point q)//
	{

		if (p.y < 29 && 
				(GUI.guiMapa[p.x][p.y + 1].getBackground().getRed() == smjer ||
				(GUI.guiMapa[p.x][p.y].getBackground().getRed() == smjer && (GUI.guiMapa[p.x][p.y + 1].getBackground() == Color.orange || GUI.guiMapa[p.x][p.y + 1].getBackground() == Color.red)) ||
				(GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x][p.y + 1].getBackground().getRed() == smjer ))
				&& p.y + 1 != q.y) // provjera desno
		{
			if (GUI.guiMapa[p.x][p.y + 1].getComponents().length == 1)
				return p;
			return  new Point(p.x, p.y + 1);
		}

		else if (p.y > 0 && (GUI.guiMapa[p.x][p.y - 1].getBackground().getRed() == smjer || (GUI.guiMapa[p.x][p.y].getBackground().getRed() == smjer && GUI.guiMapa[p.x][p.y - 1].getBackground() == Color.orange || GUI.guiMapa[p.x][p.y - 1].getBackground() == Color.red
				|| GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x][p.y - 1].getBackground().getRed() == smjer))
				&& p.y - 1 != q.y) // provjera lijevo
		{
			if (GUI.guiMapa[p.x][p.y - 1].getComponents().length == 1)
				return p;
			return new Point(p.x, p.y - 1);
		}

		else if (p.x > 0 && (GUI.guiMapa[p.x - 1][p.y].getBackground().getRed() == smjer ||
				(GUI.guiMapa[p.x][p.y].getBackground().getRed() == smjer && GUI.guiMapa[p.x - 1][p.y].getBackground() == Color.orange || GUI.guiMapa[p.x - 1][p.y].getBackground() == Color.red)
				|| (GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x - 1][p.y].getBackground().getRed() == smjer))
				&& p.x - 1 != q.x) // provjera gore
		{
			if (GUI.guiMapa[p.x - 1][p.y].getComponents().length == 1)
				return p;
			return new Point(p.x - 1, p.y);
		}

 		else if (p.x < 29 && (GUI.guiMapa[p.x + 1][p.y].getBackground().getRed() == smjer ||
 				(GUI.guiMapa[p.x][p.y].getBackground() == Color.orange && GUI.guiMapa[p.x + 1][p.y].getBackground().getRed() == smjer
				|| GUI.guiMapa[p.x][p.y].getBackground().getRed() == smjer && GUI.guiMapa[p.x + 1][p.y].getBackground() == Color.orange || GUI.guiMapa[p.x + 1][p.y].getBackground() == Color.red))
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
