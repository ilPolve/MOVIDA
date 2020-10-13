package polverinifulgaro.datastructures;

public final class Coppia {
    Comparable key;
    Object value;

    Coppia(Comparable key, Object value){
        this.key = key;
        this.value = value;
    }

    Comparable getKey(){ return this.key; }
    Object getValue(){ return this.value; }

    /**
     * A null-safe compareTo version
     * @param obj to compare against this
     * @return -1 if obj == null or this.compareTo(obj) otherwise
     */
    public int compareTo(Coppia obj){
        if(obj == null) return -1;
        else return this.key.compareTo(obj.getKey());
    }
}
