
public class PutnickiVagonRestoran extends PutnickiVagon{
	private static final long serialVersionUID = 1L;
	String opis;
	static private int count = 0;
	
	public PutnickiVagonRestoran() {
		opis="Ovo je opis vagona "+count++;
	}

}
