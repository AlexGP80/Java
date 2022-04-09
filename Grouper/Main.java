import java.util.*;
import java.io.*;

class Main {

  public static String Grouper(String str) {
    // code goes here
    TreeMap<Integer, ArrayList<Person>> peopleByPeriod = new TreeMap<Integer, ArrayList<Person>>();
    String [] strList = str.split(";");
    for (String personStr: strList) {
      String [] personFields = personStr.split("-");
      if (personFields.length == 2) {
        String name = personFields[0];

        // Parse the age
        int age = -1;
        try {
          age = Integer.parseInt(personFields[1]);
        } catch (NumberFormatException e) {
          continue; // process next person
        }

        Person person = new Person(name, age);

        // get a list of people already processed from the same age group
        ArrayList<Person> l = peopleByPeriod.get(person.getAgeGroup());
        if (l != null) {
          // There are other people in that age group
          // Add this person to that list
          l.add(person);
        } else {
          // it's the first person from this age group
          // create the list, add the person, insert the key and list in the map
          l = new ArrayList<Person>();
          l.add(person);
          peopleByPeriod.put(person.getAgeGroup(), l);
        }

      }
    }

    StringBuilder sb = new StringBuilder();

    for (Map.Entry<Integer, ArrayList<Person>> entry : peopleByPeriod.entrySet()) {
      int ageGroup = entry.getKey();
      ArrayList<Person> list = entry.getValue();
      Collections.sort(list);
      sb.append(ageGroup+":");
      for (Person person: list) {
        sb.append(person.toString() + ";");
      }
    }

    if (sb.length() > 0 && sb.charAt(sb.length()-1) == ';') {
      sb.setLength(sb.length() - 1);
    }

    return sb.toString();

  }

  public static void main (String[] args) {
    // keep this function call here
    Scanner s = new Scanner(System.in);
    System.out.print(Grouper(s.nextLine()));
  }


}



class Person implements Comparable<Person> {
  String name;
  int age;
  int ageGroup;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
    this.ageGroup = (age/10) * 10;
  }

  public String getName() {
    return this.name;
  }

  public int getAge() {
    return this.age;
  }

  public String toString() {
    return this.name+"-"+this.age;
  }

  public int getAgeGroup() {
    return this.ageGroup;
  }

  public int compareTo(Person anotherPerson) {
    int anotherPersonAge = anotherPerson.getAge();
    if (this.age < anotherPersonAge) {
      return -1;
    } else if (this.age > anotherPersonAge) {
      return 1;
    } else {
      // same age
      return this.name.compareTo(anotherPerson.getName());
    }
  }

}
