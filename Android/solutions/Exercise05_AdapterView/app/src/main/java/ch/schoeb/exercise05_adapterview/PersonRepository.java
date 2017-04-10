package ch.schoeb.exercise05_adapterview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonRepository {

    private static List<Person> persons = new ArrayList<>();

    static {
        persons.add(new Person(1, "Stefan", "Zürich"));
        persons.add(new Person(2, "Peter", "Basel"));
        persons.add(new Person(3, "Frank", "Bern"));
        persons.add(new Person(4, "Hans", "Mailand"));
        persons.add(new Person(5, "Jakob", "Madrid"));
        persons.add(new Person(6, "Florian", "London"));
        persons.add(new Person(7, "Roger", "Grabs"));
        persons.add(new Person(8, "Jonas", "Amsterdam"));
        persons.add(new Person(9, "Valon", "Moskau"));
        persons.add(new Person(10, "Didier", "Berlin"));
        persons.add(new Person(11, "Lara", "Barcelona"));
    }

    public static void addRandomPerson(){
        persons.add(0,new Person(persons.size(),randomString(9),randomString(9)));
    }

    private static String randomString(final int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }
        return sb.toString();
    }

    public static List<Person> getPersons() {
        return persons;
    }

}
