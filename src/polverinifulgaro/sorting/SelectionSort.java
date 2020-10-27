package polverinifulgaro.sorting;

import movida.commons.Person;
import polverinifulgaro.datastructures.ArrayOrdinato;
import polverinifulgaro.datastructures.IDizionario;

public class SelectionSort {

    public Object[] sort(IDizionario DB){
        if(DB == null) return null;
        else if(DB.getClass().equals(ArrayOrdinato.class)) return DB.values();
        else{
            Object[] notSortedKeys = DB.keys();
            Object[] notSortedValues = DB.values();
            
            for(int k = 0; k < notSortedKeys.length; k++){
                int m = k;
                for(int j = k+1; j < notSortedKeys.length; j++) {
                    if(((String)notSortedKeys[j]).compareTo((String)notSortedKeys[m]) < 0){
                        m = j;
                    }
                }
                if(m != k) {
                    Object tempKey = notSortedKeys[m];
                    Object tempValue = notSortedValues[m];
                    notSortedKeys[m] = notSortedKeys[k];
                    notSortedValues[m] = notSortedValues[k];
                    notSortedKeys[k] = tempKey;
                    notSortedValues[k] = tempValue;
                }
            }
            return notSortedValues;
        }
    }
    
    public Object sort(IDizionario DB, String filter){
        if(DB == null) return null;
        else{
        
        }
    }
    
}
