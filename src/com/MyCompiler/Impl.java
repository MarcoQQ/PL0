package com.MyCompiler;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
/**
 * Created by marco sun on 2017/11/24.
 */

public class Impl {
    private static final String[] reserver = {"const", "var", "procedure", "odd", "if",
            "else", "then", "while", "do", "call", "begin",
            "end", "repeat", "until", "read", "write"};

    private enum Kind{
        RESERVER, IDENTIFIER, UNARYOPERATOR, BINARYOPERATOR, CONSTANT, DELIMITER, FLOAT
    }

    //存放单词
    private List<String> word = new ArrayList<>();

    //存放单词的类别和值
    private List<List> tokens = new ArrayList<>();
    //存放正确行号
    private List<Integer> lineNum = new ArrayList<>();
    //存放错误信息
    private List<String> errorInfo = new ArrayList();
    //存放错误行号
    private List<Integer> errorLineNum = new ArrayList<>();

    public List<String> getWord() {
        return word;
    }

    public List<String> getErrorInfo() {
        return errorInfo;
    }

    public List<Integer> getErrorLineNum() {
        return errorLineNum;
    }

    public List<Integer> getLineNum() {
        return lineNum;
    }

    public List<List> getTokens() {
        return tokens;
    }


    //分析词法函数
    public void Analysis(String fileName) throws Exception{
        int num = 0;
        File inFile = new File(fileName);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String line = null;
        if(inFile.exists()){
            try{
                System.out.println("file exsist");
                fileReader = new FileReader(fileName);
                bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null){
                    num++;
                    scan(line, num);
                }
//                print();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bufferedReader.close();
                fileReader.close();
            }
        }
    }

    public  boolean isLetter(char ch){
        if(ch >= 'a' && ch <= 'z'){
            return true;
        } else if(ch >= 'A' && ch <= 'Z'){
            return true;
        }
        return false;
    }

    public  String toBinary(int i){
        String ret = "";
        int mod;
        while(i != 0){
            mod = i % 2;
            i /= 2;
            ret = mod + ret;
        }
        return ret;
    }

    public  boolean isDigit(char ch){
        if(ch >= '0' && ch <= '9'){
            System.out.print("haha");
            return true;

        }

        return false;
    }

    public boolean isReserver(String str){
        for(int i = 0; i<reserver.length; i++){
            if(str.equals(reserver[i])){
                return true;
            }
        }
        return false;
    }

    public boolean isOperator(char ch){
        if(ch == '+' || ch == '-' || ch == '*' || ch == '/'
                || ch == '>' || ch == '<' || ch == '&' || ch == '|' || ch == '='){
            return true;
        }
        return false;
    }

    public boolean isDelimiter(char ch){
        if(ch == ';' || ch == '(' || ch == ')' || ch == ','){
            return true;
        }
        return false;
    }

    //扫描过程
    public void scan(String stream, int num){

        boolean hasDot = false;
        char ch = 0;
        int kind = -1;
        boolean flag = true;
        String ans = "";
        for(int i = 0; i < stream.length(); i++) {
            flag = true;
            char preCh = ch;
            ch = stream.charAt(i);
            System.out.println(ch);
            switch (kind) {
                case -1:
                    break;
                //标识符或保留字
                case 0:
                    if (isLetter(ch) || isDigit(ch) ) {
                        ans += ch;
                        flag = false;
                    } else {
                        word.add(ans);
                        List list = new ArrayList();
                        if (isReserver(ans)) {
                            list.add(1);
                        } else {
                            list.add(2);
                        }
                        list.add(ans);
                        tokens.add(list);
                        lineNum.add(num);
                        kind = -1;
                        flag = true;
                        ans = "";
                    }
                    break;

                //双目运算符
                case 1:
                    if(ch == '='){
                        ans = "" + preCh + ch;
                        word.add(ans);
                        List list = new ArrayList();
                        list.add(4);
                        list.add(ans);
                        tokens.add(list);
                        lineNum.add(num);
                        kind = -1;
                        flag = false;
                        ans = "";
                    }else{
                        if(preCh == '>' || preCh == '<'){
                            ans = "" + preCh;
                            word.add(ans);
                            List list = new ArrayList();
                            list.add(3);
                            list.add(ans);
                            tokens.add(list);
                            lineNum.add(num);
                            kind = -1;
                            flag = true;
                            ans = "";
                        } else if(preCh == ':'){
                            error(':', num);
                            kind = -1;
                            flag = true;
                            ans = "";
                        }
                    }
                    break;

                //常数
                case 2:
                    if (isDigit(ch) || ch == '.') {
                        if(hasDot && ch == '.'){
                            error('.', num);
                            hasDot = false;
                            kind = -1;
                            ans = "";
                            flag = false;
                        }else if(ch == '.'){
                            hasDot = true;
                            ans += ch;
                            flag = false;
                        }else{
                            ans += ch;
                            flag = false;
                        }
                    } else {
                        word.add(ans);
                        List list = new ArrayList();
                        //TODO
                        if(!hasDot) {
                            list.add(5);
                            String binary = toBinary(Integer.parseInt(ans));
                            if(binary == "")
                                binary = "0";
                            list.add(binary + "<二进制>");
                        } else{
                            list.add(7);
                            list.add(ans);
                        }
                        tokens.add(list);
                        lineNum.add(num);
                        flag = true;
                        ans = "";
                        kind = -1;
                        hasDot = false;

                    }
                    break;
                default:
                    break;
            }

            if (flag) {
                if (isLetter(ch)) {
                    //看后面是否为字符串
//                    System.out.println(ch);
                    kind = 0;
                    ans += ch;
                    continue;
                } else if (isDelimiter(ch)) {
                    //界符
                    word.add("" + ch);
                    List list = new ArrayList();
                    list.add(6);
                    list.add("" + ch);
                    tokens.add(list);
                    kind = -1;
                    lineNum.add(num);
                } else if (isOperator(ch) || ch == ':') {
                    //双目
                    if (ch == '>' || ch == '<' || ch == ':') {
                        kind = 1;
                        continue;
                    } else {
                        //单目
                        word.add("" + ch);
                        List list = new ArrayList();
                        list.add(3);
                        list.add("" + ch);
                        tokens.add(list);
                        kind = -1;
                        lineNum.add(num);
                    }

                } else if (isDigit(ch)) {
                    //看是否为常数
                    kind = 2;
                    ans += ch;
                    continue;
                } else if (Character.isSpaceChar(ch)) {
                    kind = -1;
                } else {
                    error(ch, num);
                }

            }
        }
        if(kind != -1){
            if(kind == 0){
                word.add(ans);
                List list = new ArrayList();
                if (isReserver(ans)) {
                    list.add(1);
                } else {
                    list.add(2);
                }
                list.add(ans);
                tokens.add(list);
                lineNum.add(num);

            }else if(kind == 1){
                word.add(ans);
                List list = new ArrayList();
                list.add(3);
                list.add(ans);
                tokens.add(list);
                lineNum.add(num);
            }else{
                word.add(ans);
                List list = new ArrayList();
                list.add(5);
                //TODO
                if(!hasDot) {
                    String binary = toBinary(Integer.parseInt(ans));
                    if(binary == "")
                        binary = "0";
                    list.add(binary + "<二进制>");
                } else{
                    list.add(ans);
                }
                tokens.add(list);
                lineNum.add(num);
                flag = true;
            }
        }

    }

    public void error(char ch, int num){
        errorInfo.add(ch+"");
        errorLineNum.add(num);
    }

    public void print(){
        System.out.println("单词         " + "类别          " + "值          ");
        for(int i = 0; i < word.size(); i++){
            if((Integer)tokens.get(i).get(0) != 7) {
                System.out.print(word.get(i) + "         ");
                System.out.print(Kind.values()[(Integer) tokens.get(i).get(0) - 1] + "          ");
                System.out.print(tokens.get(i).get(1) + "          ");
                System.out.println();
            } else{
                String ch = word.get(i);
                System.out.println(ch + "  is not standard input");
            }
        }
    }


}
