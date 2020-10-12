package polverinifulgaro;

import movida.commons.*;
import polverinifulgaro.datastructures.ArrayOrdinato;
import polverinifulgaro.datastructures.HashIndirizzamentoAperto;
import polverinifulgaro.datastructures.IDizionario;

import java.io.*;

public class MovidaCore implements /*IMovidaCollaborations, IMovidaSearch,*/ IMovidaConfig, IMovidaDB  {

    private IDizionario MoviesDB = null, PeopleDB = null;
    private int MoviesCount = 0, PeopleCount = 0; //TODO: movies&people can be added in other ways not only with loadFromFile

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
        return false;
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

            if(MoviesDB.getClass().toString().equals(m.toString())) return false;

            if(MoviesDB.getClass().toString().equals("ArrayOrdinato")){
                MoviesDB_tmp = new HashIndirizzamentoAperto();
                PeopleDB_tmp = new HashIndirizzamentoAperto();

            }else{
                MoviesDB_tmp = new ArrayOrdinato();
                PeopleDB_tmp = new ArrayOrdinato();
            }

            for(Movie x : getAllMovies()) MoviesDB_tmp.insert(x.getTitle(), x);
            for(Person y: getAllPeople()) PeopleDB_tmp.insert(y.getName(), y);
            MoviesDB = MoviesDB_tmp;
            PeopleDB = PeopleDB_tmp;

            return true;
        }
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
    private enum MovieFields {
        Title,
        Year,
        Votes,
        Cast,
        Director,
    }

    @Override
    public void loadFromFile(File f) {
        BufferedReader reader = null;

        try{
            String line, values[];
            int count;

            reader = new BufferedReader((new FileReader(f))); //FileReader wrapped into a BufferedReader to use readLine()
            while((line = reader.readLine()) != null){
                count = 0;
                values = new String[5];

                for(MovieFields field : MovieFields.values()){
                    if(line.startsWith(field.toString() + ":")) { //Enum fields is used to recognize which attribute is being processed
                        if(values[field.ordinal()] != null){ //If an attribute is readed twice an exception is thrown
                            count = count + 1; //A count is manteined to check if one or more attributes are missing
                            values[field.ordinal()] = line.substring(field.toString().length() + 1).trim();
                        }else throw new MovidaFileException();
                    }
                }
                if(!line.isBlank() || count != 5) throw new MovidaFileException(); //Another check on file structure (blank line between records)
                else {
                    //If everything is okay, finally we create the movie record
                    Movie newMovie = new Movie(
                            values[0],
                            Integer.valueOf(values[1]),
                            Integer.valueOf(values[2]),
                            values[3], //TODO: slice the cast line, check if every person already exists or not, insert into Person db?
                            new Person(values[4])); //TODO: check  if director already exist
                    MoviesDB.insert(values[0], newMovie);
                }
            }
        }catch(Exception e){
            throw new MovidaFileException();
        }finally {
            //reader.close();
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
    @Override
    public void saveToFile(File f) {

    }

    /**
     * Cancella tutti i dati.
     * <p>
     * Sar� quindi necessario caricarne altri per proseguire.
     */
    @Override
    public void clear() {
        this.MoviesDB = this.PeopleDB = null;
        this.MoviesCount = this.PeopleCount = 0;
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    @Override
    public int countMovies() {
        return this.MoviesCount;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    @Override
    public int countPeople() {
        return this.PeopleCount;
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
        return (Movie)MoviesDB.search(title);
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    @Override
    public Person getPersonByName(String name) {
        return (Person)PeopleDB.search(name);
    }

    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    @Override
    public Movie[] getAllMovies() {
        Movie[] allMovies = new Movie[this.MoviesCount];
        //for(Movie item : ) //TODO: come cazzo ottengo tutti i film? Metodo ausiliario?
        return allMovies;
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    @Override
    public Person[] getAllPeople() {
        //TODO: stesso problema di tutti i film
        return new Person[0];
    }
}
