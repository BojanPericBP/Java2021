import java.awt.Component;
import java.util.Random;
import java.util.logging.*;
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
	Koordinate trenutneKoordinate;
	Koordinate prethodneKoordinate;
	char smjer;
	char put;
	String putanjaSlike;
	static FileHandler handler;
	static final int MIN_BRZINA = 1500; //1,5 sekundi najsporije kretanje, a najbrze se cita iz datoteke
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Vozila.log");
			Logger.getLogger(Vozilo.class.getName()).addHandler(handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Vozilo(double maxBrzinaArg, char putArg, String putanjaSlikeArg)
	{
		maxBrzina = maxBrzinaArg;
		trenutnaBrzina = maxBrzina + Math.random() * (MIN_BRZINA-maxBrzina);
		marka = "marka" + new Random().nextInt(100);
		model = "model" + new Random().nextInt(100);
		godiste = 1990 + count++;
		put = putArg;
		trenutneKoordinate = new Koordinate(-3, -3);
		prethodneKoordinate = new Koordinate(-3, -3);
		smjer = (char) ('0' + new Random().nextInt(2));
		putanjaSlike = putanjaSlikeArg;
	}

	@Override
	public void run()
	{
		while (GUI.simulacijaUToku && (trenutneKoordinate.i != -1 || trenutneKoordinate.j != -1))
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

				if (trenutneKoordinate.equals(k))
				{
					continue;
				}

				if (k.i == -1 && k.j == -1)
				{
					GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].remove((JLabel) GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].getComponents()[0]);
					trenutneKoordinate = k;
					GUI.trenutniBrVozilaNaPutevima[put - 'A']--;
				}
				else if (k.i == -2)
				{
					Koordinate tmpKoord = prelazakPruznogPrelaza();
					if (tmpKoord != null)
					{
						GUI.guiMapa[tmpKoord.i][tmpKoord.j].add((JLabel) GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].getComponents()[0]);
						trenutneKoordinate = tmpKoord;
					}
					else
					{
						continue; 
					}
				}
				else
				{
					usaglasavanjeBrzine(k);
					GUI.guiMapa[k.i][k.j].add((JLabel) GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j].getComponents()[0]);

					prethodneKoordinate.i = trenutneKoordinate.i;
					prethodneKoordinate.j = trenutneKoordinate.j;
					trenutneKoordinate = k;
				}
				SwingUtilities.updateComponentTreeUI(GUI.frame);					
				
			}
		}
	}

	private synchronized Koordinate prelazakPruznogPrelaza() // provjerava da li se moze preci preko pruge. Vraca koordinate sledeceg polja ako se moze preci ili null ako ne moze
	{

		if (put == 'A' && smjer == '0' && GUI.guiMapa[20][2].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[20][1].getComponents().length == 0)
			{
				prethodneKoordinate.i = 20;
				prethodneKoordinate.j = 2;
				return new Koordinate(20, 1);
			}
		}

		else if (put == 'A' && smjer == '1' && GUI.guiMapa[21][2].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[21][3].getComponents().length == 0)
			{
				prethodneKoordinate.i = 21;
				prethodneKoordinate.j = 2;
				return new Koordinate(21, 3);
			}
		}

		else if (put == 'B' && smjer == '0' && GUI.guiMapa[6][14].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[5][14].getComponents().length == 0)
			{
				prethodneKoordinate.i = 6;
				prethodneKoordinate.j = 14;
				return new Koordinate(5, 14);
			}
		}

		else if (put == 'B' && smjer == '1' && GUI.guiMapa[6][13].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[7][13].getComponents().length == 0)
			{
				prethodneKoordinate.i = 6;
				prethodneKoordinate.j = 13;
				return new Koordinate(7, 13);
			}
		}

		else if (put == 'C' && smjer == '0' && GUI.guiMapa[20][26].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[20][25].getComponents().length == 0)
			{
				prethodneKoordinate.i = 20;
				prethodneKoordinate.j = 26;
				return new Koordinate(20, 25);
			}
		}

		else if (put == 'C' && smjer == '1' && GUI.guiMapa[21][26].getBackground() == GUI.DIGNUTA_RAMPA)
		{
			if (GUI.guiMapa[21][27].getComponents().length == 0)
			{
				prethodneKoordinate.i = 21;
				prethodneKoordinate.j = 26;
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

	synchronized Koordinate sledeciKorak()
	{

		if (trenutneKoordinate.j < 29 && ((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j + 1] == smjer || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j + 1] == 'x') && trenutneKoordinate.j + 1 != prethodneKoordinate.j)) // provjera desno
		{
			if (GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j + 1].getComponents().length == 1)
			{
				return trenutneKoordinate;
			}
			return (GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j + 1] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trenutneKoordinate.i, trenutneKoordinate.j + 1);
		}

		else if (trenutneKoordinate.j > 0 && ((GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j - 1] == smjer || GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j - 1] == 'x') && trenutneKoordinate.j - 1 != prethodneKoordinate.j)) // provjera lijevo
		{
			if (GUI.guiMapa[trenutneKoordinate.i][trenutneKoordinate.j - 1].getComponents().length == 1)
				return trenutneKoordinate;
			return (GUI.planGrada[trenutneKoordinate.i][trenutneKoordinate.j - 1] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trenutneKoordinate.i, trenutneKoordinate.j - 1);
		}

		else if (trenutneKoordinate.i > 0 && ((GUI.planGrada[trenutneKoordinate.i - 1][trenutneKoordinate.j] == smjer || GUI.planGrada[trenutneKoordinate.i - 1][trenutneKoordinate.j] == 'x') && trenutneKoordinate.i - 1 != prethodneKoordinate.i)) // provjera gore
		{
			if (GUI.guiMapa[trenutneKoordinate.i - 1][trenutneKoordinate.j].getComponents().length == 1)
				return trenutneKoordinate;
			return (GUI.planGrada[trenutneKoordinate.i - 1][trenutneKoordinate.j] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trenutneKoordinate.i - 1, trenutneKoordinate.j);
		}

		else if (trenutneKoordinate.i < 29 && ((GUI.planGrada[trenutneKoordinate.i + 1][trenutneKoordinate.j] == smjer || GUI.planGrada[trenutneKoordinate.i + 1][trenutneKoordinate.j] == 'x') && trenutneKoordinate.i + 1 != prethodneKoordinate.i)) // provjera dole
		{
			if (GUI.guiMapa[trenutneKoordinate.i + 1][trenutneKoordinate.j].getComponents().length == 1)
				return trenutneKoordinate;
			return (GUI.planGrada[trenutneKoordinate.i + 1][trenutneKoordinate.j] == 'x') ? new Koordinate(-2, -2) : new Koordinate(trenutneKoordinate.i + 1, trenutneKoordinate.j);
		}
		else
		{
			return new Koordinate(-1, -1);
		}
	}
	
}
