/*
 * File:    EquationUtility.java
 * Package: utility
 * Author:  Zachary Gill
 */

package utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides functionality to solve standard equations.<br>
 * This equations in this class cannot solve integrals, derivatives, trigonometric functions, or factorials.<br>
 * You can, however, pass the result of those functions at particular values in as variables to a standard equation.
 */
public final class EquationUtility {
    
    //Enums
    
    /**
     * An enumeration of Operations that can be applied to operands in the equation.
     */
    public enum Operation {
        POWER('^'),
        ROOT('~'),
        MULTIPLY('*'),
        DIVIDE('/'),
        MODULUS('%'),
        ADD('+'),
        SUBTRACT('-');
        
        
        //Fields
        
        /**
         * The symbol of the Operation.
         */
        private char symbol;
        
        
        //Constructors
        
        /**
         * The constructor for an Operation.
         *
         * @param symbol The symbol of the Operation.
         */
        Operation(char symbol) {
            this.symbol = symbol;
        }
        
        
        //Getters
        
        /**
         * Returns the symbol of the Operation.
         *
         * @return The symbol of the Operation.
         */
        public char getSymbol() {
            return symbol;
        }
        
        
        //Functions
        
        /**
         * Retrieves an Operation by its symbol.
         *
         * @param symbol The symbol of the Operation.
         * @return The Operation.
         */
        public static Operation getOperation(char symbol) {
            for (Operation operation : Operation.values()) {
                if (operation.getSymbol() == symbol) {
                    return operation;
                }
            }
            return null;
        }
        
    }
    
    /**
     * An enumeration representing the order of operations.
     */
    public enum OrderOfOperations {
        FIRST("^~"),
        SECOND("*/%"),
        THIRD("+-");
        
        
        //Fields
        
        /**
         * The string of symbols corresponding to the stage in the order of operations.
         */
        private String symbols;
        
        
        //Constructors
        
        /**
         * The constructor for an OrderOfOperations enumeration.
         *
         * @param symbols The string of symbols corresponding to the stage in the order of operations.
         */
        OrderOfOperations(String symbols) {
            this.symbols = symbols;
        }
        
        
        //Getters
        
        /**
         * Returns the string of symbols corresponding to the stage in the order of operations.
         *
         * @return The string of symbols corresponding to the stage in the order of operations.
         */
        public String getSymbols() {
            return symbols;
        }
        
    }
    
    
    //Static Fields
    
    /**
     * The list of symbols for the Operations that can be applied to operands in the equation.
     */
    public static List<Character> symbols = collectSymbols();
    
    
    //Functions
    
    /**
     * Parses a mathematical equation into a series of Operations on Operands.
     *
     * @param equation The mathematical equation.
     * @return A series of Operations on Operands that represents the mathematical equation.
     */
    public static MathOperation parseMath(String equation) {
        equation = cleanEquation(equation);
        
        MathOperation op;
        try {
            op = ingestElements(parseElements(equation));
        } catch (ParseException e) {
            System.out.println("Equation: " + equation + " is malformed: " + e.getMessage());
            return null;
        }
        
        return op;
    }
    
    /**
     * Tokenizes the elements from a mathematical equation.
     *
     * @param equation The mathematical equation.
     * @return The tokens parsed from the mathematical equation.
     * @throws ParseException When there is an error parsing the mathematical equation.
     */
    @SuppressWarnings("UnnecessaryContinue")
    private static List<String> parseElements(String equation) throws ParseException {
        if (equation.startsWith("(") && equation.endsWith(")")) {
            equation = equation.substring(1, equation.length() - 1);
        }
        
        List<String> elements = new ArrayList<>();
        StringBuilder element = new StringBuilder();
        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            if (symbols.contains(c)) {
                if (element.length() > 0) {
                    elements.add(element.toString());
                    element = new StringBuilder();
                }
                elements.add(String.valueOf(c));
            } else if (c == '(') {
                if (element.length() > 0) {
                    elements.add(element.toString());
                    element = new StringBuilder();
                }
                
                int depth = 1;
                element.append(c);
                for (i = i + 1; i < equation.length(); i++) {
                    char c2 = equation.charAt(i);
                    element.append(c2);
                    if (c2 == '(') {
                        depth++;
                    } else if (c2 == ')') {
                        depth--;
                        if (depth == 0) {
                            break;
                        }
                    }
                    if (i == equation.length() - 1) {
                        throw new ParseException("Parentheses in equation are not lined up", i);
                    }
                }
                elements.add(element.toString());
                element = new StringBuilder();
            } else if (c == ')') {
                throw new ParseException("Parentheses in equation are not lined up", i);
            } else {
                element.append(c);
                if ((i == equation.length() - 1) && (element.length() > 0)) {
                    elements.add(element.toString());
                    element = new StringBuilder();
                }
            }
        }
        
        for (int i = 0; i < elements.size() - 1; i++) {
            String e = elements.get(i);
            String e2 = elements.get(i + 1);
            
            if (e.equals("()")) {
                elements.remove(i);
                if (i > 0) {
                    elements.remove(i - 1);
                }
                i = -1;
                continue;
            }
            
            if ((e.length() == 1) && symbols.contains(e.charAt(0)) && (e.charAt(0) == Operation.SUBTRACT.getSymbol())) {
                if (i == 0) {
                    elements.set(i, "(0-" + e2 + ")");
                    elements.remove(i + 1);
                    i = -1;
                    continue;
                } else {
                    String e3 = elements.get(i - 1);
                    if (symbols.contains(e3.charAt(0))) {
                        elements.set(i, "(0-" + e2 + ")");
                        elements.remove(i + 1);
                        i = -1;
                        continue;
                    }
                }
            }
            
            if ((e.length() == 1) && symbols.contains(e.charAt(0)) && (e.charAt(0) == Operation.ADD.getSymbol())) {
                if (i == 0) {
                    elements.remove(i);
                    i = -1;
                    continue;
                } else {
                    String e3 = elements.get(i - 1);
                    if (symbols.contains(e3.charAt(0))) {
                        elements.remove(i);
                        i = -1;
                        continue;
                    }
                }
            }
            
            if ((e.matches("(\\d+\\.?\\d*)|([a-zA-Z]+)") || e.startsWith("(")) && e2.startsWith("(")) {
                elements.set(i, "(" + e + "*" + e2 + ")");
                elements.remove(i + 1);
                i = -1;
                continue;
            }
        }
        
        for (int i = 0; i < elements.size() - 1; i++) {
            String e = elements.get(i);
            String e2 = elements.get(i + 1);
            
            if ((e.length() == 1) && symbols.contains(e.charAt(0)) && (e2.length() == 1) && symbols.contains(e2.charAt(0)) && (e2.charAt(0) != Operation.SUBTRACT.getSymbol())) {
                throw new ParseException("Stacked operators in equation", i);
            }
        }
        
        return elements;
    }
    
    /**
     * Ingests the tokens parsed from the mathematical equation into a series of operations representing the mathematical equation.
     *
     * @param elements The tokens that were parsed from the mathematical equation.
     * @return A series of operations representing the mathematical equation.
     * @throws ParseException When there is an error parsing the mathematical equation.
     */
    private static MathOperation ingestElements(List<String> elements) throws ParseException {
        List<MathOperand> operands = new ArrayList<>();
        int o = 0;
        for (int i = 0; i < elements.size(); i++) {
            String element = elements.get(i);
            
            if (element.startsWith("(")) {
                List<String> subElements;
                subElements = parseElements(element);
                MathOperation operation = ingestElements(subElements);
                MathOperand operand = new MathOperand();
                operand.op = operation;
                operands.add(operand);
                elements.set(i, String.valueOf(o++));
                
            } else if (!((element.length() == 1) && symbols.contains(element.charAt(0)))) {
                MathOperand operand = new MathOperand();
                try {
                    operand.n = NumberFormat.getInstance().parse(element);
                } catch (Exception ignored) {
                    if (element.matches("[a-zA-Z]+")) {
                        operand.var = element;
                    } else {
                        throw new ParseException("Error parsing operand: " + element, o);
                    }
                }
                operands.add(operand);
                elements.set(i, String.valueOf(o++));
            }
        }
        
        if (operands.size() < 2) {
            MathOperation operation = new MathOperation();
            operation.operand1 = operands.get(Integer.parseInt(elements.get(0)));
            operation.operand2 = new MathOperand();
            operation.operand2.n = 0;
            operation.operation = Operation.ADD;
            
            MathOperand op = new MathOperand();
            op.op = operation;
            operands.set(0, op);
            o = 1;
            
        } else {
            for (OrderOfOperations oop : OrderOfOperations.values()) {
                if (elements.size() == 1) {
                    break;
                }
                
                for (int i = 0; i < elements.size(); i++) {
                    String element = elements.get(i);
                    if ((element.length() == 1) && symbols.contains(element.charAt(0))) {
                        if (i == 0) {
                            throw new ParseException("Invalid symbol at start of equation", i);
                        } else if (i == elements.size() - 1) {
                            throw new ParseException("Invalid symbol at end of equation", i);
                        } else {
                            if (oop.getSymbols().contains(element)) {
                                MathOperation operation = new MathOperation();
                                operation.operand1 = operands.get(Integer.parseInt(elements.get(i - 1)));
                                operation.operand2 = operands.get(Integer.parseInt(elements.get(i + 1)));
                                operation.operation = Operation.getOperation(element.charAt(0));
                                
                                MathOperand operand = new MathOperand();
                                operand.op = operation;
                                
                                operands.add(operand);
                                elements.set(i - 1, String.valueOf(o++));
                                elements.remove(i);
                                elements.remove(i);
                                i = -1;
                            }
                        }
                    }
                }
            }
        }
        
        return operands.get(o - 1).op;
    }
    
    /**
     * Converts the equation string into a mathematical equation.
     *
     * @param equation The equation string.
     * @return A mathematical equation.
     */
    public static String cleanEquation(String equation) {
        equation = equation.replace("plus", "+");
        equation = equation.replace("add", "+");
        equation = equation.replace("sum", "+");
        
        equation = equation.replace("minus", "-");
        equation = equation.replace("subtract", "-");
        equation = equation.replace("sub", "-");
        equation = equation.replace("less", "-");
        
        equation = equation.replace("times", "*");
        equation = equation.replace("multiply", "*");
        equation = equation.replace("mult", "*");
        
        equation = equation.replace("divided by", "/");
        equation = equation.replace("divide", "/");
        equation = equation.replace("div", "/");
        
        equation = equation.replace("modulus", "%");
        equation = equation.replace("modular", "%");
        equation = equation.replace("mod", "%");
        
        equation = equation.replace("square root of", "2~");
        equation = equation.replace("square root", "2~");
        equation = equation.replace("squared root of", "2~");
        equation = equation.replace("squared root", "2~");
        equation = equation.replace("cube root of", "3~");
        equation = equation.replace("cube root", "3~");
        equation = equation.replace("cubed root of", "3~");
        equation = equation.replace("cubed root", "3~");
        equation = equation.replace("root of", "~");
        equation = equation.replace("root", "~");
        equation = equation.replace("st", "");
        equation = equation.replace("nd", "");
        equation = equation.replace("rd", "");
        equation = equation.replace("th", "");
        
        equation = equation.replace("power", "^");
        equation = equation.replace("pow", "^");
        equation = equation.replace("raised to the", "^");
        equation = equation.replace("raised to", "^");
        equation = equation.replace("squared", "^2");
        equation = equation.replace("cubed", "^3");
        
        return equation.replaceAll("\\s+", "");
    }
    
    /**
     * Populates the list of symbols of operations that can be found in a mathematical equation.
     *
     * @return The list of symbols of operations that can be found in a mathematical equation.
     */
    private static List<Character> collectSymbols() {
        List<Character> symbols = new ArrayList<>();
        for (Operation op : Operation.values()) {
            symbols.add(op.getSymbol());
        }
        return symbols;
    }
    
    
    //Inner Classes
    
    /**
     * An operand in a MathOperation.
     */
    public static class MathOperand {
        
        //Fields
        
        /**
         * The number representing the operand, if it is a number.
         */
        public Number n;
        
        /**
         * The variable representing the operand, if it is a variable.
         */
        public String var;
        
        /**
         * The MathOperation representing the operand, if it is a MathOperation.
         */
        public MathOperation op;
        
        
        //Methods
        
        /**
         * Provides a string representing the MathOperand.
         *
         * @return A string representing the MathOperand.
         */
        @Override
        public String toString() {
            return (n != null) ? String.valueOf(n) : (var != null) ? var : "(" + op + ")";
        }
        
    }
    
    /**
     * An operation in a mathematical equation.
     */
    public static class MathOperation {
        
        //Fields
        
        /**
         * The first operand.
         */
        public MathOperand operand1;
        
        /**
         * The second operand.
         */
        public MathOperand operand2;
        
        /**
         * The operation to be applied on the two operands.
         */
        public Operation operation;
        
        
        //Methods
        
        /**
         * Provides a string representing the MathOperation.
         *
         * @return A string representing the MathOperation.
         */
        @Override
        public String toString() {
            return operand1 + String.valueOf(operation.getSymbol()) + operand2;
        }
        
        /**
         * Evaluates the MathOperation.
         *
         * @param vars A map of variable values to be used in the equation, if needed.
         * @return The result of the evaluation.
         */
        public Number evaluate(Map<String, Number> vars) {
            Number n1;
            Number n2;
            
            if (operand1.n != null) {
                n1 = operand1.n;
            } else if (operand1.var != null) {
                if (vars.containsKey(operand1.var)) {
                    n1 = vars.get(operand1.var);
                } else {
                    System.out.println("Variable: " + operand1.var + " was not found in the provided var map");
                    return null;
                }
            } else if (operand1.op != null) {
                n1 = operand1.op.evaluate(vars);
            } else {
                System.out.println("The MathOperation is not valid");
                return null;
            }
            
            if (operand2.n != null) {
                n2 = operand2.n;
            } else if (operand2.var != null) {
                if (vars.containsKey(operand2.var)) {
                    n2 = vars.get(operand2.var);
                } else {
                    System.out.println("Variable: " + operand2.var + " was not found in the provided var map");
                    return null;
                }
            } else if (operand2.op != null) {
                n2 = operand2.op.evaluate(vars);
            } else {
                System.out.println("The MathOperation is not valid");
                return null;
            }
            
            Number result;
            switch (operation) {
                case POWER:
                    result = Math.pow(n1.doubleValue(), n2.doubleValue());
                    break;
                case ROOT:
                    result = Math.pow(n2.doubleValue(), 1.0 / n1.doubleValue());
                    break;
                case MULTIPLY:
                    result = n1.doubleValue() * n2.doubleValue();
                    break;
                case DIVIDE:
                    result = n1.doubleValue() / n2.doubleValue();
                    break;
                case MODULUS:
                    result = n1.doubleValue() % n2.doubleValue();
                    break;
                case ADD:
                    result = n1.doubleValue() + n2.doubleValue();
                    break;
                case SUBTRACT:
                    result = n1.doubleValue() - n2.doubleValue();
                    break;
                default:
                    result = null;
            }
            
            //System.out.println(n1 + " " + String.valueOf(operation.getSymbol()) + " " + n2 + " = " + result);
            return result;
        }
        
        
        /**
         * Evaluates the MathOperation.
         *
         * @return The result of the evaluation.
         */
        public Number evaluate() {
            return evaluate(new HashMap<>());
        }
        
    }
    
}
