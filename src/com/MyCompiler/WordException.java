package com.MyCompiler;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by marco sun on 2017/11/27.
 */
public class WordException {
   public static void saveWordException(BufferedWriter bufferedWriter, List<String>errorInfo, List<Integer>errorLineNum) throws IOException{
       System.out.println("WordException\n");
       bufferedWriter.write("行号                 错误信息 \n");
       for(int i = 0; i < errorInfo.size(); i++) {
           String ch = (String) errorInfo.get(i);
           bufferedWriter.write(errorLineNum.get(i).toString() + "            ");
           bufferedWriter.write(ch + "  is not standard input\n");
       }
   }

   public static void printWordException(JTextArea jTextArear, List<String>errorInfo, List<Integer>errorLineNum) throws IOException{
       jTextArear.append("WordException\n");
       jTextArear.append("行号             错误信息 \n");
       for (int i = 0; i < errorInfo.size(); i++) {
           String ch = (String) errorInfo.get(i);
           jTextArear.append(errorLineNum.get(i).toString() + "            ");
           jTextArear.append("  " + ch + "  is not standard input\n");
       }
   }
}
