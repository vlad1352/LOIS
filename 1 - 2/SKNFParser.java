import java.util.ArrayList;
import java.util.List;

public class SKNFParser {

    private final char[] symbols = {'A', 'B', 'C','D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final String[] operations = {"!", "&", "|", "~", ">"};

    private String formula;
    private String sknf;
    public List<List<String>> matrix = new ArrayList<>();

    SKNFParser(String formula){
        this.formula = formula;
        this.sknf = "";
    }


    // ЗАполнение таблицы истинности для заданной формулы
    private void makeTable(int numberOfSymbols, List<String> temp){

        if(numberOfSymbols == 0){
            List<String> l1 = new ArrayList<>();
            l1.addAll(temp);
            this.matrix.add(l1);
            return;
        }

        temp.add("1");
        makeTable(numberOfSymbols - 1, temp);
        temp.remove(temp.size() - 1);

        temp.add("0");
        makeTable(numberOfSymbols - 1, temp);
        temp.remove(temp.size() -1);

    }

    // Дополнение таблицы истинности значеними формулы
    private void addFunctionValueAtTable(){
        matrix.get(0).add("f");
        for (int i = 1; i < matrix.size(); i++) {
            String str = "";
            str += formula;
            int functionValue = replaceExpression(str, i, -1);
            matrix.get(i).add(String.valueOf(functionValue));

        }

    }

    // Подсчет значения формулы при заданных значениях
    // line - текущая формула
    // lineNumber - номер стороки в таблице истинности
    // functionalValue - значение функции на текущей итерации

    private int replaceExpression(String line, int lineNumber, int functionValue) {

        int balance = 0;
        int left = 0, right = 0;

        for (int i = 0; i < line.length(); i++) {

            if (line.charAt(i) == '(') {
                balance++;
                if (balance == 1) {
                    left = i;
                }
            } else if (line.charAt(i) == ')') {
                balance--;
                if (balance == 0) {
                    right = i;
                    break;
                }
            }
        }

        if (left == 0 && right == 0) {
            //System.out.println("");
           //System.out.println( lineNumber + " ITERATION");
           // System.out.println("LENGTH: " + line.length());

            for (int i = 0; i < line.length(); i++) {
                if(line.charAt(i) == '!'){
                    if(line.charAt(i + 1) == '1' || line.charAt(i + 1) == '0'){
                        int value = unaryOperation(Integer.parseInt(line.substring(i + 1,i + 2)));
                        functionValue = value;
                        line = line.replace(line.substring(i, i + 2), String.valueOf(functionValue));
                    }
                    else {
                        int index = matrix.get(0).indexOf(line.substring(i + 1,i + 2));
                        int value = unaryOperation(Integer.parseInt(matrix.get(lineNumber).get(index)));
                        functionValue = value;
                        line = line.replace(line.substring(i, i + 2), String.valueOf(functionValue));
                    }

                }
            }
            int p = 2000;
           // System.out.println(line);
            int x = -2;

            for(String operation : operations){
                x = line.indexOf(operation);
                if(!operation.equals("!") && x > 0) {
                    if(line.charAt(x) == operation.charAt(0))
                        if(p > x){
                            p = x;
                        }
                }
            }


            while(p > 0) {
               // System.out.println("P = " + p);
                if(line.length() == 1)
                    break;
                for (String operation : operations) {

                    if(!operation.equals("!") && line.charAt(p) == operation.charAt(0)){
                        //System.out.println("OPERATION : " + operation);

                        if(line.charAt(p - 1) == '1'){
                            if(line.charAt(p + 1) == '1'){
                                functionValue  = binaryOperation(1,1, operation);
                            }

                            else if(line.charAt(p + 1) == '0'){
                                functionValue  = binaryOperation(1,0, operation);

                            }
                            else {
                                //int index1  = matrix.get(0).indexOf(line.substring(i - 1, i));
                                int index2 = matrix.get(0).indexOf(line.substring(p + 1, p + 2));
                                functionValue  = binaryOperation(1, Integer.parseInt(matrix.get(lineNumber).get(index2)), operation );

                            }
                        }
                        else if(line.charAt(p - 1) == '0'){
                            if(line.charAt(p + 1) == '1'){
                                functionValue  = binaryOperation(0,1, operation);
                            }

                            else if(line.charAt(p + 1) == '0'){
                                functionValue  = binaryOperation(0,0, operation);

                            }
                            else {
                                //int index1  = matrix.get(0).indexOf(line.substring(p - 1, p));
                                int index2 = matrix.get(0).indexOf(line.substring(p + 1,p + 2));
                                functionValue  = binaryOperation(0, Integer.parseInt(matrix.get(lineNumber).get(index2)), operation );
                            }

                        }
                        else if(line.charAt(p + 1) == '0'){
                            if(line.charAt(p - 1) == '1'){
                                functionValue  = binaryOperation(1,0, operation);
                            }

                            else if(line.charAt(p - 1) == '0'){
                                functionValue  = binaryOperation(0,0, operation);

                            }
                            else {
                                int index1  = matrix.get(0).indexOf(line.substring(p - 1, p));
                                //int index2 = matrix.get(0).indexOf(line.substring(i + 1,i + 2));
                                functionValue  = binaryOperation(Integer.parseInt(matrix.get(lineNumber).get(index1)), 0, operation );
                            }

                        }
                        else if(line.charAt(p + 1) == '1'){
                            if(line.charAt(p - 1) == '1'){
                                functionValue  = binaryOperation(1,1, operation);
                            }

                            else if(line.charAt(p - 1) == '0'){
                                functionValue  = binaryOperation(0,0, operation);

                            }
                            else {
                                int index1  = matrix.get(0).indexOf(line.substring(p - 1, p));
                                //int index2 = matrix.get(0).indexOf(line.substring(i + 1,i + 2));
                                functionValue  = binaryOperation(Integer.parseInt(matrix.get(lineNumber).get(index1)), 1, operation );
                            }

                        }
                        else{
                            int index1  = matrix.get(0).indexOf(line.substring(p - 1, p));
                            int index2 = matrix.get(0).indexOf(line.substring(p + 1, p + 2));

                            functionValue  = binaryOperation(Integer.parseInt(matrix.get(lineNumber).get(index1)), Integer.parseInt(matrix.get(lineNumber).get(index2)), operation );
                        }
                        line = line.replace(line.substring(p - 1, p + 2), String.valueOf(functionValue));

                        //System.out.println("LINE : " + line);
                        break;
                    }
                }
                 x = -2;
                //System.out.println("FIND OPERATION");
                for(String operation : operations){
                    if(!operation.equals("!")){
                        x = line.indexOf(operation);
                       // System.out.println("X = " + x);
                        if(x > 0 && line.charAt(x) == operation.charAt(0) && p > x) {
                            p = x;
                        }
                    }
                    if(line.length() == 1){
                        p = -1;
                        break;
                    }
                }


            }


            //System.out.println(line);

        } else {
            String expression = line.substring(left + 1, right);
            //System.out.println(expression);

            functionValue = replaceExpression(expression, lineNumber, functionValue);

            line = line.replace("(" + expression + ")", String.valueOf(functionValue));

            functionValue = replaceExpression(line, lineNumber, functionValue);

        }


        return functionValue;
    }


    // Создание из формулы СКНФ

    public String makeSKNF(){


        int a = 0;
        List<String> list1 = new ArrayList<>();
       for (char symbol : symbols){
           if(formula.indexOf(symbol) != -1){
               a++;
               list1.add(formula.substring(formula.indexOf(symbol), formula.indexOf(symbol) + 1));
           }
       }
        //System.out.println(a);
       matrix.add(list1);

       List<String> list = new ArrayList<>();
       makeTable(a, list);
        //System.out.println(matrix);
       addFunctionValueAtTable();
        System.out.println(matrix);


        // Построение СКНФ по таблиуе истинности
        for (int i = 1; i < matrix.size(); i++) {
            if(matrix.get(i).get(matrix.get(i).size() - 1).equals("0")){
                sknf+= "(";
                for (int j = 0; j < matrix.get(i).size() - 1; j++) {

                    if(matrix.get(i).get(j).equals("0")){
                        sknf += matrix.get(0).get(j);

                    }
                    else {
                        sknf += "(!" + matrix.get(0).get(j) + ")";
                    }
                    if(j != matrix.get(i).size() - 2){
                        sknf += "|";
                    }
                }
                sknf += ")";
                if(i != matrix.size() - 1){
                    sknf += " & ";
                }
            }


        }




       return sknf;
    }

    //Подсчет унарной операции при заданном значении
    private int unaryOperation(int value){
        if(value == 1){
             return 0;
        }
        return 1;
    }

    //Подсчет бинарной операции при заданном значении
    private int binaryOperation(int value1, int value2, String operation){
        if(operation.equals("&")){
            if(value1 == 0 && value2 == 0)
                return 0;
            if((value1 == 1 && value2 == 0) || (value1 == 0 && value2 == 1))
                return 0;
            if(value1 == 1 && value2 == 1)
                return 1;
        }
        if(operation.equals("|")){
            if(value1 == 0 && value2 == 0)
                return 0;
            if((value1 == 1 && value2 == 0) || (value1 == 0 && value2 == 1))
                return 1;
            if(value1 == 1 && value2 == 1)
                return 1;
        }

        if(operation.equals("|")){
            if(value1 == 0 && value2 == 0)
                return 0;
            if((value1 == 1 && value2 == 0) || (value1 == 0 && value2 == 1))
                return 1;
            if(value1 == 1 && value2 == 1)
                return 1;
        }
        if(operation.equals(">")){
            if(value1 == 0 && value2 == 0)
                return 1;
            if((value1 == 1 && value2 == 0) )
                return 0;
            if((value1 == 0 && value2 == 1))
                return 1;
            if(value1 == 1 && value2 == 1)
                return 1;
        }

        if(operation.equals("~")){
            if(value1 == 0 && value2 == 0)
                return 1;
            if((value1 == 0 && value2 == 1) )
                return 0;
            if((value1 == 1 && value2 == 0))
                return 0;
            if(value1 == 1 && value2 == 1)
                return 1;
        }
        return -1;
    }



}
