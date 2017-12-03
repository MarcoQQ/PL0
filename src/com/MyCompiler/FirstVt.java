package com.MyCompiler;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class FirstVt {
    private String []grammar;
    private boolean [][] matrax; //first集
    private int numVn; //vn集的大小
    private int numVt; //vt集的大小
    private Stack<String[]> stack;

    public FirstVt(String []grammar, int numVn, int numVt){
        this.grammar = grammar;
        this.numVn = numVn;
        this.numVt = numVt;
        matrax = new boolean[numVn][numVt];
        stack = new Stack<>();
    }

    public char getLeft(String str){
        return str.charAt(0);
    }

    public char getRight(String str){
        return str.charAt(3);
    }

    public Character getRight(String str, List<String>vn, List<String> vt){
        if(vt.contains(str.charAt(3)+"")){
            return str.charAt(3);
        }else if(str.length()>4&&vn.contains(str.charAt(3)+"")&&vt.contains(str.charAt(4)+"")){
            return str.charAt(4);
        }
        return null;
    }

    public void doInsert(int left, int right, List<String> vn, List<String> vt){
        if(!matrax[left][right]){
            matrax[left][right] = true;
            String []str = new String[2];
            str[0] = vn.get(left);
            str[1] = vt.get(right);
            stack.push(str);
        }
    }

    public int indexOf(List<String> v, char ch){
        for(int i = 0; i < v.size(); i++){
            if(v.get(i).equals(ch+"")){
                return i;
            }
        }
        return -1;
    }

    public boolean[][] getFirstVt(List<String>vn, List<String> vt){
        int grammarSize = grammar.length;
        int left;
        int right;
        String V;
        String b;
        String[] str;

        for(int i = 0; i < numVn; i++)
            for(int j = 0; j < numVt; j++)
                matrax[i][j] = false;

        for(int i = 0; i < grammarSize; i++){
            if(getRight(grammar[i], vn, vt) != null) {
                char rightChar = getRight(grammar[i], vn, vt);
                char leftChar = getLeft(grammar[i]);
                if (!Character.isSpaceChar(rightChar)) {
                    left = indexOf(vn, leftChar);
                    right = indexOf(vt, rightChar);
                    doInsert(left, right, vn, vt);
                }
            }
        }

        while(!stack.isEmpty()){
            str = stack.pop();
            V = str[0];
            b = str[1];
            for(int i = 0; i < grammarSize; i++){
                char leftChar = getLeft(grammar[i]);
                char rightChar = getRight(grammar[i]);
                if((rightChar+"").equals(V)){
                    left = indexOf(vn, leftChar);
                    right = indexOf(vt, b.charAt(0));
                    doInsert(left, right, vn, vt);
                }
            }
        }
        return matrax;
    }
}
