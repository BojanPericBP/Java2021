import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public abstract class Vozilo extends Thread
{
	static private int count = 0;
	String marka;
	String model;
	int godiste;
	double maxBrzina;
	double trenutnaBrzina;
	Point trKoo;
	Point preKoo;
	char smjer;
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
		trenutnaBrzina = (0.5 + Math.random() * (maxBrzina - 0.5));
		marka = "marka" + count;
		model = "model" + count;
		godiste = 1990 + count++;
		put = _put;
		trKoo = new Point();
		preKoo = new Point();
		smjer = (char) ('0' + new Random().nextInt(2));
		putanjaSlike=_putanjaSlike;
	}

	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && (trKoo.x != -1 || trKoo.y != -1))
		{
			try
			{
				Thread.sleep((long) trenutnaBrzina);
			}
			catch (Exception e)
			{
				Logger.getLogger(Vozilo.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
			}

			
			synchronized (GUI.frame)
			{
				Point k = sledeciKorak();

				

				if (k.x == -1 && k.y == -1)
				{
					GUI.guiMapa[trKoo.x][trKoo.y].remove((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
					trKoo = k;
					GUI.trenutniBrVozilaNaPutevima[put - 'A']--;
				}
				
				else if (trKoo.equals(k) || (GUI.mapa[k.x][k.y] == 'x' && GUI.guiMapa[k.x][k.y].getBackground() == Color.red))
				{
					continue;
				}
				
				else
				{
					usaglasavanjeBrzine(k);
					GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
					preKoo.x = trKoo.x;
					preKoo.y = trKoo.y;
					trKoo = k;
				}

				synchronized(GUI.frame)
				{
					SwingUtilities.updateComponentTreeUI(GUI.frame);					
				}
			}
		}
	}


	private synchronized void usaglasavanjeBrzine(Point preKoo)
	{
		Component[] cmp;
		cmp = GUI.guiMapa[preKoo.x][preKoo.y].getComponents();

		if (cmp.length == 1)// ako je vece od nula onda se na toj poziciji nalazi vozilo
			trenutnaBrzina = Integer.parseInt(((JLabel) cmp[0]).getName());
	}

	synchronized Point sledeciKorak()//
	{
		if (trKoo.y < 29 && (GUI.mapa[trKoo.x][trKoo.y + 1] == smjer || (GUI.mapa[trKoo.x][trKoo.y] == smjer && GUI.mapa[trKoo.x][trKoo.y + 1] == 'x'
				|| GUI.mapa[trKoo.x][trKoo.y] == 'x' && GUI.mapa[trKoo.x][trKoo.y + 1] == smjer ))
				&& trKoo.y + 1 != preKoo.y) // provjera desno
		{
			if (GUI.guiMapa[trKoo.x][trKoo.y + 1].getComponents().length == 1)
			{
				return trKoo;
			}
			return /*(GUI.mapa[trKoo.x][trKoo.y + 1] == 'x') ? new Koordinate(-2, -2) :*/ new Point(trKoo.x, trKoo.y + 1);
		}

		else if (trKoo.y > 0 && (GUI.mapa[trKoo.x][trKoo.y - 1] == smjer || (GUI.mapa[trKoo.x][trKoo.y] == smjer && GUI.mapa[trKoo.x][trKoo.y - 1] == 'x'
				|| GUI.mapa[trKoo.x][trKoo.y] == 'x' && GUI.mapa[trKoo.x][trKoo.y - 1] == smjer))
				&& trKoo.y - 1 != preKoo.y) // provjera lijevo
		{
			if (GUI.guiMapa[trKoo.x][trKoo.y - 1].getComponents().length == 1)
				return trKoo;
			return /*(GUI.mapa[trKoo.x][trKoo.y - 1] == 'x') ? new Koordinate(-2, -2) : */new Point(trKoo.x, trKoo.y - 1);
		}

		else if (trKoo.x > 0 && (GUI.mapa[trKoo.x - 1][trKoo.y] == smjer || (GUI.mapa[trKoo.x][trKoo.y] == smjer && GUI.mapa[trKoo.x - 1][trKoo.y] == 'x'
				|| GUI.mapa[trKoo.x][trKoo.y] == 'x' && GUI.mapa[trKoo.x - 1][trKoo.y] == smjer))
				&& trKoo.x - 1 != preKoo.x) // provjera gore
		{
			if (GUI.guiMapa[trKoo.x - 1][trKoo.y].getComponents().length == 1)
				return trKoo;
			return /*(GUI.mapa[trKoo.x - 1][trKoo.y] == 'x') ? new Koordinate(-2, -2) :*/ new Point(trKoo.x - 1, trKoo.y);
		}

		else if (trKoo.x < 29 && (GUI.mapa[trKoo.x + 1][trKoo.y] == smjer || (GUI.mapa[trKoo.x][trKoo.y] == 'x' && GUI.mapa[trKoo.x + 1][trKoo.y] == smjer
				|| GUI.mapa[trKoo.x][trKoo.y] == smjer && GUI.mapa[trKoo.x + 1][trKoo.y] == 'x'))
				&& trKoo.x + 1 != preKoo.x) // provjera dole
		{
			if (GUI.guiMapa[trKoo.x + 1][trKoo.y].getComponents().length == 1)
				return trKoo;
			return /*(GUI.mapa[trKoo.x + 1][trKoo.y] == 'x') ? new Koordinate(-2, -2) :*/ new Point(trKoo.x + 1, trKoo.y);
		}
		else
		{
			return new Point(-1, -1);
		}
	}

}
