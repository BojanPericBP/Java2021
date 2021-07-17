import java.util.Random;

public class PutnickiVagonSpavaci extends Vagon{

	int brojMjesta;
	
	public PutnickiVagonSpavaci() {
		super(false);
		brojMjesta = new Random().nextInt(31)+6;
	}

}
