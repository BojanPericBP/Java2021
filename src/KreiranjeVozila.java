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
	static ArrayList<ArrayList<Vozilo>> vozilaNaCekanju;
	double[] maxBrzine = new double[3];
	int[] maxBrVozila = new int[3];
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/KreiranjeVozila.log");
			Logger.getLogger(KreiranjeVozila.class.getName()).addHandler(handler);
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
			dat.nextLine(); //preskace header fajla
			
			for (int i = 0; i < 3 ; i++) //fajl ce uvijek imati 3 reda, jer ima 3 puta. Brojac i je indeks puta (za A je 0, za B je 1, za C je 2)
			{
				String redUFajlu = dat.nextLine().trim();
				String[] podaciUReduFajla = redUFajlu.split(",");
				maxBrzine[i] = Double.parseDouble(podaciUReduFajla[1].trim());
				int tmpMax = Integer.parseInt(podaciUReduFajla[2].trim());
				maxBrVozila[i] = (maxBrVozila[i] > tmpMax) ? maxBrVozila[i] : tmpMax ; //ako je postavljen manji max br vozila ignorise ga
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
				vozilaNaCekanju.get(randomPut).add(new Automobil(naziviPuteva[randomPut], maxBrzine[randomPut], "SLIKE/auto.png"));
		}
		else
		{
			if((GUI.trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut])
				vozilaNaCekanju.get(randomPut).add(new Kamion(maxBrzine[randomPut], naziviPuteva[randomPut],"SLIKE/kamion.png"));
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
		
		for(int i=0; i<3; ++i) //putevi indeksirani sa 0,1,2 za A,B,C puteve
		{
			if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '0' && GUI.guiMapa[ kordStart[i][0].i ][ kordStart[i][0].j ].getComponents().length == 0)
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trenutneKoordinate.i = kordStart[i][0].i;
				tmpVozilo.trenutneKoordinate.j = kordStart[i][0].j;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trenutneKoordinate.i][tmpVozilo.trenutneKoordinate.j].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trenutneKoordinate.i][tmpVozilo.trenutneKoordinate.j].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
				tmpVozilo.start();
			}
			else if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '1' && GUI.guiMapa[ kordStart[i][1].i ][ kordStart[i][1].j ].getComponents().length == 0) //ako nema niko na pocetku smijera 1
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trenutneKoordinate.i = kordStart[i][1].i;
				tmpVozilo.trenutneKoordinate.j = kordStart[i][1].j;
				GUI.trenutniBrVozilaNaPutevima[i]++; 
				GUI.guiMapa[tmpVozilo.trenutneKoordinate.i][tmpVozilo.trenutneKoordinate.j].add(new JLabel(new ImageIcon(tmpVozilo.putanjaSlike)));
				((JLabel)GUI.guiMapa[tmpVozilo.trenutneKoordinate.i][tmpVozilo.trenutneKoordinate.j].getComponents()[0]).setName(""+(long)tmpVozilo.trenutnaBrzina);
				tmpVozilo.start();
			}
		}
	}
	
	@Override
	public void run()
	{
		ucitajIzFajla(); //ucita nove podatke o max broju vozila na putevima i max brzinama
		kreiraj(); //kreira jedno vozilo
		postaviNaMapu(); //na svaki put pokusa postaviti po jedno vozilo, na jedan smijer
	}
}
