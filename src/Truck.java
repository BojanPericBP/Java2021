import java.util.Random;

public class Truck extends Vehicle
{
	double capacity;
	
	
	public Truck(double _maxSpeed,char _way, String _imgPath) 
	{
		super(_maxSpeed, _way, _imgPath);
		capacity = new Random().nextInt(7000) + 2000;
	}
}
