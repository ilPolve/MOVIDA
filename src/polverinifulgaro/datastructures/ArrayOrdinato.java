package polverinifulgaro.datastructures;

public class ArrayOrdinato implements IDizionario {

    private Coppia[] array;
    private int usedSlot; //Numero degli elementi attualmente presenti(usato principalmente per ridimensionare l'array)

    public ArrayOrdinato() {
        array = new Coppia[1];
        usedSlot = 0;
    }

    /**
     * Implementazione dell'operazione di inserimento
     * @param key del nuovo elemento inserito
     * @param value valore
     */
    @Override
    public void insert(Comparable key, Object value) {
        int i = 0;
        Coppia[] temp;

        if (key == null) throw new IllegalArgumentException("The provided key is invalid! Key: " + key);
        else {
            //Array size doubled when "load factor" > 0.5
            if(usedSlot + 1 >= array.length / 2) temp = new Coppia[array.length * 2];
            else temp = new Coppia[array.length];

            //Creation of the new item (useful to obtain a null-safe compareTo())
            Coppia newItem = new Coppia(key, value);

            //Copy of the elements of array whose key <= newItem.key into temp
            while(newItem.compareTo(array[i]) > 0 && i < usedSlot) i = i + 1;
            System.arraycopy(array, 0, temp, 0, i);
            //New pair is placed in the right position inside tmp and usedSlot is updated accordingly
            temp[i] = newItem;
            usedSlot = usedSlot + 1;
            //The remaining portion of the array is now copied into tmp and then temp is assigned to array
            System.arraycopy(array, i, temp, i+1, array.length - i - 1);
            array = temp;
        }
    }

    /**
     * Implementation of delete operation
     * @param key of the element to delete
     */
    @Override
    public void delete(Comparable key) {
        Coppia[] temp;

        if (key == null) throw new IllegalArgumentException("The provided key is invalid! Key: " + key);
        else {
            //Array size halved when "load factor" < 0.25
            if(usedSlot - 1 < array.length / 4) temp = new Coppia[array.length / 2];
            else temp = new Coppia[array.length];

            //Exact position of the element is determined by a search
            Integer target = dicSearch(key, 0, usedSlot);

            //Everything before target index is copied into temp
            System.arraycopy(array, 0, temp, 0, target);
            //Every item from the last to the element next to target index is shifted to left by one
            for(int i = target; i < usedSlot - 1; i++) temp[i] = array[i + 1];
            //The last item is set to null (not mandatory) and usedSlot is updated
            temp[usedSlot] = null;
            usedSlot = usedSlot - 1;

            array = temp;
        }
    }

    /**
     * Search operation is implemented by a binary search because is the quickest way to find an element into a sorted
     * array of element. The actual search operation is realized by dicSearch making possible to change binary search
     * with any other algorithm of your please.
     * @param key is the key object to look for into the dictionary
     * @return null if given key is not in the dictionary or the Coppia object if it is present
     */
    @Override
    public Object search(Comparable key) {
        if(key == null) throw new IllegalArgumentException();
        if(usedSlot <= 0) return null;
        else {
            Integer target = dicSearch(key, 0, usedSlot);
            return (target == null) ? null : array[target];
        }
    }

    /**
     * Actual implementation of a binary search.
     * @param key is the key object to look for into the dictionary
     * @param start is the first index of the current portion of the array
     * @param end is the last index of the current portion of the array
     * @return null if given key is not in the dictionary or the position of the element with @param key as key.
     */
    private Integer dicSearch(Comparable key, Integer start, Integer end){
        if(start > end) return null;
        Integer middle = Math.floorDiv(start + end, 2);
        if(array[middle].getKey().compareTo(key) == 0) return middle;
        else if(array[middle].getKey().compareTo(key) > 0) return dicSearch(key, start, middle-1);
        else return dicSearch(key, middle+1, end);
    }

    /**
     * Utility created to visualize the database in a JSON format (quite...)
     */
    public void print() {
        for (int i = 0; i < usedSlot; i++) {
            if(array[i] != null) System.out.println(array[i].getKey() + " " + array[i].getValue());
            else System.out.println("null");
        }
    }
}
