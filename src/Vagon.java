import java.awt.Point;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class Vagon extends ElementOfComposition implements Serializable 
{
	private static final long serialVersionUID = 1L;
	static private int count = 0;
	int duzinaVagona;
	int IDVagona;
	boolean jePosebneNamjene;
	
	static 
	{
		try
		{
			Logger.getLogger(Vagon.class.getName()).addHandler(new FileHandler("Error logs/Vagon.log"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Vagon(boolean _jePosebneNamjene) {
		super("resource/traincar.png");
		IDVagona = count++;
		duzinaVagona = new Random().nextInt(4)+1;
		jePosebneNamjene = _jePosebneNamjene;
		currentCoordinates = new Point();
		previousCoordinates = new Point();
	}

}