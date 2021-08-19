import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Automobil extends Vehicle
{
	int brojVrata;
	
	static 
	{
		try
		{
			Logger.getLogger(Automobil.class.getName()).addHandler(new FileHandler("Error logs/Automobili.log"));
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Automobil(double _maxBrzina, char _put, String _putanjaSlike) 
	{
		super(_maxBrzina,_put, _putanjaSlike);
		brojVrata = new Random().nextInt(6)+2;
	}
}
