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
        for (int i = 0; i < Math.pow(2, numberOfSymbols); i++) {
           matrix.add(makeRow(i, numberOfSymbols));
        }

    }

    private List<String> makeRow(int index, int numberOfSymbols) {
        List<String> row = new ArrayList<>();
        if(index == 0) {
            for (int i = 0; i < numberOfSymbols; i++) {
                row.add("0");
            }
            return row;
        }
        int b;
        StringBuilder builder = new StringBuilder();
        while(index != 0){
            b = index % 2;
            builder.append(b);
            index = index / 2;
        }
        builder = builder.reverse();
        for (int i = 0; i < numberOfSymbols - builder.length(); i++) {
            row.add("0");
        }
        for (int i = 0; i < builder.length() ; i++) {
            row.add(builder.substring(i, i + 1));
        }
        builder = null;
        return row;
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

        if(line.length() == 1){
            int index = matrix.get(0).indexOf(line);
            if(line.equals("1") || line.equals("0"))
                return functionValue;
            return Integer.parseInt(matrix.get(lineNumber).get(index));
        }

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
            int x = -2, k = 0;

            for(String operation : operations){
                x = line.indexOf(operation);
                if(!operation.equals("!") && x > 0) {
                    k++;
                    if(line.charAt(x) == operation.charAt(0))
                        if(p > x){
                            p = x;
                        }
                }
            }



            while(p > 0 && k > 0) {
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

                        break;
                    }
                }
                 x = -2;
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



        } else {
            String expression = line.substring(left + 1, right);

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
       matrix.add(list1);

       List<String> list = new ArrayList<>();
       makeTable(a, list);
       addFunctionValueAtTable();


       for (List<String> list2 : matrix) {
           System.out.println(list2);
       }

        // Построение СКНФ по таблице истинности
        if(matrix.get(0).size() == 1) {
            return "";
        }
        if(matrix.get(0).size() == 2){
            if(matrix.get(1).get(matrix.get(1).size() - 1).equals("0")) {
                sknf = "(" + matrix.get(0).get(0) + ")";
                return sknf;
            }
        }


        sknf = makeS();
       return sknf;
    }

    private String makeS() {
        List<String> subs = new ArrayList<>();
        for (int i = 1; i < matrix.size(); i++) {
            if(matrix.get(i).get(matrix.get(i).size() - 1).equals("0")) {
                subs.add(make(i));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");

        int h = subs.size() - 1;
        for(int i = 0; i < h - 2; i++) {
            sb.append("(");
        }

        for (int i = 0; i < subs.size(); i++) {
            sb.append(subs.get(i));
            if(i != 0 && i < subs.size() - 1) {
                sb.append(")");
            }
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }


    private String make(int index) {
        int h = matrix.get(0).size() - 2;
        StringBuilder sb = new StringBuilder();
        sb.append("(");

        for(int i = 0; i < h - 2; i++) {
            sb.append("(");
        }

        for (int j = 0; j < matrix.get(index).size() - 1; j++) {
            if(matrix.get(index).get(j).equals("0")) {
                sb.append(matrix.get(0).get(j));
            } else {
                sb.append("(!").append(matrix.get(0).get(j)).append(")");
            }
            if(j != 0 && j < matrix.get(index).size() - 2) {
                sb.append(")");
            }
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        //System.out.println(sb.toString());
        return sb.toString();
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
