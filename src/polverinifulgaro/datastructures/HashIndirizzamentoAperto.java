package polverinifulgaro.datastructures;

import java.util.ArrayList;
import java.util.Collection;

public class HashIndirizzamentoAperto implements IDizionario {
    
    private enum specialValue {
        DELETED
    }
    
    private Object specialVal = specialValue.DELETED;
    private Coppia[] array;
    private double loadFactor;
    private int used;
    
    public HashIndirizzamentoAperto(){
        array = new Coppia[1];
        array[0] = null;
        loadFactor = 0.0;
        used = 0;
    }
    
    @Override
    public void insert(Comparable key, Object value) {
        if (key == null) throw new IllegalArgumentException("The provided key is invalid! Key: " + key);
        else {
            //Dimensione dell'array raddoppiata quando "fattore di carico" > 0.5 e vecchi valori copiati nel nuovo
            if(loadFactor >= 0.5) {
                Coppia[] temp = new Coppia[array.length * 2];
                for(int j = 0; j < array.length; j++){
                    if(array[j] != null && !array[j].equals(specialValue.DELETED)) temp[j] = array[j];
                    else if(array[j] == null) temp[j] = null;
                    else temp[j] = (Coppia) specialVal;
                }
                for(int j = array.length; j < temp.length; j++) temp[j] = null;
                array = temp;
                loadFactor = Math.floorDiv(used, array.length);
            }
            
            //Creazione del nuovo elemento
            Coppia newItem = new Coppia(key, value);
        
            //Calcolo la funzione hash() della nuova coppia da inserire
            int hashIndex;
            if(array.length <= 1) hashIndex = 0;
            else hashIndex = (newItem.hashCode()) % array.length;
            
            //Il nuovo oggetto viene posizionato nell'array
            while(array[hashIndex] != null) hashIndex = (hashIndex + 1) % (array.length - 1);
            array[hashIndex] = newItem;
            used += 1;
            loadFactor = Math.floorDiv(used, array.length);
        }
    }

    @Override
    public void delete(Comparable key) {

    }

    @Override
    public Object search(Comparable key) {
        return null;
    }
    
    /**
     * @return array delle chiavi contenute nella tabella hash
     */
    @Override
    public Object[] keys() {
        if(used == 0) return null;
        else{
            Collection retVal = new ArrayList();
            for(Coppia coppia : array) if(coppia != null && !coppia.equals(specialValue.DELETED)) retVal.add(coppia.getKey());
            return retVal.toArray();
        }
    }
    
    /**
     * @return array dei valori contenuti nella tabella hash
     */
    @Override
    public Object[] values() {
        if(used == 0) return null;
        else{
            Collection retVal = new ArrayList();
            for(Coppia coppia : array) if(coppia != null && !coppia.equals(specialValue.DELETED)) retVal.add(coppia.getValue());
            return retVal.toArray();
        }
    }
    
    /**
     * Questo metodo svuota la struttura dati
     */
    @Override
    public void clear(){
        this.used = 0;
        this.loadFactor = 0.0;
        for(int i = 0; i < this.array.length; i++) array[i] = null;
    }
}
