/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: Index
 * Author:   Administrator
 * Date:     2020/4/24 9:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import org.w3c.dom.views.AbstractView;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Index是内存中的倒排索引类
 * 一个倒排索引对象包含了一个文档集合的倒排索引.
 * 内存中的倒排索引结构为HashMap，key为Term对象，value为对应的PostingList对象.
 * 另外在AbstractIndex里还定义了从docId和docPath之间的映射关系.
 * 必须实现下面接口:
 * FileSerializable：可序列化到文件或从文件反序列化.
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class Index extends AbstractIndex {

    /**
     * 用来记录文档数，其实就是对于文档的ID
     */
    static int numOfDocId = 0;

    /**
     * 返回索引的字符串表示
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        this.optimize();
        String string = new String();
        string += docIdToDocPathMapping.toString();
        string += termToPostingListMapping.toString();
        return string;
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        if(document.getTupleSize() == 0) return;
        if(!docIdToDocPathMapping.containsKey(document.getDocId())){
            docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        }
        for(int i = 0;i<document.getTuples().size();i++){
            AbstractTermTuple termTuple = new TermTuple();
            termTuple = document.getTuple(i);
            //System.out.println(termTuple);
            if(termToPostingListMapping.containsKey(termTuple.term)){
                AbstractPostingList postingList = termToPostingListMapping.get(termTuple.term);
                if(postingList.indexOf(document.getDocId()) == -1){
                    AbstractPosting posting = new Posting();
                    posting.setDocId(document.getDocId());
                    posting.setFreq(1);
                    posting.getPositions().add(termTuple.curPos);
                    postingList.add(posting);
                }
                else{
                    AbstractPosting posting = postingList.get(postingList.indexOf(document.getDocId()));
                    posting.setFreq(posting.getFreq() + 1);
                    posting.getPositions().add(termTuple.curPos);
                }
            }
            //找到bug，发现addDocument中没写map中不包含term的情况
            else{
                AbstractPostingList postingList = new PostingList();
                termToPostingListMapping.put(termTuple.term, postingList);
                AbstractPosting posting = new Posting();
                posting.setDocId(document.getDocId());
                posting.setFreq(1);
                posting.getPositions().add(termTuple.curPos);
                postingList.add(posting);
            }
        }
    }

    /**
     *  显示index中的term和对应的PostingList内容
     *  便于其他程序的测试
     */
    public void showTermToPostingListMapping(){
        this.optimize();
        System.out.println("测试 - TermToPostingListMapping大小为" + termToPostingListMapping.size());
        for(AbstractTerm term: termToPostingListMapping.keySet())
        {
            System.out.println(term.getContent());
            System.out.println(termToPostingListMapping.get(term));
        }
    }

    /**
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     */
    @Override
    public void load(File file){
        ObjectInputStream in = null;
        try {
            if(file.isFile()) {
                in = new ObjectInputStream(new FileInputStream(file));
                this.readObject(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     */
    @Override
    public void save(File file) {
        try {
            if(file.isFile())
            {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                this.writeObject(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return (PostingList)termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的集合
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        Set<AbstractTerm> set = new HashSet<>();
        for(AbstractTerm abstractTerm: termToPostingListMapping.keySet()){
            set.add(abstractTerm);
        }
        return set;
    }

    /**
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     */
    @Override
    public void optimize() {
        for(AbstractPostingList postingList : termToPostingListMapping.values()){
            postingList.sort();
            for(int i = 0; i < postingList.size(); i++){
                postingList.get(i).sort();
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     * 如果没有对应Id就返回null
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        if(docIdToDocPathMapping.containsKey(docId))
            return docIdToDocPathMapping.get(docId);
        else return "";
    }

    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        this.optimize();
        try {
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            docIdToDocPathMapping = (Map<Integer, String>) in.readObject();
            termToPostingListMapping = (Map<AbstractTerm, AbstractPostingList>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * IndexBuilder中发现addDocument有问题，不能成功添加三元组到map中，始终为空白
     * 该函数中进行测试
     * 找到bug，发现addDocument中没写map中不包含term的情况
     * @param args
     */
    public static void main(String...args){
        Index index = new Index();
        index.save(new File("D:\\HUST\\java\\实验1\\test_save.txt"));
        Index index1 = new Index();
        index1.load(new File("D:\\HUST\\java\\实验1\\test_save.txt"));
        index1.showTermToPostingListMapping();

        // 测试save函数和load函数
        File file = new File("D:\\HUST\\java\\实验1\\test_write.txt");
        index.save(file);
        //index.showTermToPostingListMapping();
        // index显示正常,load函数正确

        /*        // 直接使用已经序列化好的文件进行测试反序列化的功能
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("D:\\HUST\\java\\实验1\\test_write.txt"));
            Index index = new Index();
            index.readObject(inputStream);
            System.out.println(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        /*
        DocumentBuilder documentBuilder = new DocumentBuilder();
        String filePath = "D:\\HUST\\java\\实验1\\真实测试数据集\\1.txt";
        File file = new File(filePath);
        AbstractDocument document = documentBuilder.build(0,filePath,file);
        //System.out.println(document.getTuples());
        Index index = new Index();

        index.addDocument(document);
        index.optimize();
        //index.showTermToPostingListMapping();
        //System.out.println(index.getDictionary());
        //System.out.println(index.getDocName(0));

        // 测试序列化和反序列化
        try {
            ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream("D:\\HUST\\java\\实验1\\test_write.txt"));
            index.writeObject(oss);
            //System.out.println(index);
            // 成功写入，测试反序列化读入
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("D:\\HUST\\java\\实验1\\test_write.txt"));
            Index index1 = new Index();
            index1.readObject(inputStream);
            index1.showTermToPostingListMapping();
            // 测试可知程序执行正确
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }
}