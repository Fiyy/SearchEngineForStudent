/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: Hit
 * Author:   Administrator
 * Date:     2020/4/29 10:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Hit是一个搜索命中结果的类. 该类要实现Comparable接口.
 * 实现该接口是因为需要必须比较大小，用于命中结果的排序.
 *
 * @author Administrator
 * @create 2020/4/29
 * @since 1.0.0
 */
public class Hit extends AbstractHit {

    /**
     * 默认构造函数
     */
    public Hit() {
        this.score = 1;
    }

    /**
     * 构造函数
     * @param docId 文档ID
     * @param docPath   文档绝对路径
     */
    public Hit(int docId, String docPath){
        super(docId, docPath);
        this.score = 1;
    }

    /**
     * 构造函数
     * @param docId 文档ID
     * @param docPath   文档绝对路径
     * @param termPostingMapping    单词键值对
     */
    public Hit(int docId, String docPath, Map<AbstractTerm, AbstractPosting> termPostingMapping){
        super(docId, docPath, termPostingMapping);
        this.score = 1;
    }

    /**
     * 获得文档id
     *
     * @return ： 文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 获得文档绝对路径
     *
     * @return ：文档绝对路径
     */
    @Override
    public String getDocPath() {
        return this.docPath;
    }

    /**
     * 获得文档内容
     *
     * @return ： 文档内容
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /**
     * 设置文档内容
     *
     * @param content ：文档内容
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获得文档得分
     *
     * @return ： 文档得分
     */
    @Override
    public double getScore() {
        return this.score;
    }

    /**
     * 设置文档得分
     *
     * @param score ：文档得分
     */
    @Override
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * 获得命中的单词和对应的Posting键值对
     * 这里直接返回引用，后续根据是否需要深拷贝而修改程序
     * @return ：命中的单词和对应的Posting键值对
     */
    @Override
    public Map<AbstractTerm, AbstractPosting> getTermPostingMapping() {
        return this.termPostingMapping;
    }

    /**
     * 获得命中结果的字符串表示, 用于显示搜索结果.
     *
     * @return : 命中结果的字符串表示
     */
    @Override
    public String toString() {
        String string = new String();
        string += "docId: " + this.docId +'\n';
        string += "docPath: " + this.docPath +'\n';
        string += "content: " + this.content + '\n';
        string += "scores: " + this.score + '\n';
        string += termPostingMapping.toString();
        return string;
    }

    /**
     * 比较二个命中结果的大小，根据score比较
     *
     * @param o ：要比较的名字结果
     * @return ：二个命中结果得分的差值
     */
    @Override
    public int compareTo(AbstractHit o) {
        //为了使排序结果为大到小，变为负值
        if(o!=null)
            return (int)Math.ceil(this.score - o.getScore());
        else return (int)Math.ceil(this.score);
    }

    public static void main(String args[]){
        //测试putall是不是深拷贝：putall只是将引用拷贝放入map中
        Map<Integer, AbstractTerm> map = new HashMap<>();
        Map<Integer, AbstractTerm> map1 = new HashMap<>();
        AbstractTerm t1= new Term("dfasd");
        map.put(1,t1);
        System.out.println(map);
        map1.putAll(map);
        t1.setContent("1");
        System.out.println(map);
        System.out.println(map1);
        map1.get(1).setContent("123");
        System.out.println(map);
    }
}