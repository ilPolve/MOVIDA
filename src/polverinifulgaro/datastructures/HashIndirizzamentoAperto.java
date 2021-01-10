package polverinifulgaro.datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

public class HashIndirizzamentoAperto implements IDizionario {
    
    private enum specialValue {
        DELETED
    }
    
    private Object specialVal = specialValue.DELETED;
    private Coppia[] array;
    private double loadFactor;
    private int used;
    private int nearestPrime = 1;
    
    public HashIndirizzamentoAperto(){
        array = new Coppia[1];
        Arrays.fill(array, null);
        loadFactor = 0.0;
        used = 0;
    }
    
    /**
     * @param key chiave del nuovo elemento da inserire
     * @param value valore del nuovo elemento da inserire
     *
     * La funzione inserisce un nuovo elemento nella hash table usando la funzione _insert().
     * Prima di un nuovo inserimento viene controllato il fattore di carico e se quest'ultimo è maggiore di 0.75,
     * la dimensione della hash table viene raddoppiata e tutti gli elementi pre-esistenti re-inseriti.
     */
    @Override
    public void insert(Comparable key, Object value) {
        if (key == null) throw new IllegalArgumentException("The provided key is invalid!");
        else {
            //Dimensione dell'array raddoppiata quando "fattore di carico" > 0.5
            if(loadFactor > 0.75) {
                Coppia[] temp = new Coppia[array.length * 2];
                Arrays.fill(temp, null);
                //I vecchi valori vengono re-inseriti nella nuova tabella ricalcolando l'indirizzo hash
                for(Coppia coppia : array){
                    if(coppia != null && !coppia.equals(specialVal)) _insert(coppia.getKey(), coppia.getValue(), temp);
                }
                
                array = temp;
                loadFactor = Math.floorDiv(used, array.length);
                nearestPrime = getNearestPrime(array.length);
            }
            
            //Inserimento nuovo elemento
            _insert(key, value, array);
        }
    }
    
    /**
     * @param key la chiave dell'elemento target da eliminare
     *
     * La funzione localizza innanzitutto l'indice dell'elemento con chiave key usando _indexSearch(key).
     * Se il risultato è diverso da -1 (key non trovata), l'elemento di chiave target viene eliminato copiandoci
     * all'interno il valore speciale DELETED.
     *
     * Fatto questo, il fattore di carico viene valutato e se è inferiore a 0.25, la dimensione della hash table viene
     * dimezzata e gli elementi pre-esistenti vengono re-inseriti.
     */
    @Override
    public void delete(Comparable key) {
        if (key == null) throw new IllegalArgumentException("The provided key is invalid!");
        else {
            //Ricerca dell'indice dell'elemento di chiave key
            int target = _search(key);
            
            //Se l'elemento viene trovato, allora viene eliminato
            if(target >= 0) {
                array[target] = (Coppia) specialVal;
                used -= 1;
                loadFactor = Math.floorDiv(used, array.length);
            }else throw new NoSuchElementException("Key not found");
            
            //Dimensione dell'array dimezzata quando "fattore di carico" < 0.25
            if(loadFactor < 0.25) {
                Coppia[] temp = new Coppia[array.length / 2];
                Arrays.fill(temp, null);
                //I vecchi valori vengono re-inseriti nella nuova tabella ricalcolando l'indirizzo hash
                for(Coppia coppia : array){
                    if(coppia != null && !coppia.equals(specialVal)) _insert(coppia.getKey(), coppia.getValue(), temp);
                }
                
                array = temp;
                loadFactor = Math.floorDiv(used, array.length);
                nearestPrime = getNearestPrime(array.length);
            }
        }
    }
    
    /**
     * @param key la chiave dell'oggetto cercato
     * @return l'oggetto cercato, se viene trovato. null altrimenti.
     *
     * La funzione utilizza _indexSearch(key) per localizzare l'elemento di chiave key.
     * Se l'elemento viene trovato ( _indexSearch(key) >= 0 ), viene restituito.
     * Altrimenti viene restituito null.
     */
    @Override
    public Object search(Comparable key) {
        if (key == null) throw new IllegalArgumentException("The provided key is invalid!");
        else{
            int result = _search(key);
            return (result != -1) ? array[result] : null;
        }
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
        used = 0;
        loadFactor = 0.0;
        Arrays.fill(array, null);
    }
    
    /**
     * @param key chiave del nuovo valore da inserire
     * @param value valore del nuovo elemento da inserire
     * @param struct struttura dati nel quale inserire l'elemento
     *
     * Para-funzione di inserimento realizzata allo scopo di evitare duplicazioni di codice.
     * La specifica di un terzo parametro si è resa necessaria per distinguere un normale inserimento in hash table da
     * un inserimento fatto in occasione di un raddoppiamento/dimezzamento della tabella.
     */
    private void _insert(Comparable key, Object value, Coppia[] struct) {
        //Creazione del nuovo elemento
        Coppia newItem = new Coppia(key, value);
        
        //Inserimento dell'elemento usando un hashing doppio per trovare la prima posizione libera
        int attemp;
        for(attemp = 0; attemp < struct.length; attemp++) {
            
            int hashIndex = doubleHashing(key, attemp);
            
            if(struct[hashIndex] == null || struct[hashIndex].equals(specialVal)) {
                struct[hashIndex] = newItem;
                used += 1;
                loadFactor = Math.floorDiv(used, struct.length);
                return;
            }
        }
    }
    
    /**
     * @param key chiave dell'oggetto target da trovare
     * @return indice dell'oggetto target, se trovato. -1 altrimenti.
     */
    private int _search(Comparable key) {
        if (key == null) throw new IllegalArgumentException("The provided key is invalid!");
        else{
            int j;
            for(int i = 0; i < array.length; i++){
                j = doubleHashing(key, i); //TODO: investigare sul ruolo del valore DELETED
                if(array[j] != null && !array[j].equals(specialVal) && array[j].getKey().equals(key)) return j;
            }
        }
        return -1;
    }
    
    /**
     * @param key la chiave dell'elemento di cui deve essere calcolato l'indirizzo hash
     * @param i il tentativo
     * @return l'indirizzo della prima cella libera nella tabella in cui inserire l'elemento
     *         o l'indirizzo a cui si trova un determinato elemento
     *
     * Per realizzare un hashing doppio, ovviamente, necessitiamo di due funzioni di hashing: la prima darà l'indirizzo
     * della prima ispezione e la seconda darà l'offset delle successive.
     *
     * - h1(key) realizza la prima funzione di hashing: viene calcolato l'hash code della chiave, preso il valore
     *   assoluto e restituito il resto della divisione tra questo valore e la dimensione della tabella
     * - h2(key) realizza la seconda funzione.
     *   Se per la prima non ci sono stati molti dubbi, sulla seconda ho passato qualche ora a documentarmi in rete.
     *   Dalle mie ricerche, è saltato fuori che una popolare seconda funzione di hash è PRIME - (key % PRIME), dove
     *   PRIME è un numero primo < dimensione della tabella e key è l'hash code della chiave.
     *   Per trovare il più grande numero primo < dimensione della tabella, entra in gioco la funzione getNearestPrime()
     */
    private int doubleHashing(Comparable key, int i) {
        return (h1(key) + i * h2(key)) % array.length;
    }
    
    private int h1(Comparable key) {
        return Math.abs(key.hashCode() % array.length);
    }
    
    private int h2(Comparable key) {
        return Math.abs(nearestPrime - key.hashCode() % nearestPrime);
    }
    
    /**
     * @param max il numero che farà da upper bound alla ricerca del più grande numero primo
     * @return il numero primo più grande strettamente minore di max.
     * Ispirato al Crivello di Sundaram.
     * Dalla lista dei numeri da 1 a max vengono rimossi (marcati) tutti quelli nella forma i + j + 2ij dove:
     * 1) i e j sono naturali tali che 1 <= i <= j
     * 2) i + j + 2ij <= max
     *
     * I numeri rimasti vengono moltiplicati per due e, al risultato, viene aggiunto 1: si ottiene così la lista
     * dei numeri dispari e primi <= 2*max + 2
     *
     * Considerazione #1
     * f(i, j) = i + j + 2ij
     * f(i, j+1) = i + j + 1 + 2i(j+1)
     *           = i + j + 1 + 2ij + 2i
     *           = f(i, j) + 2i + 1
     */
    private int getNearestPrime(int max) {
        boolean[] marked = new boolean[max/2];
        int i, j;
        
        if(max < 4) return max;
        Arrays.fill(marked, false);
        
        for (i = 1; i < max/2; i++) {
            j = i;
            while ((i + j + 2 * i * j) < max/2) {
                marked[i + j + 2 * i * j] = true;
                j += 2 * i + 1;
            }
        }
        
        for(i = max/2 - 1; i > 0; i--) {
            if(!marked[i]) break;
        }
        return i * 2 + 1;
    }
}
