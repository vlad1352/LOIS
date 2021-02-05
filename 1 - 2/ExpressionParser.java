public class ExpressionParser {

    public String line;
    public int subFormulasNumber;
    private final char[] symbols = {'A', 'B', 'C','D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    public ExpressionParser(String line) {
        this.line = line;
        this.subFormulasNumber = 0;
    }

    //Замена имликации (заменяет -> на >)

    // Подсчет и замена операций отрицания
    private void countAndDeleteLogicNot() {

        StringBuilder newLine = new StringBuilder();
        for (char logicNot : this.line.toCharArray()) {
            if (logicNot != '!') {
                newLine.append(logicNot);
            } else {
                this.subFormulasNumber++;
            }
        }

        this.line = newLine.toString();
    }

    //Подсчет остальных подформул
    private void replaceExpression(String line) {

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
            this.subFormulasNumber += line.length();
        } else {
            String expression = line.substring(left + 1, right);
            line = line.replace("(" + expression + ")", "Q");
            replaceExpression(line);

            replaceExpression(expression);
            this.subFormulasNumber--;
        }

        return;
    }

    public void parse() {
        //replaceImplication();
        countAndDeleteLogicNot();
        replaceExpression(this.line);

        for (int i = 0; i < line.length(); i++) {
            for(char symbol: symbols) {
                if(line.charAt(i) == symbol) {
                    for (int j = i + 1; j < line.length(); j++) {
                        if(line.charAt(j) == symbol){
                            subFormulasNumber--;
                            break;
                        }
                    }
                }
            }

        }

    }

    public String getLine() {
        return line;
    }

    public int getSubFormulasNumber() {
        return subFormulasNumber;
    }
}