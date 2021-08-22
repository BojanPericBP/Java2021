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
	
	public Kamion(double maxBriznaArg, char putArg) 
	{
		super(maxBriznaArg, putArg);
		smjer = (char) ('0' + new Random().nextInt(2));
		if(smjer == '1')
			putanjaSlike = "SLIKE/kamion_desni.png";
		else 
			putanjaSlike = "SLIKE/kamion_lijevi.png";
		nosivost = new Random().nextInt(200) + 40;
	}

}
