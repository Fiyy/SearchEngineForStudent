/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: Document
 * Author:   Administrator
 * Date:     2020/4/24 9:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档对象是解析一个文本文件得到结果，文档对象里面包含：
 *              文档id.
 *              文档的绝对路径.
 *              文档包含的三元组对象列表，一个三元组对象是抽象类AbstractTermTuple的子类实例
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class Document extends AbstractDocument {

    /**
     * 缺省构造函数
     */
    public Document() {}

    /**
     * 不包含三元组列表的构造函数
     * 直接调用父类函数
     * @param docPath   文档绝对路径
     * @param docId     文档ID
     */
    public Document(int docId,String docPath){
        super(docId, docPath);
    }

    /**
     * 包含三元组对象链表的构造函数
     * 直接调用父类函数
     * @param docId     文档id
     * @param docPath   文档绝对路径
     * @param tuples    三元组列表
     */
    public Document(int docId, String docPath, List<AbstractTermTuple> tuples){
        super(docId,docPath,tuples);
    }

    /**
     * 获得文档id
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return docId;
    }

    /**
     * 设置文档id
     * @param docId ：文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * 获得文档绝对路径
     * @return ：文档绝对路径
     */
    @Override
    public String getDocPath() {
        return this.docPath;
    }

    /**
     * 设置文档绝对路径
     * @param docPath ：文档绝对路径
     */
    @Override
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    /**
     * 获得文档包含的三元组列表
     * 这里使用对list进行深拷贝，但是list的每个元素还是对象的引用，所以其实还是浅拷贝
     * @return ：文档包含的三元组列表
     */
    @Override
    public List<AbstractTermTuple> getTuples() {
        List<AbstractTermTuple> list = new ArrayList<>();
        for(int i = 0;i<tuples.size();i++){
            list.add(tuples.get(i));
        }
        return list;
    }

    /**
     * 向文档对象里添加三元组, 要求不能有内容重复的三元组
     * 需要遍历tuples，如果有相同内容的三元组就结束函数，遍历结束后添加tuple
     * @param tuple ：要添加的三元组
     */
    @Override
    public void addTuple(AbstractTermTuple tuple) {
        for(int i  = 0; i < tuples.size(); i++){
            if(tuples.get(i).equals(tuple)) return;
        }
        tuples.add(tuple);
    }

    /**
     * 判断是否包含指定的三元组
     * @param tuple ： 指定的三元组
     * @return ： 如果包含指定的三元组，返回true;否则返回false
     */
    @Override
    public boolean contains(AbstractTermTuple tuple) {
        return tuples.contains(tuple);
    }

    /**
     * 获得指定下标位置的三元组
     * 要注意下标值的范围
     * @param index ：指定下标位置
     * @return：三元组
     */
    @Override
    public AbstractTermTuple getTuple(int index) {
        if(index<0 || index >= tuples.size()) return null;
        return tuples.get(index);
    }

    /**
     * 返回文档对象包含的三元组的个数
     *
     * @return ：文档对象包含的三元组的个数
     */
    @Override
    public int getTupleSize() {
        return tuples.size();
    }

    /**
     * 获得Document的字符串表示
     *
     * @return ： Document的字符串表示
     */
    @Override
    public String toString() {
        String string = new String();
        string += ("docId: " + String.valueOf(docId) + '\n');
        string += ("docPath: " + docPath + '\n');
        for(int i=0;i<tuples.size();i++){
            string += (tuples.get(i).toString() + '\n');
        }
        return string;
    }
}