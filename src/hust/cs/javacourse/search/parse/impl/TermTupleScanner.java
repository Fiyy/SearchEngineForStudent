/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: TermTupleScanner
 * Author:   Administrator
 * Date:     2020/4/23 11:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Document;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个具体的TermTupleScanner对象就是
 *  一个AbstractTermTupleStream流对象，它利用java.io.BufferedReader去读取文本文件得到一个个三元组TermTuple.
 *
 * @author Administrator
 * @create 2020/4/23
 * @since 1.0.0
 */
public class TermTupleScanner extends AbstractTermTupleScanner {

    public TermTupleScanner() {}

    public TermTupleScanner(BufferedReader input) {
        super(input);
    }

    private int pos = 0;        //记录当前三元组单词的位置

    private int size = 0;       //记录当前单词表的大小

    private int pos1 = 0;       //当前单词在单词表中的位置

    private List<String> parts = new ArrayList<>(); //单词表

    AbstractTermTuple termTuple; //返回的三元组

    @Override
    public AbstractTermTuple next() {
        termTuple = new TermTuple();
        if(size != 0 && pos1 < size){
            termTuple.curPos = pos++;
            termTuple.term = new Term(parts.get(pos1++));
        }
        else{
            try {
                String string = input.readLine();
                while(string != null && string.isEmpty())
                    string = input.readLine();
                if(string == null){
                    this.close();
                    termTuple = null;
                }
                // System.out.print(string);
                else{
                    StringSplitter splitter = new StringSplitter();
                    splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
                    parts = splitter.splitByRegex(string);
                    pos1 = 0;
                    size = parts.size();
                    termTuple.curPos = pos++;
                    if(!parts.isEmpty())
                    termTuple.term = new Term(parts.get(pos1++));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return termTuple;
    }

    /**
     * 测试Scanner的功能，功能正确
     * 根据DocumentBuilder的bug2而来，测试功能数据集\1.txt,发生中断错误java.lang.NullPointerException，但是是将所有词输出之后产生的错误
     * 找到错误原因：当readLine读完后没有判断string是否为null而是直接使用isempty函数
     * @param args
     */
    public static void main(String args[]){
        List<AbstractTermTuple> tuples = new ArrayList<>();
        File file = new File("D:\\HUST\\java\\实验1\\功能测试数据集\\1.txt");
        AbstractDocument document = new Document();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            AbstractTermTupleStream termTupleScanner = new TermTupleScanner(reader);
            AbstractTermTuple termTuple1= new TermTuple();
            termTuple1 = termTupleScanner.next();
            while(termTuple1 != null){
                System.out.println(termTuple1.toString());
                termTuple1 = termTupleScanner.next();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}