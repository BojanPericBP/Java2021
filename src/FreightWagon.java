import java.util.Random;

public class FreightWagon extends Vagon {
	private static final long serialVersionUID = 1L;
	double maxCapacity;
	
	public FreightWagon() {
		super(false);
		maxCapacity = new Random().nextDouble()*1000+1;
	}

}
