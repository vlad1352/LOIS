public class Formula {

    private String formula;
    private final String[] constants = {"1", "0"};
    private final char[] symbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final String[] operations = {"!", "&", "|", "~", ">"};

    Formula(String formula) {
        this.formula = formula;
        replace();
    }


    private void replace() {

        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '-' && formula.charAt(i + 1) == '>') {
                newLine.append('>');
                i++;
            }

            else if(formula.charAt(i) == '/' && formula.charAt(i + 1) == '\\'){
                newLine.append('&');
                i++;
            }

            else if(formula.charAt(i) == '\\' && formula.charAt(i + 1) == '/'){
                newLine.append('|');
                i++;
            }
            else{
                newLine.append(formula.charAt(i));

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
        for(char s : symbols){
            if(formula.length() == 1 && formula.charAt(0) == s){
                return true;
            }
        }


        if (formula.charAt(formula.length() - 1) != ')') {
            return false;
        }


        int balance = 0;

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
        if(formula.charAt(0) != '(' && formula.charAt(formula.length() - 1) != ')')
            return false;


        String str = check(formula, false);

        if (str == null) {
            return false;
        } else if (str.length() != 1) {
            return false;
        }

        return true;
    }

    private String check(String line, boolean key) {

        if (line == null) {
            return null;
        }
        if (line.length() == 1) {
            return line;
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
        if(key){
            if(left ==0 && right == line.length() - 1){
                return null;
            }
        }

        if (left + 1 == right) {
            return null;
        }

        if (left == 0 && right == 0) {

            //System.out.println(line);
            int p = line.indexOf('!');
            //System.out.println("X : "  + x);
            while (p >= 0 && p != line.length() - 1) {
                if (line.charAt(p + 1) == '1' || line.charAt(p + 1) == '0') {
                    line = line.replace(line.substring(p, p + 2), "A");
                } else if (line.charAt(p + 1) >= 'A' && line.charAt(p + 1) <= 'Z') {
                    line = line.replace(line.substring(p, p + 2), "S");
                    break;
                }

                p = line.indexOf('!');
            }

            if (line.length() == 1) {
                return line;
            }

            p = line.indexOf('&');
            if (p == -1) {
                p = line.indexOf('|');
            }
            if (p == -1) {
                p = line.indexOf('~');
            }
            if (p == -1) {
                p = line.indexOf('>');
            }
            while (p != -1) {

                for (char symbol1 : symbols) {
                    for (char symbol2 : symbols) {
                        if (p != line.length() - 1) {
                            if(line.length() == 1)
                                return line;
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
                                break;

                            }
                        }

                    }
                }

                p = line.indexOf('&');
                if (p == -1) {
                    p = line.indexOf('|');
                }
                if (p == -1) {
                    p = line.indexOf('~');
                }
                if (p == -1) {
                    p = line.indexOf('>');
                }
            }
            return line;

        } else {
            String expression = line.substring(left + 1, right);
            if(left == 0 && right == line.length() - 1){
                key = true;
            }
            String str = check(expression, key);


            str = check(str,key);

            if (str == null)
                return null;

            line = line.replace("(" + expression + ")", str);
            line = check(line, key);

            if (line == null)
                return null;

        }

        return line;
    }
}
