package polverinifulgaro.datastructures;

public class ArrayOrdinato implements IDizionario {

    private Coppia[] array;
    private int used; //Numero delle posizioni attualmente occupate(usato principalmente per ridimensionare l'array)

    public ArrayOrdinato() {
        array = new Coppia[1];
        used = 0;
    }

    /**
     * Operazione di inserimento
     * @param key del nuovo elemento inserito (e.g. titolo -> film, cognome -> persona)
     * @param value valore del nuovo elemento (e.g. oggetto Movie -> film, nome -> persona)
     */
    @Override
    public void insert(Comparable key, Object value) {
        int i = 0;
        Coppia[] temp;

        if (key == null) throw new IllegalArgumentException("The provided key is invalid! Key: " + key);
        else {
            //Dimensione dell'array raddoppiata quando "fattore di carico" > 0.5
            if(used+1 >= array.length/2) temp = new Coppia[array.length*2];
            else temp = new Coppia[array.length];

            //Creazione del nuovo elemento(utile per ottenere una versione null-safe di compareTo())
            Coppia newItem = new Coppia(key, value);

            //Tutti gli elementi con key <= newItem.key vengono copiati in temp
            while(newItem.compareTo(array[i]) >= 0 && i < used) i = i + 1;
            System.arraycopy(array, 0, temp, 0, i);
            
            //Il nuovo oggetto viene posizionato in temp e UsedSlot viene aggiornato
            temp[i] = newItem;
            used = used + 1;
            //La rimanente porzione dell'array viene ricopiata in temp che viene, infine, assegnato a array
            System.arraycopy(array, i, temp, i+1, array.length - i - 1);
            array = temp;
        }
    }

    /**
     * Operazione di eliminazione
     * @param key dell'elemento da eliminare
     */
    @Override
    public void delete(Comparable key) {
        Coppia[] temp;

        if (key == null) throw new IllegalArgumentException("The provided key is invalid! Key: " + key);
        else {
            //La dimensione dell'array viene dimezzata quando "fattore di carico" < 0.25
            if(used - 1 < array.length / 4) temp = new Coppia[array.length / 2];
            else temp = new Coppia[array.length];

            //La posizione esatta dell'elemento viene determinata con una ricerca
            Integer target = dicSearch(key, 0, used);

            //Tutto quello che c'è prima dell'indice target viene copiato così com'è in temp
            System.arraycopy(array, 0, temp, 0, target);
            //Ogni elemento, a partire dall'ultimo fino a quello di posto target + 1, viene spostato di una posizione a sinistra
            for(int i = target; i < used - 1; i++) temp[i] = array[i + 1];
            //Anche se non è obbligatorio, l'ultima posizione di temp è posta uguale a null e UsedSlot è aggionato
            temp[used] = null;
            used = used - 1;

            array = temp;
        }
    }

    /**
     * L'operazione di ricerca è implementata dalla funzione dicSearch() che, in questo caso, implementa una ricerca binaria.
     * Nel caso in cui si voglia cambiare algoritmo, basterà cambiare solamente dicSearch().
     * @param key dell'oggetto da cercare
     * @return null se key == null, l'array è vuoto o se target == null, l'oggetto Coppia di indice target altrimenti
     */
    @Override
    public Coppia search(Comparable key) {
        if(key == null) throw new IllegalArgumentException();
        if(used <= 0) return null;
        else {
            Integer target = dicSearch(key, 0, used - 1);
            return (target == null) ? null : array[target];
        }
    }
    
    /**
     * Implementazione della ricerca binaria.
     * @param key dell'oggetto che stiamo cercando
     * @param start è l'indice iniziale della porzione di array in esame
     * @param end è l'indice finale della porzione di array in esame
     * @return indice dell'array in cui si trova key; null se la chiave non è stata trovata o.
     */
    private Integer dicSearch(Comparable key, int start, int end){
        if(start > end) return null;
        int middle = Math.floorDiv(start + end, 2);
        if(array[middle].getKey().compareTo(key) == 0) return middle;
        else if(array[middle].getKey().compareTo(key) > 0) return dicSearch(key, start, middle-1);
        else return dicSearch(key, middle+1, end);
    }
    
    /**
     * @return array di tutte le chiavi
     */
    @Override
    public Object[] keys() {
        Object[] retVal = new Object[used];
        for(int i = 0; i < used; i++) retVal[i] = array[i].getKey() ;
        return retVal;
    }
    
    /**
     * @return array di tutti i valori
     */
    @Override
    public Object[] values() {
        Object[] retVal = new Object[used];
        for(int i = 0; i < used; i++) retVal[i] = array[i].getValue();
        return retVal;
    }
    
    @Override
    public void clear(){
        this.used = 0;
    }
}
