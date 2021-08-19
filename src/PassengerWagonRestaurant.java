
public class PassengerWagonRestaurant extends PassengerWagon{
	private static final long serialVersionUID = 1L;
	String description;
	static private int count = 0;
	
	public PassengerWagonRestaurant() {
		description="Ovo je opis vagona "+count++;
	}

}
