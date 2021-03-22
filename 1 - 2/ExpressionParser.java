import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExpressionParser {

    public String line;
    public int subFormulasNumber;
    private final char[] symbols = {'A', 'B', 'C','D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private HashSet<String> set;

    public ExpressionParser(String line) {
        this.line = line;
        this.subFormulasNumber = 0;
        set = new HashSet<>();
    }

    //Подсчет подформул

    public void parse() {
        String str = find(line);
       // System.out.println(str);
       // System.out.println(set);
        subFormulasNumber += set.size() - 1;
        for(char s : symbols){
            int x = line.indexOf(s);
            if(x != -1){
                subFormulasNumber++;
            }
        }
        int x = line.indexOf("1");
        if(x != -1){
            subFormulasNumber++;
        }
        x = line.indexOf("0");
        if(x != -1){
            subFormulasNumber++;
        }

    }

    public String getLine() {
        return line;
    }

    public int getSubFormulasNumber() {
        return subFormulasNumber;
    }

    private String find(String line) {
       // System.out.println(line);
        int balance = 0;
        int left = 0, right = 0;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {

            if (line.charAt(i) == '(') {
                balance++;
                if (balance == 1 ) {
                    left = i;
                }
            } else if (line.charAt(i) == ')') {
                balance--;
                if (balance == 0) {
                    right = i;
                    list.add(line.substring(left+1, right));
                }
            }
        }
        
        if(list.isEmpty()){
            if(line.length() == 2){
                set.add(line);
                return  line;
            }
            else if(line.length() == 3){
                if(line.charAt(0) > line.charAt(2)){
                    StringBuilder str = new StringBuilder(line);
                    set.add(str.reverse().toString());
                    return str.reverse().toString();
                } else{
                    set.add(line);
                    return line;
                }
            }
        } else{
            if(list.size() == 1){
                line = line.replace(list.get(0), find(list.get(0)));
            } else if(list.size() == 2){
                String x = find(list.get(0));
                String y = find(list.get(1));
               // System.out.println(x);
               // System.out.println(y);
                if (x.compareTo(y) < 0) {
                   // System.out.println("-1");
                    line = line.replace(list.get(0), x);
                    line = line.replace(list.get(1), y);
                }
                else {
                   // System.out.println("1");
                    line = line.replace(list.get(0), y);
                    line = line.replace(list.get(1), x);
                }
              //  set.add(x);
                //set.add(y);
            }
        }
        //System.out.println(line);
        set.add(line);
        return line;
    }

}