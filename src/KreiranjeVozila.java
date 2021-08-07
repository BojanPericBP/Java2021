import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class KreiranjeVozila extends TimerTask
{
	ArrayList<Vozilo> vozilaNaCekanjuA;
	ArrayList<Vozilo> vozilaNaCekanjuB;
	ArrayList<Vozilo> vozilaNaCekanjuC;
	ArrayList<ArrayList<Vozilo>> vozilaNaCekanju;

	double[] maxBrzine = new double[3];
	int[] maxBrVozila = new int[3];
	
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
	
	public KreiranjeVozila()
	{
		vozilaNaCekanjuA = new ArrayList<>();
		vozilaNaCekanjuB = new ArrayList<>();
		vozilaNaCekanjuC = new ArrayList<>();
		vozilaNaCekanju = new ArrayList<>(3);
		vozilaNaCekanju.add(vozilaNaCekanjuA);
		vozilaNaCekanju.add(vozilaNaCekanjuB);
		vozilaNaCekanju.add(vozilaNaCekanjuC);
	}

	public void ucitajIzFajla()
	{
		try
		{
			Scanner dat = new Scanner(new File("vozila_config.txt"));
			dat.nextLine();
			for (int i = 0; i < 3 ; i++)
			{
				String redUFajlu = dat.nextLine().trim();
				String[] podaciUReduFajla = redUFajlu.split(",");
				maxBrzine[i] = Double.parseDouble(podaciUReduFajla[1].trim());
				maxBrVozila[i] = Integer.parseInt(podaciUReduFajla[2].trim());
			}
			dat.close();
		}
		catch (Exception e)
		{
			Logger.getLogger(KreiranjeVozila.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
		}
	}
	
	public void kreiraj()
	{
		char[] naziviPuteva = {'A', 'B', 'C'};
		
		Random rand = new Random();
		int randomPut = rand.nextInt(3);
		
		if(rand.nextBoolean()) //TRUE: kreira se auto, FALSE: kreira se kamion
		{
			if((GUI.trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut]) 
				vozilaNaCekanju.get(randomPut).add(new Automobil(maxBrzine[randomPut], naziviPuteva[randomPut],"car.png"));
		}
		else
		{
			if((GUI.trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut])
				vozilaNaCekanju.get(randomPut).add(new Kamion(maxBrzine[randomPut], naziviPuteva[randomPut],"kamion.png"));
		}
	}

	private void postaviNaMapu()
	{
		Koordinate[][] kordStart = 
		{ 
			{ new Koordinate(29, 8), new Koordinate(21, 0) },   //A
			{ new Koordinate(29, 14), new Koordinate(0, 13) },  //B
			{ new Koordinate(20, 29), new Koordinate(29, 22) }	//C
		};
		
		for(int i=0; i<3; ++i) //na svaki put pokusa po jedno vozilo postaviti po jedno vozilo
		{
			if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '0' && GUI.guiMapa[ kordStart[i][0].i ][ kordStart[i][0].j ].getComponents().length == 0)
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo.i = kordStart[i][0].i;
				tmpVozilo.trKoo.j = kordStart[i][0].j;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trKoo.i][tmpVozilo.trKoo.j].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trKoo.i][tmpVozilo.trKoo.j].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
				tmpVozilo.start();
			}
			else if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '1' && GUI.guiMapa[ kordStart[i][1].i ][ kordStart[i][1].j ].getComponents().length == 0) //ako nema niko na pocetku smijera 1
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo.i = kordStart[i][1].i;
				tmpVozilo.trKoo.j = kordStart[i][1].j;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trKoo.i][tmpVozilo.trKoo.j].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trKoo.i][tmpVozilo.trKoo.j].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
				tmpVozilo.start();
			}
		}
	}
	
	@Override
	public void run()
	{
		ucitajIzFajla();
		kreiraj();
		postaviNaMapu();
	}
}

