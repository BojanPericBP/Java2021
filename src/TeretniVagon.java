import java.util.Random;

public class TeretniVagon extends Vagon 
{
	private static final long serialVersionUID = 1L;
	double maxNosivost;
	
	public TeretniVagon() 
	{
		super(false);
		maxNosivost = new Random().nextDouble()*1000+1;
	}

}
