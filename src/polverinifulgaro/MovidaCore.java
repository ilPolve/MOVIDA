package polverinifulgaro;

import movida.commons.*;
import polverinifulgaro.datastructures.ArrayOrdinato;
import polverinifulgaro.datastructures.HashIndirizzamentoAperto;
import polverinifulgaro.datastructures.IDizionario;

import java.io.*;
import java.util.ArrayList;

public class MovidaCore implements /*IMovidaCollaborations, IMovidaSearch,*/ IMovidaConfig, IMovidaDB  {

    private IDizionario MoviesDB = null, PeopleDB = null;

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

            reader = new BufferedReader((new FileReader(f))); //FileReader usato per creare un BufferedReader e usare readLine()
            while((line = reader.readLine()) != null){
                count = 0;
                values = new String[5];

                for(MovieFields field : MovieFields.values()){
                    if(line.startsWith(field.toString() + ":")) { //L'enumerazione fields è usata per riconoscere l'attributo processato
                        if(values[field.ordinal()] != null){ //Attributo ripetuto == struttura non rispettata == eccezione sollevata
                            count = count + 1; //Un contatore segnala quanti attributi DIVERSI sono stati processati, se != 5 -> eccezione sollevata
                            values[field.ordinal()] = line.substring(field.toString().length() + 1).trim();
                        }else throw new MovidaFileException();
                    }
                }
                if(!line.isBlank() || count != 5) throw new MovidaFileException(); //Se c'è una linea bianca fuori contesto (cioè non separa due record) -> eccezione sollevata
                else {
                    ArrayList<Person> cast = new ArrayList<>();
                    //La linea contenente il cast è spezzata usando la virgola come separatore
                    for(String x : values[3].split(",")){
                        x = x.trim();
                        //L'esistenza di ogni persona del cast è verificata, se non esiste già viene aggiunta a PeopleDB
                        if(PeopleDB.search(x) == null) PeopleDB.insert(x, x);
                        cast.add(new Person(x));
                    }
                    //La stessa operazione è fatta con il direttore
                    if(PeopleDB.search(values[3].trim()) == null) PeopleDB.insert(values[3].trim(), values[3].trim());

                    //Se tutto è in ordine, finalmente l'oggetto Movie viene creato e inserito
                    Movie newMovie = new Movie(
                            values[0],
                            Integer.valueOf(values[1]),
                            Integer.valueOf(values[2]),
                            cast.toArray(new Person[0]),
                            new Person(values[4]));
                    MoviesDB.insert(values[0], newMovie);
                }
            }

            reader.close();
        }catch(Exception e){
            throw new MovidaFileException();
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
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    @Override
    public int countMovies() {
        return MoviesDB.keys().size();
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    @Override
    public int countPeople() {
        return PeopleDB.keys().size();
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
        return (Movie [])MoviesDB.values().toArray();
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    @Override
    public Person[] getAllPeople() {
        return (Person [])PeopleDB.values().toArray();
    }
}
