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
		trenutnaBrzina = (maxBrzina + Math.random() * (3000-maxBrzina));
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
				Point k = sledeciKorak(trKoo,preKoo);
				
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
				else if(GUI.mapa[k.x][k.y]=='x')//prelazak preko pruznog prelaza bez zadrzavanja
				{
					synchronized(this)
					{
						
					//ako na k2 nema nikoga napravi dva koraka bez da gubi monitor i sleep na svakom polju da bude /2
					Point k2 = sledeciKorak(k, trKoo);
					if(!k.equals(k2))
					{
						GUI.guiMapa[k.x][k.y].add((JLabel) GUI.guiMapa[trKoo.x][trKoo.y].getComponents()[0]);
						SwingUtilities.updateComponentTreeUI(GUI.frame);
						
						preKoo.x = trKoo.x;
						preKoo.y = trKoo.y;
						trKoo = k;
						
						try {sleep((long)maxBrzina/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						
						GUI.guiMapa[k2.x][k2.y].add((JLabel) GUI.guiMapa[k.x][k.y].getComponents()[0]);
						SwingUtilities.updateComponentTreeUI(GUI.frame);	
						try {sleep((long)maxBrzina/2);} catch (InterruptedException e) {e.printStackTrace();}
						
						preKoo.x = trKoo.x;
						preKoo.y = trKoo.y;
						trKoo = k2;
					}
					}
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

	synchronized Point sledeciKorak(Point p, Point q)//
	{
		if (p.y < 29 && (GUI.mapa[p.x][p.y + 1] == smjer || (GUI.mapa[p.x][p.y] == smjer && GUI.mapa[p.x][p.y + 1] == 'x'
				|| GUI.mapa[p.x][p.y] == 'x' && GUI.mapa[p.x][p.y + 1] == smjer ))
				&& p.y + 1 != q.y) // provjera desno
		{
			if (GUI.guiMapa[p.x][p.y + 1].getComponents().length == 1)
			{
				return p;
			}
			return  new Point(p.x, p.y + 1);
		}

		else if (p.y > 0 && (GUI.mapa[p.x][p.y - 1] == smjer || (GUI.mapa[p.x][p.y] == smjer && GUI.mapa[p.x][p.y - 1] == 'x'
				|| GUI.mapa[p.x][p.y] == 'x' && GUI.mapa[p.x][p.y - 1] == smjer))
				&& p.y - 1 != q.y) // provjera lijevo
		{
			if (GUI.guiMapa[p.x][p.y - 1].getComponents().length == 1)
				return p;
			return new Point(p.x, p.y - 1);
		}

		else if (p.x > 0 && (GUI.mapa[p.x - 1][p.y] == smjer || (GUI.mapa[p.x][p.y] == smjer && GUI.mapa[p.x - 1][p.y] == 'x'
				|| GUI.mapa[p.x][p.y] == 'x' && GUI.mapa[p.x - 1][p.y] == smjer))
				&& p.x - 1 != q.x) // provjera gore
		{
			if (GUI.guiMapa[p.x - 1][p.y].getComponents().length == 1)
				return p;
			return new Point(p.x - 1, p.y);
		}

		else if (p.x < 29 && (GUI.mapa[p.x + 1][p.y] == smjer || (GUI.mapa[p.x][p.y] == 'x' && GUI.mapa[p.x + 1][p.y] == smjer
				|| GUI.mapa[p.x][p.y] == smjer && GUI.mapa[p.x + 1][p.y] == 'x'))
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
