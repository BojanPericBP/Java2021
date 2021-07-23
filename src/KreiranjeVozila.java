import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;

public class KreiranjeVozila extends TimerTask
{
	ArrayList<Vozilo> vozilaNaCekanjuA;
	ArrayList<Vozilo> vozilaNaCekanjuB;
	ArrayList<Vozilo> vozilaNaCekanjuC;
	ArrayList<ArrayList<Vozilo>> vozilaNaCekanju;
	
	int[] trenutniBrVozilaNaPutevima = {10, 20, 30}; //TODO skontat gdje ovo premjestiti
	double[] maxBrzine = new double[3];
	int[] maxBrVozila = new int[3];
	
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
			Scanner dat = new Scanner(new File("src/vozila_config.txt"));
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
			e.printStackTrace();
		}
	}
	
	public void kreiraj()
	{
		char[] naziviPuteva = {'A', 'B', 'C'};
		
		Random rand = new Random();
		int randomPut = rand.nextInt(3);
		
		if(rand.nextBoolean())
		{
			if((trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut]) 
				vozilaNaCekanju.get(randomPut).add(new Automobil(rand.nextDouble()*maxBrzine[randomPut], naziviPuteva[randomPut])); //TODO: gdje se odredjuje smijer vozila i ostali atributi?
		}
		else
		{
			if((trenutniBrVozilaNaPutevima[randomPut] + vozilaNaCekanju.get(randomPut).size()) < maxBrVozila[randomPut])
				vozilaNaCekanju.get(randomPut).add(new Kamion(rand.nextDouble()*maxBrzine[randomPut], naziviPuteva[randomPut]));
		}
	}

	private void postaviNaMapu()
	{
		Koordinate[][] kordStart = 
		{ 
			{ new Koordinate(29, 8), new Koordinate(21, 0) }, //A
			{ new Koordinate(29, 14), new Koordinate(0, 13) }, //B
			{ new Koordinate(20, 29), new Koordinate(29, 22) }	//C
		};
		
		for(int i=0; i<3; ++i) 
		{
			if(vozilaNaCekanju.get(i).size()>0 && vozilaNaCekanju.get(i).get(0).smjer == '0' && GUI.guiMapa[ kordStart[i][0].i ][ kordStart[i][0].j ].getComponents().length == 0)
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo = kordStart[i][0];
				trenutniBrVozilaNaPutevima[i]++; 
				tmpVozilo.start();
			}
			else if(vozilaNaCekanju.get(i).size()>0 && GUI.guiMapa[ kordStart[i][1].i ][ kordStart[i][1].j ].getComponents().length == 0) //ako nema niko na pocetku smijera 1
			{
				Vozilo tmpVozilo = vozilaNaCekanju.get(i).remove(0);
				tmpVozilo.trKoo = kordStart[i][1];
				trenutniBrVozilaNaPutevima[i]++; 
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


//KORISTENJE U MAIN-U

//Timer timer = new Timer();
//timer.schedule(new KreiranjeVozila, 2000);
//timer.cancel();


