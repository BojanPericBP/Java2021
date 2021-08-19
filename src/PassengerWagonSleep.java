import java.util.Random;

public class PassengerWagonSleep extends Vagon{
	private static final long serialVersionUID = 1L;
	int brojMjesta;
	
	public PassengerWagonSleep() {
		super(false);
		brojMjesta = new Random().nextInt(31)+6;
	}

}
