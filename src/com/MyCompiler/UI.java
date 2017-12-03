package com.MyCompiler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by marco sun on 2017/11/25.
 */
public class UI extends JFrame{
    //打开的文件的名字
    private String fileName;
    //保存的文件的名字
    private String saveName;
    //左文本区域
    private JTextArea jTextAreal;
    //右文本区域
    JTextArea jTextArear;
    //每个单词名
    private List<String> word = new ArrayList();
    //单词的信息
    private List<List> tokens = new ArrayList();
    //正确行号
    private List<Integer>lineNum = new ArrayList();
    //错误行号
    private List<Integer>errorLineNum = new ArrayList<>();
    //错误单词
    private List<String>errorInfo = new ArrayList<>();

    private enum Kind{
        RESERVER, IDENTIFIER, UNARYOPERATOR, BINARYOPERATOR, CONSTANT, DELIMITER, FLOAT
    }
    private static Hashtable<Kind, String> hashtable = new Hashtable<>();

    static{
        hashtable.put(Kind.RESERVER, "保留字");
        hashtable.put(Kind.IDENTIFIER, "标识符");
        hashtable.put(Kind.UNARYOPERATOR, "一元运算符");
        hashtable.put(Kind.BINARYOPERATOR, "二元运算符");
        hashtable.put(Kind.CONSTANT, "常数");
        hashtable.put(Kind.DELIMITER, "分界符");
        hashtable.put(Kind.FLOAT, "浮点数");
    }

    public UI(){
        super();
        Toolkit toolkit;
        Dimension screenSize;
        JPanel jPanel;
        //左内容面板
        JScrollPane jScrollPanel;
        //右内容面板
        JScrollPane jScrollPaner;

        //按钮面板
        JPanel buttonJPanel;
        //提交按钮
        JButton submit;
        JMenuBar jMenuBar;
        int width;
        int height;
        int w;
        int h;

        //工具
        toolkit = Toolkit.getDefaultToolkit();
        screenSize = toolkit.getScreenSize();
        setSize(800,600);
        width = screenSize.width;
        height = screenSize.height;
        w = getWidth();
        h =getHeight();
        //窗口的位置
        setLocation((width - w) / 2, (height - h) / 2);
        setTitle("Marco词法分析器");
        //内容面板
        jPanel = (JPanel)getContentPane();
        //无法最大化；
        setResizable(false);
//        JMenuBar = create
        jScrollPanel = new JScrollPane(jTextAreal = new JTextArea("要分析的代码: " + "\n", 1000, 26));
        jScrollPaner = new JScrollPane(jTextArear = new JTextArea("词法分析结果如下:" + "\n", 1000, 26));
        jTextAreal.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        jTextArear.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        buttonJPanel = new JPanel();
        submit = new JButton("开始词法分析");
        submit.addActionListener(new start());
        buttonJPanel.add(submit);
        jPanel.add(jScrollPanel, BorderLayout.WEST);
        jPanel.add(jScrollPaner, BorderLayout.EAST);
        jPanel.add(buttonJPanel, BorderLayout.SOUTH);
        jMenuBar = createJMenuBar();
        setJMenuBar(jMenuBar);

        //关闭时退出
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jTextAreal.setEditable(false);
        jTextArear.setEditable(false);
        //可见
        setVisible(true);

    }

    public class start implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
            jTextArear.setText("词法分析结果如下: \n");

            try{
                if(fileName != null) {

                    Impl impl = new Impl();
                    impl.Analysis(fileName);
                    errorInfo = impl.getErrorInfo();
                    errorLineNum = impl.getErrorLineNum();
                    if (errorInfo.isEmpty()) {
                        word = impl.getWord();
                        tokens = impl.getTokens();
                        lineNum = impl.getLineNum();

                        jTextArear.append("行号            单词         类别          值          \n");
                        for (int i = 0; i < word.size(); ++i) {
                            jTextArear.append(lineNum.get(i).toString() + "            ");
                            jTextArear.append((String) word.get(i) + "         ");
                            jTextArear.append(hashtable.get(Kind.values()[((Integer) ((List) tokens.get(i)).get(0)).intValue() - 1]) + "          ");
                            jTextArear.append(((List) tokens.get(i)).get(1) + "          ");
                            jTextArear.append("\n");
                        }
                    } else {
                        WordException.printWordException(jTextArear, errorInfo, errorLineNum);
                    }
                } else{
                    //TODO
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        }


    private JMenuBar createJMenuBar(){
        JMenuBar jMenuBar = new JMenuBar();
        JMenu fMenu = new JMenu("file");
        JMenu iMenu = new JMenu("Instruction");
        JMenuItem oMenu = new JMenuItem("Open");
        JMenuItem sMenu = new JMenuItem("Save");
        JMenuItem eMenu = new JMenuItem("Exit");
        //打开文件事件
        oMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jTextAreal.setText("要分析的代码: \n");
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("请选择要分析的代码");
                int ret = jFileChooser.showOpenDialog(null);
                //如果选择了文件
                if(JFileChooser.APPROVE_OPTION == ret){
                    FileReader fileReader = null;
                    BufferedReader bufferedReader = null;
                    String line;
                    System.out.println(jFileChooser.getSelectedFile());
                    fileName = jFileChooser.getSelectedFile().toString();
                    File infile = new File(fileName);
                    try{
                        fileReader = new FileReader(fileName);
                        bufferedReader = new BufferedReader(fileReader);
                        while((line = bufferedReader.readLine()) != null){
                            jTextAreal.append(line + "\n");
                        }
                    } catch (Exception e1){
                        e1.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                            fileReader.close();
                        }catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }
                }

            }
        });
        //保存文件
        sMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(word.isEmpty()){
                    //TODO
                }
                FileWriter fileWriter = null;
                BufferedWriter bufferedWriter = null;
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("保存");
                int ret = jFileChooser.showSaveDialog(null);
                if(ret ==  JFileChooser.APPROVE_OPTION) {
                    System.out.println(jFileChooser.getSelectedFile());
                    saveName = jFileChooser.getSelectedFile().toString();
                    try {
                        fileWriter = new FileWriter(saveName);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        if(errorInfo.isEmpty()) {
                            bufferedWriter.write("行号                 单词         类别          值          \n");
                            for (int i = 0; i < word.size(); ++i) {
                                bufferedWriter.write(lineNum.get(i).toString() + "            ");
                                bufferedWriter.write((String) word.get(i) + "         ");
                                bufferedWriter.write(hashtable.get(Kind.values()[((Integer) ((List) tokens.get(i)).get(0)).intValue() - 1]) + "          ");
                                bufferedWriter.write(((List) tokens.get(i)).get(1) + "          ");
                                bufferedWriter.newLine();
                            }
                        } else{
                            WordException.saveWordException(bufferedWriter, errorInfo, errorLineNum);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            bufferedWriter.close();
                            fileWriter.close();
                        } catch(Exception e2){
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
        //退出程序
        eMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //退出并释放资源
                          UI.this.dispose();
            }}
        );
        fMenu.add(oMenu);
        fMenu.addSeparator();
        fMenu.add(sMenu);
        fMenu.addSeparator();
        fMenu.add(eMenu);
        fMenu.addSeparator();
        jMenuBar.add(fMenu);
        jMenuBar.add(iMenu);
        return jMenuBar;
    }
}
