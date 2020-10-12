package polverinifulgaro.test;

import polverinifulgaro.MovidaCore;

import java.io.File;

public class MovidaCoreTest {
    public static void main(String[] args) {
        MovidaCore core = new MovidaCore();
        core.loadFromFile(new File("/home/mattia/IdeaProjects/MOVIDA/src/movida/commons/esempio-formato-dati.txt"));
    }
}
