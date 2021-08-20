import java.util.Random;

public class FreightWagon extends Wagon {
	private static final long serialVersionUID = 1L;
	double maxCapacity;
	
	public FreightWagon() {
		super();
		maxCapacity = new Random().nextDouble()*1000+1;
	}

}
