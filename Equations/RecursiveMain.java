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

class EmptyInputException extends Exception {
    public EmptyInputException(String errorMessage) {
        super(errorMessage);
    }
}

class IncorrectEquationException extends Exception {
    public IncorrectEquationException(String errorMessage) {
        super(errorMessage);
    }
}

class XNotFoundException extends Exception {
    public XNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

class VariableNotFoundException extends Exception {
    public VariableNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

class SystemOfEquations {
    HashMap<String, Integer> varMap = new HashMap<String, Integer>();
    HashMap<Integer, ArrayList<Operand>> equationMap = new HashMap<Integer, ArrayList<Operand>>();
    HashMap<Integer, Integer> solvedMap = new HashMap<Integer, Integer>();
    HashMap<Integer, Boolean> processedMap = new HashMap<Integer, Boolean>();

    SystemOfEquations(String str) throws Exception {
        if (str.isEmpty()) {
          // System.out.println("Empty");
          throw new EmptyInputException("Empty input, cannot create System of Equations.");
        }

        // 1. Separate each equation
        String [] equations = str.split("; ");

        int varId = 0;

        // 2. For each equation, get the left part and store it into a HashMap
        for (String equation: equations) {
            String trimmedEquation = equation.replaceAll("\\s+", "");
            String [] parts = trimmedEquation.split("=");

            if (parts.length != 2) {
                // Reject equations with more than one "=" sign or no "=" sign at all
                throw new IncorrectEquationException("Equation with more than 1 equal sign (or no equal sign at all): " + equation);

            }

            String leftPart = parts[0];
            String rightPart = parts[1];

            //****************************************
            // PROCESS THE LEFT PART OF THE EQUATION
            if (!containsOnlyLetters(leftPart) || !containsOnlyLettersNumbersAndXOR(rightPart)) {
                // discard equations where variables contain illegal characters (not letters)
                // and discard equations where the right part contains illegal characters
                throw new IncorrectEquationException("Illegal var name or illegal characters in the right part: " + equation);

            }

            if (equationMap.containsKey(leftPart)) {
                // The same var cannot be on the left side of multiple equations
                throw new IncorrectEquationException("Duplicate var on the left side: " + equation);
            }

            // If it's the first time we see the var on the left part, map the var to an ID and put it in the HashMap
            Integer lVar = this.varMap.get(leftPart);
            if (lVar == null) {
                varMap.put(leftPart, varId);
                lVar = varId;
                varId++;
            }


            //*****************************************
            // PROCESS THE RIGHT PART OF THE EQUATION
            ArrayList<Operand> operandList = new ArrayList<Operand>();

            String[] operands = rightPart.split("\\^");
            int scalar = 0;
            boolean solved = true;
            for (String operand: operands) {
                if (isVar(operand)) {
                    solved = false;
                    Integer iOp = this.varMap.get(operand);
                    if (iOp == null) {
                        this.varMap.put(operand, varId);
                        iOp = varId;
                        varId++;
                    }
                    operandList.add(new Operand(iOp, true));
                } else {
                    int iOperand = 0;
                    // DON'T TRY, PROPAGATE EXCEPTION
                    iOperand = Integer.parseInt(operand);
                    scalar ^= iOperand;
                }
            }

            if (solved) {
                this.solvedMap.put(lVar, scalar);
            } else {
                if (scalar != 0) {
                    operandList.add(new Operand(scalar, false));
                }
                this.equationMap.put(lVar, operandList);
            }
            this.processedMap.put(lVar, false);
        }
        Integer idX = this.varMap.get("x");
        if (idX == null) {
            throw new XNotFoundException("X not found, cannot build System of Equations.");
        }
    }

    public String resolve() throws Exception {
        int idX = this.varMap.get("x");
        return "" + getVarValue(idX);
    }

    public int getVarValue(int varId) throws Exception {
        Integer value = this.solvedMap.get(varId);
        if (value != null) {
            return value;
        }
        ArrayList<Operand> operands = this.equationMap.get(varId);
        if (operands == null) {
            throw new VariableNotFoundException("Variable not found: " + varId);
        }
        int result = 0;
        for (Operand operand: operands) {
            if (operand.isVar()) {
                result = result ^ getVarValue(operand.getOperand());
            } else {
                result = result ^ operand.getOperand();
            }
        }
        this.solvedMap.put(varId, result);
        this.equationMap.remove(varId);
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
}

class RecursiveMain {

    public static String SystemofEquations(String str) {
        // code goes here
        try {
            SystemOfEquations soe = new SystemOfEquations(str);
            return soe.resolve();
        } catch (Exception e) {
            return "null";
        }
    }


//     public static String SystemofEquations(String str) {
//         // code goes here
//
//         if (str.isEmpty()) {
//           // System.out.println("Empty");
//           return "null";
//         }
//         String result = "";
//
//         // 1. Separate each equation
//         String [] equations = str.split("; ");
//
//         HashMap<String, Integer> varMap = new HashMap<String, Integer>();
//         HashMap<Integer, ArrayList<Operand>> equationMap = new HashMap<Integer, ArrayList<Operand>>();
//         HashMap<Integer, Integer> solvedMap = new HashMap<Integer, Integer>();
//         HashMap<Integer, Boolean> processedMap = new HashMap<Integer, Boolean>();
//
//         int varId = 0;
//
//         // 2. For each equation, get the left part and store it into a HashMap
//         for (String equation: equations) {
//             String trimmedEquation = equation.replaceAll("\\s+", "");
//             String [] parts = trimmedEquation.split("=");
//
//             if (parts.length != 2) {
//                 // Reject equations with more than one "=" sign or no "=" sign at all
//                 // System.out.println("Equation with more than 1 equal sign (or no equal sign at all)");
//                 return "null";
//             }
//
//             String leftPart = parts[0];
//             String rightPart = parts[1];
//
//             if (!containsOnlyLetters(leftPart) || !containsOnlyLettersNumbersAndXOR(rightPart)) {
//                 // discard equations where variables contain illegal characters (not letters)
//                 // and discard equations where the right part contains illegal characters
//                 System.out.println("Illegal var name or illegal characters in the right part)");
//                 return "null";
//             }
//
//             if (equationMap.containsKey(leftPart)) {
//                 // The same var cannot be on the left side of multiple equations
//                 System.out.println("Duplicate var on the left side: " + leftPart);
//                 return "null";
//             }
//
//             Integer lVar = varMap.get(leftPart);
//             if (lVar == null) {
//                 varMap.put(leftPart, varId);
//                 lVar = varId;
//                 varId++;
//             }
//
//             ArrayList<Operand> operandList = new ArrayList<Operand>();
//
//             String[] operands = rightPart.split("\\^");
//             int scalar = 0;
//             boolean solved = true;
//             for (String operand: operands) {
//                 if (isVar(operand)) {
//                     solved = false;
//                     Integer iOp = varMap.get(operand);
//                     if (iOp == null) {
//                         varMap.put(operand, varId);
//                         iOp = varId;
//                         varId++;
//                     }
//                     operandList.add(new Operand(iOp, true));
//                 } else {
//                     int iOperand = 0;
//                     try {
//                         iOperand = Integer.parseInt(operand);
//                     } catch (NumberFormatException e) {
//                         System.out.println("Wrong value parsing operand to integer: " + operand);
//                         return "null";
//                     }
//                     scalar ^= iOperand;
//                 }
//             }
//
//
//
//             if (solved) {
//                 solvedMap.put(lVar, scalar);
//             } else {
//                 if (scalar != 0) {
//                     operandList.add(new Operand(scalar, false));
//                 }
//                 equationMap.put(lVar, operandList);
//             }
//             processedMap.put(lVar, false);
//         }
//         equations = null;
//
//         int idX = varMap.get("x");
//         int currentVarId = idX;
//         int count = 0;
//         Stack<Integer> varStack = new Stack<Integer>();
//         int numSolved = solvedMap.size();
//         boolean changedState = true;
//         while (!solvedMap.containsKey(idX) && changedState) {
//             if (count%1000==0) {
//                 // int solvedSize = solvedMap.size();
//                 // int totSize = solvedSize + equationMap.size();
//                 // System.out.println(solvedSize + "/" + totSize);
//                 changedState = false;
//             }
//
//             // System.out.println("\n\nITERATION: " + count);
//             // System.out.println("currentVarId: " + currentVarId);
//             // System.out.println("\nSOLVED VARS:");
//             // for (Map.Entry<Integer, Integer> entry: solvedMap.entrySet()) {
//             //     System.out.println(entry.getKey() + "=" + entry.getValue());
//             // }
//             // System.out.print("\nUNSOLVED VARS:");
//             // for (Map.Entry<Integer, ArrayList<Operand>> entry: equationMap.entrySet()) {
//             //     System.out.print("\n" + entry.getKey() + "=");
//             //     for (Operand operand: entry.getValue()) {
//             //         System.out.print("^"+operand.getOperand());
//             //         if (operand.isVar()) {
//             //             System.out.print("(V)");
//             //         }
//             //     }
//             // }
//
//             ArrayList<Operand> operands = equationMap.get(currentVarId);
//             ArrayList<Operand> newOperands = new ArrayList<Operand>();
//             int scalar = 0;
//             boolean solved = true;
//             for (Operand operand: operands) {
//                 if (operand.isVar()) {
//                     Integer scalarValue = solvedMap.get(operand.getOperand());
//                     if (scalarValue != null) {
//                         scalar ^= scalarValue;
//                     } else {
//                         solved = false;
//                         newOperands.add(operand);
//                         varStack.push(operand.getOperand());
//                     }
//                 } else {
//                     scalar ^= operand.getOperand();
//                 }
//             }
//             if (solved) {
//                 solvedMap.put(currentVarId, scalar);
//                 equationMap.remove(currentVarId);
//                 changedState = true;
//             } else {
//                 if (scalar != 0) {
//                     newOperands.add(new Operand(scalar, false));
//                 }
//                 equationMap.put(currentVarId, newOperands);
//             }
//
//             count++;
//
//             if (processedMap.get(currentVarId) == false) {
//                 processedMap.put(currentVarId, true);
//                 changedState = true;
//             }
//
//             if (varStack.size() > 0) {
//                 currentVarId = varStack.pop();
//             } else {
//                 if (solvedMap.size() == numSolved) {
//                     break;
//                 } else {
//                     numSolved = solvedMap.size();
//                     currentVarId = idX;
//                 }
//             }
//         }
//
//         // System.out.println("\n\nITERATION: " + count);
//         // System.out.println("currentVarId: " + currentVarId);
//         // System.out.println("\nSOLVED VARS:");
//         // for (Map.Entry<Integer, Integer> entry: solvedMap.entrySet()) {
//         //     System.out.println(entry.getKey() + "=" + entry.getValue());
//         // }
//         // System.out.print("\nUNSOLVED VARS:");
//         // for (Map.Entry<Integer, ArrayList<Operand>> entry: equationMap.entrySet()) {
//         //     System.out.print("\n" + entry.getKey() + "=");
//         //     for (Operand operand: entry.getValue()) {
//         //         System.out.print("^"+operand.getOperand());
//         //         if (operand.isVar()) {
//         //             System.out.print("(V)");
//         //         }
//         //     }
//         // }
//
//
//         Integer solution = solvedMap.get(idX);
//         if (solution == null) {
// //          System.out.println("System of Equations without solution")
//             return "null";
//         }
//         result += solution;
//
//         return result;
//     }


    public static void main (String[] args) {
    // keep this function call here
        Scanner s = new Scanner(System.in);
        System.out.print(SystemofEquations(s.nextLine()));
    }

}
