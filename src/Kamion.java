import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Kamion extends Vozilo
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
	
	public Kamion(double _maxBrizna,char _put) 
	{
		super(_maxBrizna, _put);
		nosivost = new Random().nextInt(200) + 40;
	}

}
