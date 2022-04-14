import java.util.*;
import java.io.*;

class Main {

  public static String SystemofEquations(String str) {
    // code goes here

    if (str.isEmpty()) {
      System.out.println("Empty");
      return "null";
    }
    String result = "";

    // 1. Separate each equation
    String [] equations = str.split("; ");

    HashMap<String, String> equationMap = new HashMap<String, String>();

    // 2. For each equation, get the left part and store it into a HashMap
    for (String equation: equations) {
      String trimmedEquation = equation.replaceAll("\\s+", "");
      String [] parts = trimmedEquation.split("=");
      // String [] parts = equation.split("=");
      if (parts.length != 2) {
        // Reject equations with more than one "=" sign or no "=" sign at all
        // System.out.println("Equation with more than 1 equal sign (or no equal sign at all)");
        return "null";
      }
      String leftPart = parts[0];
      String rightPart = parts[1];
      if (!containsOnlyLetters(leftPart) || !containsOnlyLettersNumbersAndXOR(rightPart)) {
        // discard equations where variables contain illegal characters (not letters)
        // and discard equations where the right part contains illegal characters
        System.out.println("Illegal var name or illegal characters in the right part)");
        return "null";
      }
      if (equationMap.containsKey(leftPart)) {
        // The same var cannot be on the left side of multiple equations
        System.out.println("Duplicate var on the left side: " + leftPart);
        return "null";
      } else {
        equationMap.put(leftPart, rightPart);
      }
    }

    // 3. get the X equation
    String xRightPart = equationMap.get("x");
    // we allow the system to process all variables at least (num_variables) number of times
    for (int i = 0; i < equationMap.size(); i++) {
      for (String var: equationMap.keySet()) {
        String varRightPart = equationMap.get(var);
        xRightPart = xRightPart.replaceAll("\\^"+var+"\\^", "^"+varRightPart+"^");
        xRightPart = xRightPart.replaceAll("^"+var+"\\^", varRightPart+"^");
        xRightPart = xRightPart.replaceAll("\\^"+var+"$", "^"+varRightPart);
      }
    }

    // System.out.println(xRightPart);
    if (doNotContainVars(xRightPart) == false) {
      System.out.println("System of equations without solution");
      return "null";
    }

    int xValue = 0;
    String [] operands = xRightPart.split("\\^");
    for (String operand: operands) {
      int iOperand = 0;

      try {
        iOperand = Integer.parseInt(operand);
      } catch (NumberFormatException e) {
        System.out.println("Wrong value parsing operand to integer: " + operand);
        return "null";
      }

      if (xValue == 0) {
        xValue = iOperand;
      } else {
        xValue = xValue ^ iOperand;
      }
    }

    result += xValue;

    return result;
  }

  public static boolean isVar(String str) {
    return containsOnlyLetters(str);
  }

  public static boolean doNotContainVars(String str) {
    char[] chars = str.toCharArray();

    for (char c : chars) {
      if (Character.isLetter(c)) {
        return false;
      }
    }

    return true;
  }

  public static boolean containsOnlyLettersNumbersAndXOR(String str) {
    char[] chars = str.toCharArray();

    for (char c : chars) {
      if (!(Character.isLetter(c)||Character.isDigit(c)||c=='^')) {
        return false;
      }
    }

    return true;
  }

  public static boolean containsOnlyLetters(String str) {
    char[] chars = str.toCharArray();

    for (char c : chars) {
      if (!Character.isLetter(c)) {
        return false;
      }
    }

    return true;
  }

  public static void main (String[] args) {
    // keep this function call here
    Scanner s = new Scanner(System.in);
    System.out.print(SystemofEquations(s.nextLine()));
  }

}
