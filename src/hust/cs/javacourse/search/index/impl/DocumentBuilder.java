/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: DocumentBuilder
 * Author:   Administrator
 * Date:     2020/4/24 9:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.query.AbstractHit;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * DocumentBuilder是Document构造器.
 * Document构造器的功能应该是由解析文本文档得到的TermTupleStream，产生Document对象.
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class DocumentBuilder extends AbstractDocumentBuilder {

    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {
        AbstractDocument document = new Document();
        File file = new File(docPath);
        if(!file.isFile()) return document;
        if(termTupleStream == null) return document;
        List<AbstractTermTuple> tuples = new ArrayList<>();
        // 更换后
        // AbstractTermTupleStream abstractTermTupleStream2 = new PatternTermTupleFilter(termTupleStream);
        AbstractTermTuple temp = termTupleStream.next();
        while(temp!=null){
            tuples.add(temp);
            temp = termTupleStream.next();
            //temp = termTupleStream.next(); //直接将不过滤的版本输出
        }
        document = new Document(docId,docPath,tuples);
        return document;
    }


    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    @Override
    public AbstractDocument build(int docId, String docPath, File file) {
        AbstractDocument document = new Document();
        if(file == null) return document;
        if(!file.isFile() || docPath == null || docPath.equals(""))
            return document;
        List<AbstractTermTuple> tuples = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            AbstractTermTupleStream termTupleScanner = new TermTupleScanner(reader);
            AbstractTermTupleStream abstractTermTupleStream = new StopWordTermTupleFilter(termTupleScanner);
            AbstractTermTupleStream abstractTermTupleStream1 = new PatternTermTupleFilter(abstractTermTupleStream);
            AbstractTermTupleStream abstractTermTupleStream2 = new LengthTermTupleFilter(abstractTermTupleStream1);
            document = this.build(docId,docPath, abstractTermTupleStream2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return document;
        }
    }

    /**
     * 测试documentBuilder的功能
     * 测试出indexBuilder的bug1，document中的Tuple为空:原因未实现StopWordTermTupleFilter中的next函数
     * 测试出现bug2： 加载功能测试数据集\1.txt时，产生空的document文件，没有任何词组，问题应该出在Scanner中
     * bug2解决：问题是Scanner中，已经找到问题并解决
     * @param args
     */
    public static void main(String args[]){
        DocumentBuilder documentBuilder = new DocumentBuilder();
        String filePath = "D:\\HUST\\java\\exp1\\功能测试数据集\\2.txt";
        File file = new File(filePath);
        //file = null;
        AbstractDocument document = documentBuilder.build(0,filePath,file);
        System.out.println(document.getTuples());
    }
}