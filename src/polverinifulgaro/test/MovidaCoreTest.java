package polverinifulgaro.test;

import movida.commons.MapImplementation;
import movida.commons.Movie;
import movida.commons.Person;
import polverinifulgaro.MovidaCore;

import java.io.File;
import java.util.Arrays;

public class MovidaCoreTest {
    public static void main(String[] args) {
        MovidaCore core = new MovidaCore();
        System.out.println("--- CARICAMENTO DATI ---");
        core.loadFromFile(new File("/home/mattia/IdeaProjects/MOVIDA/docs/esempio-formato-dati.txt"));
    
        //System.out.println("--- CAMBIO STRUTTURA ---");
        core.setMap(MapImplementation.HashIndirizzamentoAperto);
        
        System.out.println("--- FILM ---");
        for(Movie x : core.getAllMovies()) System.out.println(x.getTitle());
        System.out.println("--- REGISTI ---");
        for(Movie x : core.getAllMovies()) System.out.println(x.getDirector().getName());
        System.out.println("--- ATTORI ---");
        for(Person x : core.getAllPeople()) System.out.println(x.getName());
    
        System.out.println("--- GET TEST ---");
        System.out.println("Expected: \"Al Pacino\", Got: " + core.getPersonByName("Al Pacino").getName());
        System.out.println("Expected: \"Air Force One\", Got: " + core.getMovieByTitle("Air Force One").getTitle());
    
        core.clear();
    
        System.out.println("--- GET TEST after clear() ---");
        System.out.println("Expected: null, Got: " + core.getPersonByName("Al Pacino"));
        System.out.println("Expected: null, Got: " + core.getMovieByTitle("Air Force One"));
    
       // core.loadFromFile(new File("/home/mattia/IdeaProjects/MOVIDA/docs/esempio-formato-dati.txt"));
        //core.saveToFile(new File("/home/mattia/IdeaProjects/MOVIDA/docs/prova-salvataggio.txt"));
    }
}
