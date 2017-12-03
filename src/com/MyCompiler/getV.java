package com.MyCompiler;

import java.util.ArrayList;
import java.util.List;

public class getV {
    private List<String> v;
    private List<String> vl;
    private List<String> vn;
    private List<String> vt;

    public getV(List<String> v, List<String> vl){
        this.v = v; //所有符号，不重复
        this.vl = vl; //所有出现在左边的符号，可能重复
        vn = new ArrayList<>();
        vt = new ArrayList<>();
    }

    //getVn先调用，再调用getVt
    public  List<String> getVn(){
        for(String str : vl){
            if(!vn.contains(str)){
                vn.add(str);
            }
        }
        return vn;
    }

    public  List<String> getVt(){
        for(String str : v){
            if(!vn.contains(str)){
                vt.add(str);
            }
        }
        return vt;
    }


}
