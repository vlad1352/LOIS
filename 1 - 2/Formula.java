public class Formula {

    private String formula;
    private final String[] constants = {"1", "0"};
    private final char[] symbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final String[] operations = {"!", "&", "|", "~", ">"};

    Formula(String formula) {
        this.formula = formula;
        replaceImplication();
    }


    private void replaceImplication() {

        StringBuilder newLine = new StringBuilder();
        for (char sign : this.formula.toCharArray()) {
            if (sign != '-') {
                newLine.append(sign);
            }
        }

        this.formula = newLine.toString();
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public void printFormula() {
        System.out.println(formula);

    }

    // Подсчет количества подформул в формуле
    public int findSubFormulas() {
        ExpressionParser parse = new ExpressionParser(formula);
        parse.parse();

        return parse.subFormulasNumber;
    }

    //Создание СКНФ

    public String makeSKNF() {
        SKNFParser sknf = new SKNFParser(formula);

        return sknf.makeSKNF();
    }

    // Проверка на правильность введенной формулы
    public boolean checkFormula() {

        if (formula.equals("1") || formula.equals("0")) {
            return true;
        }

        if (formula.equals("")) {
            return false;
        }

        boolean check = false;
        for (char symbol : symbols) {
            if (formula.charAt(formula.length() - 1) == symbol || formula.charAt(formula.length() - 1) == '1' || formula.charAt(formula.length() - 1) == '0') {
                check = true;
                break;
            }
        }
        if (!check) {
            if (formula.charAt(formula.length() - 1) != ')') {
                return false;
            }
        }

        int balance = 0, left = 0, right = 0;
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '(') {
                balance++;

            } else if (formula.charAt(i) == ')') {
                balance--;

            }

            if (balance < 0)
                return false;
        }
        if (balance != 0)
            return false;


        String str = check(formula);

        if(str == null) {
            return false;
        }
        else if (str.length() != 1) {
            return false;
        }

        return true;
    }

    private String check(String line) {

        if(line == null){
            return null;
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
        if(left + 1 == right){
            return null;
        }

        if (left == 0 && right == 0) {

            int p = 2000;
            //System.out.println(line);
            int x = line.indexOf('!');
            //System.out.println("X : "  + x);
            if (x >= 0 && p > x) {

                p = x;

            }
            else if (x == -1){
                for(String operation : operations){
                    x = line.indexOf(operation);
                    if(x > 0 && p > x){
                        p = x;
                    }
                }
            }


            while (p >= 0) {
                // System.out.println("P = " + p);
                if (line.length() == 1) {
                    return line;
                }

                //System.out.println("LINE LENGTH : " + line.length());
                if (line.charAt(p) == '!') {
                    if (line.charAt(p + 1) == '1' || line.charAt(p + 1) == '0') {

                        line = line.replace(line.substring(p, p + 2), "A");
                    } else {
                        for (char symbol : symbols) {
                            if (line.charAt(p + 1) == symbol) {
                               // System.out.println(line);
                                line = line.replace(line.substring(p, p + 2), "S");
                                break;
                            }
                        }
                    }

                }


                //System.out.println(line);
                 if (line.charAt(p) == '&' || line.charAt(p) == '|' || line.charAt(p) == '~' || line.charAt(p) == '>') {
                    //System.out.println("OPERATION : " + p);
                    if (p != line.length() - 1) {
                        for (char symbol1 : symbols) {
                            for (char symbol2 : symbols) {
                                //System.out.println("LENGTH : " + line.length());

                                if ((symbol1 == line.charAt(p - 1) && symbol2 == line.charAt(p + 1)) ||
                                        (line.charAt(p - 1) == '0' && line.charAt(p + 1) == '0') ||
                                        (line.charAt(p - 1) == '0' && line.charAt(p + 1) == '1') ||
                                        (line.charAt(p - 1) == '1' && line.charAt(p + 1) == '0') ||
                                        (line.charAt(p - 1) == '1' && line.charAt(p + 1) == '1') ||
                                        (line.charAt(p - 1) == '0' && line.charAt(p + 1) == symbol1) ||
                                        (line.charAt(p - 1) == symbol1 && line.charAt(p + 1) == '0') ||
                                        (line.charAt(p - 1) == '1' && line.charAt(p + 1) == symbol1) ||
                                        (line.charAt(p - 1) == symbol1 && line.charAt(p + 1) == '1')) {

                                    line = line.replace(line.substring(p - 1, p + 2), "Q");
                                    //System.out.println("RETURN : " + line);
                                    return line;

                                }
                            }

                        }
                    }
                }


                //System.out.println("FIND OPERATION");
                x = line.indexOf('!');

                if (x > 0 && p > x) {

                    p = x;

                }
                else {
                    for(String operation : operations){
                        x = line.indexOf(operation);
                        if(x > 0 && p > x){
                            p = x;
                        }
                    }
                }
            }
        } else {
            String expression = line.substring(left + 1, right);
            String str = check(expression);


            //System.out.println(str);
            str = check(str);
            //System.out.println(str);

            if(line == null)
                return null;
            line = line.replace("(" + expression + ")", "Q");
            line = check(line);

            if(line == null)
                return null;

        }

        return line;
    }
}
