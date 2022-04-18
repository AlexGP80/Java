import java.util.*;
import java.io.*;

class Operand {
    int operand;
    boolean isVar;

    Operand(int operand, boolean isVar) {
        this.operand = operand;
        this.isVar = isVar;
    }

    boolean isVar() {
        return this.isVar;
    }

    int getOperand() {
        return this.operand;
    }

}


class Main {

    public static String SystemofEquations(String str) {
        // code goes here

        if (str.isEmpty()) {
          // System.out.println("Empty");
          return "null";
        }
        String result = "";

        // 1. Separate each equation
        String [] equations = str.split("; ");

        HashMap<String, Integer> varMap = new HashMap<String, Integer>();
        HashMap<Integer, ArrayList<Operand>> equationMap = new HashMap<Integer, ArrayList<Operand>>();
        HashMap<Integer, Integer> solvedMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Boolean> processedMap = new HashMap<Integer, Boolean>();

        int varId = 0;

        // 2. For each equation, get the left part and store it into a HashMap
        for (String equation: equations) {
            String trimmedEquation = equation.replaceAll("\\s+", "");
            String [] parts = trimmedEquation.split("=");

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
                // System.out.println("Illegal var name or illegal characters in the right part)");
                return "null";
            }

            if (equationMap.containsKey(leftPart)) {
                // The same var cannot be on the left side of multiple equations
                // System.out.println("Duplicate var on the left side: " + leftPart);
                return "null";
            }

            Integer lVar = varMap.get(leftPart);
            if (lVar == null) {
                varMap.put(leftPart, varId);
                lVar = varId;
                varId++;
            }

            ArrayList<Operand> operandList = new ArrayList<Operand>();

            String[] operands = rightPart.split("\\^");
            int scalar = 0;
            boolean solved = true;
            for (String operand: operands) {
                if (isVar(operand)) {
                    solved = false;
                    Integer iOp = varMap.get(operand);
                    if (iOp == null) {
                        varMap.put(operand, varId);
                        iOp = varId;
                        varId++;
                    }
                    operandList.add(new Operand(iOp, true));
                } else {
                    int iOperand = 0;
                    try {
                        iOperand = Integer.parseInt(operand);
                    } catch (NumberFormatException e) {
                        // System.out.println("Wrong value parsing operand to integer: " + operand);
                        return "null";
                    }
                    scalar ^= iOperand;
                }
            }



            if (solved) {
                solvedMap.put(lVar, scalar);
            } else {
                if (scalar != 0) {
                    operandList.add(new Operand(scalar, false));
                }
                equationMap.put(lVar, operandList);
            }
            processedMap.put(lVar, false);
        }
        equations = null;

        int idX = varMap.get("x");
        int currentVarId = idX;
        int count = 0;
        Stack<Integer> varStack = new Stack<Integer>();
        int numSolved = solvedMap.size();
        boolean changedState = true;
        while (!solvedMap.containsKey(idX) && changedState) {
            if (count%1000==0) {
                // int solvedSize = solvedMap.size();
                // int totSize = solvedSize + equationMap.size();
                // System.out.println(solvedSize + "/" + totSize);
                changedState = false;
            }

            // System.out.println("\n\nITERATION: " + count);
            // System.out.println("currentVarId: " + currentVarId);
            // System.out.println("\nSOLVED VARS:");
            // for (Map.Entry<Integer, Integer> entry: solvedMap.entrySet()) {
            //     System.out.println(entry.getKey() + "=" + entry.getValue());
            // }
            // System.out.print("\nUNSOLVED VARS:");
            // for (Map.Entry<Integer, ArrayList<Operand>> entry: equationMap.entrySet()) {
            //     System.out.print("\n" + entry.getKey() + "=");
            //     for (Operand operand: entry.getValue()) {
            //         System.out.print("^"+operand.getOperand());
            //         if (operand.isVar()) {
            //             System.out.print("(V)");
            //         }
            //     }
            // }

            ArrayList<Operand> operands = equationMap.get(currentVarId);
            ArrayList<Operand> newOperands = new ArrayList<Operand>();
            int scalar = 0;
            boolean solved = true;
            for (Operand operand: operands) {
                if (operand.isVar()) {
                    Integer scalarValue = solvedMap.get(operand.getOperand());
                    if (scalarValue != null) {
                        scalar ^= scalarValue;
                    } else {
                        solved = false;
                        newOperands.add(operand);
                        varStack.push(operand.getOperand());
                    }
                } else {
                    scalar ^= operand.getOperand();
                }
            }
            if (solved) {
                solvedMap.put(currentVarId, scalar);
                equationMap.remove(currentVarId);
                changedState = true;
            } else {
                if (scalar != 0) {
                    newOperands.add(new Operand(scalar, false));
                }
                equationMap.put(currentVarId, newOperands);
            }

            count++;

            if (processedMap.get(currentVarId) == false) {
                processedMap.put(currentVarId, true);
                changedState = true;
            }

            if (varStack.size() > 0) {
                currentVarId = varStack.pop();
            } else {
                if (solvedMap.size() == numSolved) {
                    break;
                } else {
                    numSolved = solvedMap.size();
                    currentVarId = idX;
                }
            }
        }

        // System.out.println("\n\nITERATION: " + count);
        // System.out.println("currentVarId: " + currentVarId);
        // System.out.println("\nSOLVED VARS:");
        // for (Map.Entry<Integer, Integer> entry: solvedMap.entrySet()) {
        //     System.out.println(entry.getKey() + "=" + entry.getValue());
        // }
        // System.out.print("\nUNSOLVED VARS:");
        // for (Map.Entry<Integer, ArrayList<Operand>> entry: equationMap.entrySet()) {
        //     System.out.print("\n" + entry.getKey() + "=");
        //     for (Operand operand: entry.getValue()) {
        //         System.out.print("^"+operand.getOperand());
        //         if (operand.isVar()) {
        //             System.out.print("(V)");
        //         }
        //     }
        // }


        Integer solution = solvedMap.get(idX);
        if (solution == null) {
//          System.out.println("System of Equations without solution")
            return "null";
        }
        result += solution;

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
