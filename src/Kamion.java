import java.io.IOException;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Kamion extends Vozilo
{
	double nosivost;
	static FileHandler handler;
	
	static 
	{
		try
		{
			handler = new FileHandler("Error logs/Kamion.log");
			Logger.getLogger(Kamion.class.getName()).addHandler(handler);
		}
		catch (SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Kamion(double maxBriznaArg,char putArg, String putanjaSlikeArg) 
	{
		super(maxBriznaArg, putArg, putanjaSlikeArg);
		nosivost = new Random().nextInt(200) + 40;
	}

}
