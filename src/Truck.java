import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Kamion extends Vehicle
{
	double nosivost;
	
	static 
	{
		try
		{
			Logger.getLogger(Kamion.class.getName()).addHandler(new FileHandler("Error logs/Kamion.log"));
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Kamion(double _maxBrizna,char _put, String _putanjaSlike) 
	{
		super(_maxBrizna, _put, _putanjaSlike);
		nosivost = new Random().nextInt(7000) + 2000;
	}

}
