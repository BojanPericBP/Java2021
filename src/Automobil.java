import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Automobil extends Vozilo
{
	int brojVrata;
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Automobili.log");
			Logger.getLogger(Automobil.class.getName()).addHandler(handler);
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Automobil(char put, double maxBrzina, String path) 
	{
		super(maxBrzina, put);
		smjer = (char) ('0' + new Random().nextInt(2));
		if(smjer == '1')
			putanjaSlike = "SLIKE/auto_desno.png";
		else 
			putanjaSlike = "SLIKE/auto_lijevo.png";
		brojVrata = new Random().nextInt(4)+2;
	}

}
