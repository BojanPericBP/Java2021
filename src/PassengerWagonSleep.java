import java.util.Random;

public class PassengerWagonSleep extends Wagon{
	private static final long serialVersionUID = 1L;
	int seats;
	
	public PassengerWagonSleep() {
		super();
		seats = new Random().nextInt(53)+12;
	}

}
