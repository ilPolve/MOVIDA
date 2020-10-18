package polverinifulgaro.sorting;

import polverinifulgaro.datastructures.Coppia;
import polverinifulgaro.datastructures.IDizionario;

public class SelectionSort {

    public Object[] sort(IDizionario DB, String filter){
        Object[] notSortedKeys = DB.keys();
        Object[] notSortedValues = DB.values();

        for(int k = 0; k < notSortedKeys.length; k++){
            int m = k;
            for(int j = k+1; j < notSortedKeys.length; j++) {
                if (((Comparable) notSortedKeys[j]).compareTo(notSortedKeys[m]) < 0) {
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

        Object[] retVal = new Object[notSortedKeys.length];
        for(int i = 0; i < notSortedKeys.length; i++){
            retVal[i] = new Coppia((Comparable) notSortedKeys[i], notSortedValues[i]);
        }
        return retVal;
    }

}
