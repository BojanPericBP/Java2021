import java.util.Random;

public class Kamion extends Vozilo{

	double nosivost;
	
	public Kamion(double _maxBrizna,char _put) {
		super(_maxBrizna, _put);
		nosivost = new Random().nextInt(200) + 40;
	}

}
