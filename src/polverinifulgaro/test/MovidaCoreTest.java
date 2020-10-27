package polverinifulgaro.test;

import movida.commons.MapImplementation;
import movida.commons.Movie;
import movida.commons.Person;
import polverinifulgaro.MovidaCore;

import java.io.File;

public class MovidaCoreTest {
    public static void main(String[] args) {
        MovidaCore core = new MovidaCore();
        core.loadFromFile(new File("/home/mattia/IdeaProjects/MOVIDA/docs/esempio-formato-dati.txt"));
        //core.setMap(MapImplementation.HashIndirizzamentoAperto);
        
        System.out.println("--- FILM ---");
        for(Movie x : core.getAllMovies()) System.out.println(x.getTitle());
        System.out.println("--- REGISTI ---");
        for(Person x : core.getAllPeople()) System.out.println(x.getName());
        
        System.out.println(core.getPersonByName("Al Pacino"));
        System.out.println(core.getMovieByTitle("Air Force One"));
    
        core.clear();
    
        System.out.println(core.getPersonByName("Al Pacino"));
        System.out.println(core.getMovieByTitle("Air Force One"));
        
        core.saveToFile(new File("/home/mattia/Desktop/tmp.txt"));
    }
}
