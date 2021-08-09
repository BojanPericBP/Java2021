import java.io.Serializable;
import java.util.Objects;

public class Koordinate implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
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
	public int hashCode() {
		return Objects.hash(i, j);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final Koordinate other = (Koordinate) obj;
		return i == other.i && j == other.j;
	}
	
	@Override
	public String toString() 
	{
		return "("+i+","+j+")";
	}

}
