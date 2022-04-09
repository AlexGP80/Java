import java.util.*;
import java.io.*;

class Main {

  public static String StringChallenge(String str, int num) {
    String myToken = "qmg2dfu06e1";
    StringBuilder sb = new StringBuilder();
    // code goes here
    final int num_chars = 26;
    int shift_num_chars = num % num_chars;
    for (int i = 0; i < str.length(); i++) {
      int ascii = (int)str.charAt(i);
      int new_ascii = ascii;
      if (ascii >= 65 && ascii <= 90) {
        // Mayúsculas
        new_ascii += shift_num_chars;
        if (new_ascii > 90) {
          new_ascii = new_ascii - 90 + 65;
        }
      } else if (ascii >= 97 && ascii <= 122) {
        // Minúsculas
        new_ascii += shift_num_chars;
        if (new_ascii > 122) {
          new_ascii = new_ascii - 122 + 97;
        }
      }
      sb.append(Character.toChars(new_ascii));

    }
    return replaceEveryFourthCharWithUnderscore(sb.toString()+myToken);
  }

  public static String replaceEveryFourthCharWithUnderscore(String str) {
    String result = "";
    for (int i=0; i<str.length(); i+=4) {
      if (i+3 < str.length()) {
        result += str.substring(i, i+3) + "_";
      } else {
        result += str.substring(i, str.length());
      }
    }
    return result;
  }

  public static void main (String[] args) {
    // keep this function call here
    Scanner s = new Scanner(System.in);
    System.out.print(StringChallenge(s.nextLine()));
  }

}
