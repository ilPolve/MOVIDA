package polverinifulgaro.datastructures;

public interface IDizionario {

    public void insert(Comparable key, Object value);

    public void delete(Comparable key);

    public Object search(Comparable key);
}
