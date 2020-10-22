package polverinifulgaro.datastructures;

public final class Coppia {
    private Comparable key;
    private Object value;

    public Coppia(Comparable key, Object value){
        this.key = key;
        this.value = value;
    }

    public Comparable getKey(){ return this.key; }
    public Object getValue(){ return this.value; }

    /**
     * Versione null-safe di compareTo tra coppie
     * @param obj da comparare a questo
     * @return -1 se obj == null oppure this.compareTo(obj) altrimenti
     */
    public int compareTo(Coppia obj){
        if(obj == null) return -1;
        else return this.key.compareTo(obj.getKey());
    }
    
    @Override
    public String toString(){
        return "Coppia{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
