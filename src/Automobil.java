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
	
	public Automobil(double _maxBrzina,char _put, String _putanjaSlike) 
	{
		super(_maxBrzina,_put, _putanjaSlike);
		brojVrata = new Random().nextInt(6)+2;
	}

}
