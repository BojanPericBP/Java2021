import java.util.Random;

public class Automobil extends Vozilo{

	int brojVrata;
	
	public Automobil(double _maxBrzina,char _put) {
		super(_maxBrzina,_put);
		brojVrata = new Random().nextInt(6)+2;
	}

}
