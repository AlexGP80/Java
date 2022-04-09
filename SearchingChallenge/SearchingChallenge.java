import java.util.*;
import java.io.*;

class Main {

  public static String SearchingChallenge(String str) {
    // code goes here
    String[] words = str.split(" ");
    for (String word: words) {
      System.out.println(word);
    }
    return str;
  }

  public static void main (String[] args) {
    // keep this function call here
    Scanner s = new Scanner(System.in);
    System.out.print(SearchingChallenge(s.nextLine()));
  }

}
