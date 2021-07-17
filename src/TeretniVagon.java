import java.util.Random;

public class TeretniVagon extends Vagon {

	double maxNosivost;
	public TeretniVagon() {
		super(false);
		maxNosivost = new Random().nextDouble()*1000+1;
	}

}
