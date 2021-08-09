import java.awt.Point;
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
			Scanner dat = new Scanner(new File("carConfigFile.txt"));
			dat.nextLine();
			for (int i = 0; i < 3 ; i++)
			{
				String redUFajlu = dat.nextLine();
				String[] podaciUReduFajla = redUFajlu.split(" ");
				maxBrVozila[i] = Integer.parseInt(podaciUReduFajla[1]);
				maxBrzine[i] = Double.parseDouble(podaciUReduFajla[2]);
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
				vozilaNaCekanju.get(randomPut).add(new Automobil(maxBrzine[randomPut], naziviPuteva[randomPut],"resource/car.png"));
		}
		else
		{
			if((GUI.trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut])
				vozilaNaCekanju.get(randomPut).add(new Kamion(maxBrzine[randomPut], naziviPuteva[randomPut],"resource/truck.png"));
		}
	}

	private void postaviNaMapu()
	{
		Point[][] kordStart = 
		{ 
			{ new Point(29, 8), new Point(21, 0) },   //A
			{ new Point(29, 14), new Point(0, 13) },  //B
			{ new Point(20, 29), new Point(29, 22) }	//C
		};
		
		for(int i=0; i<3; ++i) //na svaki put pokusa po jedno vozilo postaviti po jedno vozilo
		{
			if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '0' && GUI.guiMapa[ kordStart[i][0].x ][ kordStart[i][0].y ].getComponents().length == 0)
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo.x = kordStart[i][0].x;
				tmpVozilo.trKoo.y = kordStart[i][0].y;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trKoo.x][tmpVozilo.trKoo.y].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trKoo.x][tmpVozilo.trKoo.y].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
				tmpVozilo.start();
			}
			else if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '1' && GUI.guiMapa[ kordStart[i][1].x ][ kordStart[i][1].y ].getComponents().length == 0) //ako nema niko na pocetku smijera 1
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo.x = kordStart[i][1].x;
				tmpVozilo.trKoo.y = kordStart[i][1].y;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trKoo.x][tmpVozilo.trKoo.y].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trKoo.x][tmpVozilo.trKoo.y].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
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

