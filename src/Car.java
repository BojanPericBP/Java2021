import java.util.Random;

public class Car extends Vehicle
{
	int doors;

	public Car(double _maxSpeed, char _way, String _imgPath) 
	{
		super(_maxSpeed,_way, _imgPath);
		doors = new Random().nextInt(4)+2;
	}
}
