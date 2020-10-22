package polverinifulgaro.datastructures;

import java.util.Collection;
public interface IDizionario {
    public void insert(Comparable key, Object value);

    public void delete(Comparable key);

    public Object search(Comparable key);

    public Object[] keys();

    public Object[] values();
	
	void clear();
}
