package polverinifulgaro;

import movida.commons.*;
import polverinifulgaro.datastructures.ArrayOrdinato;
import polverinifulgaro.datastructures.Coppia;
import polverinifulgaro.datastructures.HashIndirizzamentoAperto;
import polverinifulgaro.datastructures.IDizionario;

import java.io.*;
import java.util.ArrayList;

public class MovidaCore implements /*IMovidaCollaborations,*/ IMovidaSearch, IMovidaConfig, IMovidaDB  {

    private IDizionario MoviesDB;
    private IDizionario PeopleDB;
    private MapImplementation currentMapImplementation;
    private SortingAlgorithm currentSortAlgo;

    public MovidaCore(){
        MoviesDB = new ArrayOrdinato();
        PeopleDB = new ArrayOrdinato();
        currentMapImplementation = MapImplementation.ArrayOrdinato;
        currentSortAlgo = SortingAlgorithm.SelectionSort;
    }
    /**
     * Seleziona l'algoritmo di ordinamento.
     * Se l'algortimo scelto non � supportato dall'applicazione
     * la configurazione non cambia
     *
     * @param a l'algoritmo da selezionare
     * @return <code>true</code> se la configurazione � stata modificata, <code>false</code> in caso contrario
     */
    @Override
    public boolean setSort(SortingAlgorithm a) {
        if(!a.equals(SortingAlgorithm.SelectionSort) && !a.equals(SortingAlgorithm.QuickSort)) return false;
        else{
            if(currentMapImplementation.toString().equals(a.toString())) return false;
            else currentSortAlgo = a;
            
            return true;
        }
    }

    /**
     * Seleziona l'implementazione del dizionario
     * <p>
     * Se il dizionario scelto non � supportato dall'applicazione
     * la configurazione non cambia
     *
     * @param m l'implementazione da selezionare
     * @return <code>true</code> se la configurazione � stata modificata, <code>false</code> in caso contrario
     */
    @Override
    public boolean setMap(MapImplementation m) {
        if(!m.equals(MapImplementation.ArrayOrdinato) && !m.equals(MapImplementation.HashIndirizzamentoAperto)) return false;
        else{
            IDizionario MoviesDB_tmp = null;
            IDizionario PeopleDB_tmp = null;

            if(currentMapImplementation.equals(m)) return false;

            if(currentMapImplementation.equals(MapImplementation.ArrayOrdinato)){
                MoviesDB_tmp = new HashIndirizzamentoAperto();
                PeopleDB_tmp = new HashIndirizzamentoAperto();
                currentMapImplementation = MapImplementation.HashIndirizzamentoAperto;
            }else{
                MoviesDB_tmp = new ArrayOrdinato();
                PeopleDB_tmp = new ArrayOrdinato();
                currentMapImplementation = MapImplementation.ArrayOrdinato;
            }

            for(Movie x : getAllMovies()) MoviesDB_tmp.insert(x.getTitle(), x);
            for(Person y: getAllPeople()) PeopleDB_tmp.insert(y.getName(), y);
            MoviesDB = MoviesDB_tmp;
            PeopleDB = PeopleDB_tmp;

            return true;
        }
    }

    /**
     * Ricerca film per titolo.
     * <p>
     * Restituisce i film il cui titolo contiene la stringa
     * <code>title</code> passata come parametro.
     * <p>
     * Per il match esatto usare il metodo <code>getMovieByTitle(String s)</code>
     * <p>
     * Restituisce un vettore vuoto se nessun film rispetta il criterio di ricerca.
     *
     * @param title titolo del film da cercare
     * @return array di film
     */
    @Override
    public Movie[] searchMoviesByTitle(String title) {
        ArrayList<Movie> result = new ArrayList<>();
        for(Movie x : getAllMovies()){
            if(x.getTitle().contains(title)) result.add(x);
        }
        return (Movie[]) result.toArray();
    }

    /**
     * Ricerca film per anno.
     * <p>
     * Restituisce i film usciti in sala nell'anno
     * <code>anno</code> passato come parametro.
     * <p>
     * Restituisce un vettore vuoto se nessun film rispetta il criterio di ricerca.
     *
     * @param year anno del film da cercare
     * @return array di film
     */
    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        return new Movie[0];
    }

    /**
     * Ricerca film per regista.
     * <p>
     * Restituisce i film diretti dal regista il cui nome � passato come parametro.
     * <p>
     * Restituisce un vettore vuoto se nessun film rispetta il criterio di ricerca.
     *
     * @param name regista del film da cercare
     * @return array di film
     */
    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        return new Movie[0];
    }

    /**
     * Ricerca film per attore.
     * <p>
     * Restituisce i film a cui ha partecipato come attore
     * la persona il cui nome � passato come parametro.
     * <p>
     * Restituisce un vettore vuoto se nessun film rispetta il criterio di ricerca.
     *
     * @param name attore coinvolto nel film da cercare
     * @return array di film
     */
    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        return new Movie[0];
    }

    /**
     * Ricerca film pi� votati.
     * <p>
     * Restituisce gli <code>N</code> film che hanno
     * ricevuto pi� voti, in ordine decrescente di voti.
     * <p>
     * Se il numero di film totali � minore di N restituisce tutti i film,
     * comunque in ordine.
     *
     * @param N numero di film che la ricerca deve resistuire
     * @return array di film
     */
    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        return new Movie[0];
    }

    /**
     * Ricerca film pi� recenti.
     * <p>
     * Restituisce gli <code>N</code> film pi� recenti,
     * in base all'anno di uscita in sala confrontato con l'anno corrente.
     * <p>
     * Se il numero di film totali � minore di N restituisce tutti i film,
     * comunque in ordine.
     *
     * @param N numero di film che la ricerca deve resistuire
     * @return array di film
     */
    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        return new Movie[0];
    }

    /**
     * Ricerca gli attori pi� attivi.
     * <p>
     * Restituisce gli <code>N</code> attori che hanno partecipato al numero
     * pi� alto di film
     * <p>
     * Se il numero di attori � minore di N restituisce tutti gli attori,
     * comunque in ordine.
     *
     * @param N numero di attori che la ricerca deve resistuire
     * @return array di attori
     */
    @Override
    public Person[] searchMostActiveActors(Integer N) {
        return new Person[0];
    }

    /**
     * Carica i dati da un file, organizzato secondo il formato MOVIDA (vedi esempio-formato-dati.txt)
     * <p>
     * Un film � identificato in modo univoco dal titolo (case-insensitive), una persona dal nome (case-insensitive).
     * Semplificazione: non sono gestiti omonimi e film con lo stesso titolo.
     * <p>
     * I nuovi dati sono aggiunti a quelli gi� caricati.
     * <p>
     * Se esiste un film con lo stesso titolo il record viene sovrascritto.
     * Se esiste una persona con lo stesso nome non ne viene creata un'altra.
     * <p>
     * Se il file non rispetta il formato, i dati non sono caricati e
     * viene sollevata un'eccezione.
     *
     * @param f il file da cui caricare i dati
     * @throws MovidaFileException in caso di errore di caricamento
     */

    /**
     * This enumeration contains the attributes that a Movie is supposed to have.
     * It is used to check the the file structure.
     */
    private final String[] allowedPatterns = {"Title:[ \\w||.:?!#]+", "Year: \\d{4}", "Director: [\\w .]+", "Cast: [\\w ,]+[^,]$", "Votes: \\d*", "\\s*"};

    @Override
    public void loadFromFile(File f) {
        BufferedReader reader;
        boolean isEOF = false;
        
        try{
            reader = new BufferedReader((new FileReader(f)));
        }catch(FileNotFoundException e){
            throw new MovidaFileException();
        }
        
        String[] record = new String[allowedPatterns.length];
        int i = 0, index;
        while(!isEOF){
            index = i % (allowedPatterns.length);
            try{
                record[index] = reader.readLine().trim();
            }catch(NullPointerException | IOException e){
                if(e.getClass().equals(NullPointerException.class)) isEOF = true;
                else e.printStackTrace();
            }
            if(!record[index].matches(allowedPatterns[index])) throw new MovidaFileException();
            else if(index == (allowedPatterns.length - 1) || isEOF){
                //Elinazione dell'intestazione della riga
                for(int j = 0; j < record.length; j++) record[j] = record[j].substring(record[j].indexOf(":") + 1).trim();
                    
                ArrayList<Person> cast = new ArrayList<>();
                //La linea contenente il cast è spezzata usando la virgola come separatore
                String[] values = record[3].split(",");
                for(int k = 0; k < values.length; k++){
                    values[k] = values[k].trim();
                    //L'esistenza di ogni persona del cast è verificata, se non esiste già viene aggiunta a PeopleDB
                    if(PeopleDB.search(values[k]) == null) PeopleDB.insert(values[k], new Person(values[k]));
                    cast.add(new Person(values[k]));
                }
                //La stessa operazione è fatta con il direttore
                if(PeopleDB.search(record[2]) == null) PeopleDB.insert(record[2], new Person(record[2]));
    
                //Se tutto è in ordine, finalmente l'oggetto Movie viene creato e inserito
                Movie newMovie = new Movie(record[0], Integer.valueOf(record[1]), Integer.valueOf(record[4]), cast.toArray(Person[]::new), new Person(record[2]));
                if(MoviesDB.search(record[0]) == null) MoviesDB.insert(record[0], newMovie);
            }
            i += 1;
        }
    }

    /**
     * Salva tutti i dati su un file.
     * <p>
     * Il file � sovrascritto.
     * Se non � possibile salvare, ad esempio per un problema di permessi o percorsi,
     * viene sollevata un'eccezione.
     *
     * @param f il file su cui salvare i dati
     * @throws MovidaFileException in caso di errore di salvataggio
     */
    @Override //TODO: duplica salvataggi...
    public void saveToFile(File f){
        if(f == null || !f.canWrite()) throw new MovidaFileException();
        else{
            BufferedWriter writer;
            try{
                writer = new BufferedWriter((new FileWriter(f, true)));
            
                for(Object x : MoviesDB.values()){
                    writer.write("Title: " + ((Movie) x).getTitle() + "\n");
                    writer.write("Year: " + ((Movie) x).getYear() + "\n");
                    writer.write("Director: " + ((Movie) x).getDirector().getName() + "\n");
                    String cast = "";
                    for(Person y : ((Movie) x).getCast()) cast = cast + (y.getName() + ", ");
                    writer.write("Cast: " + cast.substring(0, cast.lastIndexOf(",")) + "\n");
                    writer.write("Votes: " + ((Movie) x).getVotes() + "\n");
                    writer.newLine();
                }
                
                writer.close();
            }catch(IOException e){
                throw new MovidaFileException();
            }
        }
    }

    /**
     * Cancella tutti i dati.
     * <p>
     * Sar� quindi necessario caricarne altri per proseguire.
     */
    @Override
    public void clear() {
        this.PeopleDB.clear();
        this.MoviesDB.clear();
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    @Override
    public int countMovies() {
        return MoviesDB.keys().length;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    @Override
    public int countPeople() {
        return PeopleDB.keys().length;
    }

    /**
     * Cancella il film con un dato titolo, se esiste.
     *
     * @param title titolo del film
     * @return <code>true</code> se il film � stato trovato e cancellato,
     * <code>false</code> in caso contrario
     */
    @Override
    public boolean deleteMovieByTitle(String title) {
        if(MoviesDB.search(title) == null || title == null) return false;
        else{
            MoviesDB.delete(title);
            return true;
        }
    }

    /**
     * Restituisce il record associato ad un film
     *
     * @param title il titolo del film
     * @return record associato ad un film
     */
    @Override
    public Movie getMovieByTitle(String title) {
        Coppia retVal = (Coppia) MoviesDB.search(title);
        return (retVal == null) ? null : (Movie) retVal.getValue();
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    @Override
    public Person getPersonByName(String name) {
        Coppia retVal = (Coppia) PeopleDB.search(name);
        return (retVal == null) ? null : (Person) retVal.getValue();
    }

    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    @Override
    public Movie[] getAllMovies() {
        ArrayList<Movie> retVal = new ArrayList<>();
        for(Object x : MoviesDB.values()) retVal.add((Movie) x);
        return retVal.toArray(new Movie[0]);
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    @Override
    public Person[] getAllPeople() {
        ArrayList<Person> retVal = new ArrayList<>();
        for(Object x : PeopleDB.values()) retVal.add((Person) x);
        return retVal.toArray(new Person[0]);
    }
}
