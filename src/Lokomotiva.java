import java.awt.Point;
import java.io.Serializable;
import java.util.Random;


public class Lokomotiva extends elementKompozicije implements Serializable{

	static private int count = 0;
	private static final long serialVersionUID = 1L;
	char pogon; //dizel-1, parni-2, elektricni-3
	String typeTrain;
	double snaga;
	int idLokomotive;
	
	public Lokomotiva(String tipLokomotive) {
		pogon = (char) (new Random().nextInt(2)+49);
		snaga = Math.random()*10;
		idLokomotive = count++;
		
		trKoo = new Point(-1,-1);
		preKoo = new Point(-1,-1);
		
		typeTrain = tipLokomotive;
	}
}
