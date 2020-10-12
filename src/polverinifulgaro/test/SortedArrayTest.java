package polverinifulgaro.test;

import polverinifulgaro.datastructures.ArrayOrdinato;

public class SortedArrayTest {
    public static void main(String[] args) {
        ArrayOrdinato testDB = new ArrayOrdinato();
        testDB.insert(1, "a");
        //testDB.print();
        //System.out.println("---------");
        testDB.insert(Integer.valueOf("3"), "b");
        //testDB.print();
        //System.out.println("---------");
        testDB.insert(Integer.valueOf("8"), "c");
        //testDB.print();
        //System.out.println("---------");
        testDB.insert(Integer.valueOf("2"), "d");
        //testDB.print();
        //System.out.println("---------");
        testDB.insert(Integer.valueOf("6"), "e");
        //testDB.print();
        //System.out.println("---------");
        testDB.print();
        System.out.println("---------");
       /* ((Coppia)testDB.search(8)).print();
        ((Coppia)testDB.search(8)).print();
        ((Coppia)testDB.search(6)).print();
        ((Coppia)testDB.search(3)).print();
        //((Coppia)testDB.search(5)).print();*/
        testDB.delete(9);
        testDB.print();

    }
}
