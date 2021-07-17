
public class Koordinate {

	int i;
	int j;
	
	public Koordinate(Koordinate _Koordinate) {
		i = _Koordinate.i;
		j = _Koordinate.j;
	}
	
	public Koordinate(int x, int y)
	{
		i = x;
		j = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Koordinate)
		{
			if(i == ((Koordinate)obj).i && j == ((Koordinate)obj).j)
			{
				return true;
			}
		}
		return false;
	}

}
