import com.MyCompiler.*;

import java.util.ArrayList;
import java.util.List;

public class Main {



    public static String toBinary(int i){
        String ret = "";
        int mod;
        while(i != 0){
            mod = i % 2;
            i /= 2;
            ret = mod + ret;
        }
        return ret;
    }
    public static void main(String[] args) throws Exception{
//        common.Impl I = new common.Impl();
//        I.Anaylsis("src/word.txt");
//        UI ui = new UI();
        List<String> vn= new ArrayList();
        List<String> vt = new ArrayList();
        String[] grammar = new String[7];
        grammar[0]="s->#e#";
        grammar[1]="e->e+t";
        grammar[2]="e->t";
        grammar[3]="t->t*f";
        grammar[4]="t->f";
        grammar[5]="f->(e)";
        grammar[6]="f->i";
        vn.add("s");
        vn.add("e");
        vn.add("t");
        vn.add("f");
        vt.add("#");
        vt.add("+");
        vt.add("*");
        vt.add("(");
        vt.add(")");
        vt.add("i");
        LastVt lv = new LastVt(grammar, 4, 6);
        boolean lastVt[][] = lv.getLastVt(vn, vt);
        FirstVt fv = new FirstVt(grammar, 4, 6);
        boolean firstVt[][] = fv.getFirstVt(vn, vt);
        PrecedenceTb ptb = new PrecedenceTb(grammar, firstVt, lastVt, vn, vt);
        int [][]tb = ptb.getTb();
        for(int i = 0; i < tb.length; i++) {
            for (int j = 0; j < tb[0].length; j++)
                System.out.print(tb[i][j]+"  ");
            System.out.println();
        }
//        boolean bo[][] = fv.getLastVt(vn, vt);
//        for(int i = 0; i < bo.length; i++) {
//            for (int j = 0; j < bo[0].length; j++)
//                System.out.print(bo[i][j]);
//            System.out.println();
//        }
//        String str = "wefq  wfwef\n\rfweqfqe  wf";
//        for(int i = 0; i < str.length(); i++){
//            System.out.println(str.charAt(i));
//        }
//        System.out.println(str);
    }
}
