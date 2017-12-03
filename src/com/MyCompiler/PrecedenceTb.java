package com.MyCompiler;

import java.util.List;

//返回一个二维数组表示算符优先矩阵，大小为(numVt+1)*(numVt+1)
public class PrecedenceTb {
    private int[][] tb; //左：栈内，右：栈外。 -1代表不存在，0代表小于，1代表大于，2代表等于
    private String[] grammar;
    private boolean[][] firstVt;
    private boolean[][] lastVt;
    private List<String> vn;
    private List<String> vt;
    private int numVt;

    public PrecedenceTb(String[] grammar, boolean[][] firstVt, boolean[][] lastVt, List<String>vn, List<String>vt){
        this.numVt = vt.size();
        this.tb = new int[numVt+1][numVt+1];
        this.grammar = grammar;
        this.firstVt = firstVt;
        this.lastVt = lastVt;
        this.vn = vn;
        this.vt = vt;
    }

    public int indexOf(List<String> v, char ch){
        for(int i = 0; i < v.size(); i++){
            if(v.get(i).equals(ch+"")){
                return i;
            }
        }
        return -1;
    }

    public int[][] getTb(){
        int len;
        String eachStr;
        //初始化
        for(int i = 0; i < numVt+1; i++)
            for(int j = 0; j < numVt+1; j++)
                tb[i][j] = -1;
        //处理#
        for(int i = 0; i < numVt; i++){
            if(!vt.get(i).equals(")"))
            tb[numVt][i] = 0;
            if(!vt.get(i).equals("("))
            tb[i][numVt] = 1;
        }


        for(int i = 0; i < grammar.length; i++){
            eachStr = grammar[i];
            len = eachStr.length();
            for(int j = 3; j < len - 1; j++){
                for(int k = 0; k < len; k++)
                    System.out.print(eachStr.charAt(k)+"  ");
                System.out.println();
                if(vt.contains(eachStr.charAt(j)+"")&&vt.contains(eachStr.charAt(j+1)+"")){
                    char leftChar = eachStr.charAt(j);
                    char rightChar = eachStr.charAt(j+1);
                    int left = indexOf(vt, leftChar);
                    int right = indexOf(vt, rightChar);
                    tb[left][right] = 2;
                }
                if((j+2<len)&&vt.contains(eachStr.charAt(j)+"")&&vt.contains(eachStr.charAt(j+2)+"")&&vn.contains(eachStr.charAt(j+1)+"")){
                    char leftChar = eachStr.charAt(j);
                    char rightChar = eachStr.charAt(j+2);
                    int left = indexOf(vt, leftChar);
                    int right = indexOf(vt, rightChar);
                    System.out.print(left+" ");
                    System.out.println(right);
                    tb[left][right] = 2;
                }
                if(vt.contains(eachStr.charAt(j)+"")&&vn.contains(eachStr.charAt(j+1)+"")){
                    char vnChar = eachStr.charAt(j+1);    //获取非终结符
                    int vnIndex = indexOf(vn, vnChar);    //非终结符的位置
                    char leftChar = eachStr.charAt(j);
                    int left = indexOf(vt, leftChar);
                    for(int right = 0; right < numVt; right++){
                        if(firstVt[vnIndex][right]){
                            tb[left][right] = 0;

                        }
                    }
                }
                if(vn.contains(eachStr.charAt(j)+"")&&vt.contains(eachStr.charAt(j+1)+"")){
                    char vnChar = eachStr.charAt(j);
                    int vnIndex = indexOf(vn, vnChar);
                    char rightChar = eachStr.charAt(j+1);
                    int right = indexOf(vt, rightChar);
                    for(int left = 0; left < numVt; left++){
                        if(lastVt[vnIndex][left]){
                            tb[left][right] = 1;
                            System.out.print("hahaha   ");

                            System.out.print(left+" ");
                            System.out.println(right);
                        }
                    }
                }
            }

        }
        return tb;
    }
}
