import java.awt.Point;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class Wagon extends ElementOfComposition implements Serializable 
{
	private static final long serialVersionUID = 1L;
	static private int count = 0;
	int wagonLength;
	int IdWagon;
	
	
	public Wagon() {
		super("resource/traincar.png");
		IdWagon = count++;
		wagonLength = new Random().nextInt(4)+1;
		currentCoordinates = new Point();
		previousCoordinates = new Point();
	}

}