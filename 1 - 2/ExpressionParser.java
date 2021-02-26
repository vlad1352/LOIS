

public class ExpressionParser {

    public String line;
    public int subFormulasNumber;
    private final char[] symbols = {'A', 'B', 'C','D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    public ExpressionParser(String line) {
        this.line = line;
        this.subFormulasNumber = 0;
    }

    //Подсчет подформу

    public void parse() {
        String countSymbols = "";
        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == '!' || line.charAt(i) == '~' || line.charAt(i) == '&' || line.charAt(i) == '|' || line.charAt(i) == '>') {
                subFormulasNumber++;
                continue;
            }
            for(char sym: symbols){
                if(line.charAt(i) == sym){

                    boolean check = false;
                    for (int j = 0; j < countSymbols.length(); j++) {
                        if(countSymbols.charAt(j) == line.charAt(i)){
                            check = true;
                        }
                    }
                    if(!check){
                        countSymbols += sym;
                        subFormulasNumber++;
                    }
                    break;
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