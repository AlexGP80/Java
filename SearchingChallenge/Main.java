import java.util.*;
import java.io.*;

class Main {

  public static String searchingChallenge(String str) {
    // code goes here
    String myToken = "qmg2dfu06e1";
    if (str.isEmpty()) {
      return replaceEveryFourthCharWithUnderscore("-1" + myToken);
    }
    String[] words = str.split(" ");
    int [] lens = new int [words.length]; // int default value is 0 in java
    for (int i = 0; i < words.length; i++) {
      lens[i] = numRepeatedCharacters(words[i]);
    }
    int pos_max = getFirstMax(lens);
    if (pos_max == -1) {
      return replaceEveryFourthCharWithUnderscore("-1" + myToken);
    }
    String result =  words[pos_max] + myToken;
    return replaceEveryFourthCharWithUnderscore(result);
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

  public static int getFirstMax(int [] arr) {
    int max = 0;
    int pos_max = -1;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
        pos_max = i;
      }
    }
    if (max == 1) {
      return -1;
    }
    return pos_max;
  }

  public static int numRepeatedCharacters(String str) {
    int max = 1;
    for (int i = 0; i<str.length(); i++) {
      int current = 1;
      for (int j = i+1; j<str.length(); j++) {
        if (str.charAt(j) == str.charAt(i))  {
          current++;
        }
      }
      if (current > max) {
        max = current;
      }
    }
    return max;
  }

  public static void main (String[] args) {
    try {
      File file = new File("./input.txt");
      BufferedReader br = new BufferedReader(new FileReader(file));

      String str;
      while ((str = br.readLine()) != null) {
        System.out.println(searchingChallenge(str));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
