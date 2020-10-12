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

    public void print(){
        System.out.print(
                //"Coppia: { " +
                "{ key:" + getKey() +
                " value:" + getValue() + " }"
        );
    }

    /**
     * A null-safe compareTo version
     * @param obj
     * @return
     */
    public int compareTo(Coppia obj){
        if(obj == null) return -1;
        else return this.key.compareTo(obj.getKey());
    }
}
