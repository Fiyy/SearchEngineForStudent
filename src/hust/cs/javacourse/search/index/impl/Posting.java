/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: Posting
 * Author:   Administrator
 * Date:     2020/4/24 9:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *          Posting对象代表倒排索引里每一项， 一个Posting对象包括:
 *          包含单词的文档id.
 *          单词在文档里出现的次数.
 *          单词在文档里出现的位置列表（位置下标不是以字符为编号，而是以单词为单位进行编号.
 *          @author Administrator
 *          @create 2020/4/24
 *          @since 1.0.0
 */
public class Posting extends AbstractPosting {

    /**
     * 缺省构造函数
     */
    public Posting() {}

    /**
     * 构造函数，直接调用父类构造函数
     * @param docId：文档ID
     * @param freq： 出现频率
     * @param positions： 出现的位置序列表
     */
    public Posting(int docId, int freq, List<Integer> positions){
        super(docId,freq,positions);
    }

    /**
     * 判断二个Posting内容是否相同
     *
     * @param obj ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Posting){
            if(docId == ((Posting) obj).docId && freq == ((Posting) obj).freq)
                return true;
            else return false;
        }
        return false;
    }

    /**
     * 返回Posting的字符串表示
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        String string = new String();
        string += "docId: " + String.valueOf(this.docId) + "    ";
        string += "freq: " + String.valueOf(freq) + "\n";
        for(int i= 0;i<positions.size();i++){
            string += positions.get(i).toString() + "--->";
        }
        string += '\n';
        return string;
    }

    /**
     * 返回包含单词的文档id
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 设置包含单词的文档id
     * @param docId ：包含单词的文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * 返回单词在文档里出现的次数
     * @return ：出现次数
     */
    @Override
    public int getFreq() {
        return freq;
    }

    /**
     * 设置单词在文档里出现的次数
     * @param freq :单词在文档里出现的次数
     */
    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    /**
     * 返回单词在文档里出现的位置列表
     * @return ：位置列表
     */
    @Override
    public List<Integer> getPositions() {
        return positions;
    }

    /**
     * 设置单词在文档里出现的位置列表
     * @param positions ：单词在文档里出现的位置列表
     */
    @Override
    public void setPositions(List<Integer> positions) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < positions.size(); i++){
            list.add(positions.get(i));
        }
        this.positions = list;
    }

    /**
     * 比较二个Posting对象的大小（根据docId）
     * @param o ： 另一个Posting对象
     * @return ：二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o) {
        if(o == this) return 0;
        return (this.docId - o.getDocId());
    }

    /**
     * 对内部positions从小到大排序
     * 直接使用Collections中的静态方法sort
     */
    @Override
    public void sort() {
        Collections.sort(positions);
    }

    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(docId);
            out.writeObject(freq);
            out.writeObject(positions);
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
            docId = (Integer)in.readObject();
            freq = (Integer)in.readObject();
            positions = (ArrayList<Integer>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public void main(String...args)
    {
        PostingList postingList = new PostingList();
        ArrayList<Integer> postions1 = new ArrayList();
        for(int i= 1;i<5;i++)
            postions1.add(i);
        Posting posting1 = new Posting(1,4, postions1);
        ArrayList<Integer> postions2 = new ArrayList();
        postions2.add(2);postions2.add(4);postions2.add(3);postions2.add(1);
        Posting posting2 = new Posting(1,4,postions2);
        postingList.add(posting1);
        postingList.add(posting2);
        System.out.println(postingList.toString());
    }
}