import java.util.Random;

public class PutnickiVagonSpavaci extends Vagon{
	private static final long serialVersionUID = 1L;
	int brojMjesta;
	
	public PutnickiVagonSpavaci() {
		super(false);
		brojMjesta = new Random().nextInt(31)+6;
	}

}
