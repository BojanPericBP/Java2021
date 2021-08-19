import java.awt.Point;
import java.io.Serializable;
import java.util.Random;


public class Locomotive extends ElementOfComposition implements Serializable{

	private static final long serialVersionUID = 1L;
	static private int count = 0;
	
	char drive; //dizel-1, parni-2, elektricni-3
	String typeLocomotive;
	double power;
	int idLocomotive;
	
	public Locomotive(String _typeLocomotive) {
		drive = (char) (new Random().nextInt(2)+49);
		power = Math.random()*10;
		idLocomotive = count++;
		
		currentCoordinates = new Point(-1,-1);
		previousCoordinates = new Point(-1,-1);
		
		typeLocomotive = _typeLocomotive;
	}
}
