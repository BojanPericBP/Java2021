
public class PutnickiVagonRestoran extends PutnickiVagon
{
	private static final long serialVersionUID = 1L;
	static private int count = 0;
	String opis;
	
	public PutnickiVagonRestoran() 
	{
		opis="Ovo je opis vagona "+count++;
	}

}
