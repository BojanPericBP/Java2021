import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Vozilo extends Thread
{
	static private int count = 0;
	String marka;
	String model;
	int godiste;
	double maxBrzina;
	double trenutnaBrzina;
	Koordinate trKoo;
	Koordinate preKoo;
	char smjer;
	char put;
	String putanjaSlike;
	
	static 
	{
		try
		{
			Logger.getLogger(Vozilo.class.getName()).addHandler(new FileHandler("Error logs/Vozila.log"));
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public Vozilo(double _maxBrzina, char _put, String _putanjaSlike)
	{
		maxBrzina = _maxBrzina;
		trenutnaBrzina = (0.5 + Math.random() * (maxBrzina - 0.5)) * 1000;
		marka = "marka" + count;
		model = "model" + count;
		godiste = 1990 + count++;
		put = _put;
		trKoo = new Koordinate(-3, -3);
		preKoo = new Koordinate(-3, -3);
		smjer = (char) ('0' + new Random().nextInt(2));
		putanjaSlike=_putanjaSlike;
	}

	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && (trKoo.i != -1 || trKoo.j != -1))
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
				Koordinate k = sledeciKorak();

				if (trKoo.equals(k))
				{
					continue;
				}

				if (k.i == -1 && k.j == -1)
				{
					GUI.guiMapa[trKoo.i][trKoo.j].remove((JLabel) GUI.guiMapa[trKoo.i][trKoo.j].getComponents()[0]);
					trKoo = k;
					GUI.trenutniBrVozilaNaPutevima[put - 'A']--;
				}
				else if (k.i == -2)
				{
					Koordinate tmpKoord = prelazakPruznogPrelaza();
					if (tmpKoord != null)
					{
						GUI.guiMapa[tmpKoord.i][tmpKoord.j].add((JLabel) GUI.guiMapa[trKoo.i][trKoo.j].getComponents()[0]);
						trKoo = tmpKoord;
					}
					else
					{
						continue; 
					}
				}
				else
				{
					usaglasavanjeBrzine(k);
					GUI.guiMapa[k.i][k.j].add((JLabel) GUI.guiMapa[trKoo.i][trKoo.j].getComponents()[0]);

					preKoo.i = trKoo.i;
					preKoo.j = trKoo.j;
					trKoo = k;
				}

				synchronized(GUI.frame)
				{
					SwingUtilities.updateComponentTreeUI(GUI.frame);					
				}
			}
		}
	}

	private synchronized Koordinate prelazakPruznogPrelaza() // provjerava da li se moze preci preko pruge i vraca: koordinate
												// sledeceg polja ako se moze preci, null ako ne moze
	{

		if (put == 'A' && smjer == '0' && GUI.guiMapa[20][2].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[20][1].getComponents().length == 0)
			{
				preKoo.i = 20;
				preKoo.j = 2;
				return new Koordinate(20, 1);
			}
		}

		else if (put == 'A' && smjer == '1' && GUI.guiMapa[21][2].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[21][3].getComponents().length == 0)
			{
				preKoo.i = 21;
				preKoo.j = 2;
				return new Koordinate(21, 3);
			}
		}

		else if (put == 'B' && smjer == '0' && GUI.guiMapa[6][14].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[5][14].getComponents().length == 0)
			{
				preKoo.i = 6;
				preKoo.j = 14;
				return new Koordinate(5, 14);
			}
		}

		else if (put == 'B' && smjer == '1' && GUI.guiMapa[6][13].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[7][13].getComponents().length == 0)
			{
				preKoo.i = 6;
				preKoo.j = 13;
				return new Koordinate(7, 13);
			}
		}

		else if (put == 'C' && smjer == '0' && GUI.guiMapa[20][26].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[20][25].getComponents().length == 0)
			{
				preKoo.i = 20;
				preKoo.j = 26;
				return new Koordinate(20, 25);
			}
		}

		else if (put == 'C' && smjer == '1' && GUI.guiMapa[21][26].getBackground() == Color.orange)
		{
			if (GUI.guiMapa[21][27].getComponents().length == 0)
			{
				preKoo.i = 21;
				preKoo.j = 26;
				return new Koordinate(21, 27);
			}
		}

		return null;
	}

	private synchronized void usaglasavanjeBrzine(Koordinate preKoo)
	{
		Component[] cmp;
		cmp = GUI.guiMapa[preKoo.i][preKoo.j].getComponents();

		if (cmp.length == 1)// ako je vece od nula onda se na toj poziciji nalazi vozilo
			trenutnaBrzina = Integer.parseInt(((JLabel) cmp[0]).getName());
	}

	synchronized Koordinate sledeciKorak()//
	{

		if (trKoo.j < 29 && ((GUI.mapa[trKoo.i][trKoo.j + 1] == smjer || GUI.mapa[trKoo.i][trKoo.j + 1] == 'x') && trKoo.j + 1 != preKoo.j)) // provjera desno
		{
			if (GUI.guiMapa[trKoo.i][trKoo.j + 1].getComponents().length == 1)
			{
				return trKoo;
			}
			return (GUI.mapa[trKoo.i][trKoo.j + 1] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trKoo.i, trKoo.j + 1);
		}

		else if (trKoo.j > 0 && ((GUI.mapa[trKoo.i][trKoo.j - 1] == smjer || GUI.mapa[trKoo.i][trKoo.j - 1] == 'x') && trKoo.j - 1 != preKoo.j)) // provjera lijevo
		{
			if (GUI.guiMapa[trKoo.i][trKoo.j - 1].getComponents().length == 1)
				return trKoo;
			return (GUI.mapa[trKoo.i][trKoo.j - 1] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trKoo.i, trKoo.j - 1);
		}

		else if (trKoo.i > 0 && ((GUI.mapa[trKoo.i - 1][trKoo.j] == smjer || GUI.mapa[trKoo.i - 1][trKoo.j] == 'x') && trKoo.i - 1 != preKoo.i)) // provjera gore
		{
			if (GUI.guiMapa[trKoo.i - 1][trKoo.j].getComponents().length == 1)
				return trKoo;
			return (GUI.mapa[trKoo.i - 1][trKoo.j] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trKoo.i - 1, trKoo.j);
		}

		else if (trKoo.i < 29 && ((GUI.mapa[trKoo.i + 1][trKoo.j] == smjer || GUI.mapa[trKoo.i + 1][trKoo.j] == 'x') && trKoo.i + 1 != preKoo.i)) // provjera dole
		{
			if (GUI.guiMapa[trKoo.i + 1][trKoo.j].getComponents().length == 1)
				return trKoo;
			return (GUI.mapa[trKoo.i + 1][trKoo.j] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trKoo.i + 1, trKoo.j);
		}
		else
		{
			return new Koordinate(-1, -1);
		}
	}

}
