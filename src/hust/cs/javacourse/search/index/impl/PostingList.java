/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: PostingList
 * Author:   Administrator
 * Date:     2020/4/24 17:21
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class PostingList extends AbstractPostingList {

    /**
     * 缺省构造函数,必须是共有，否则测试程序没权限
     */
    public PostingList(){}

    /**
     * 添加Posting,要求不能有内容重复的posting
     * @param posting ：Posting对象
     */
    @Override
    public void add(AbstractPosting posting) {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(posting)) return;
        }
        list.add(posting);
    }

    /**
     * 获得PosingList的字符串表示
     * @return ： PosingList的字符串表示
     */
    @Override
    public String toString() {
        String string = new String();
        for(int i = 0; i < list.size(); i++){
            string += list.get(i).toString() + '\n';
        }
        return string;
    }

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * 复用参数为posting的add函数
     * @param postings ：Posting列表
     */
    @Override
    public void add(List<AbstractPosting> postings) {
        for(int i = 0; i < postings.size(); i++){
            this.add(postings.get(i));
        }
    }

    /**
     * 返回指定下标位置的Posting
     *
     * @param index ：下标
     * @return： 指定下标位置的Posting
     */
    @Override
    public AbstractPosting get(int index) {
        if(index < 0 || index >= list.size()) return null;
        return list.get(index);
    }

    /**
     * 返回指定Posting对象的下标
     *
     * @param posting ：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(AbstractPosting posting) {
        if(list.contains(posting)) return list.indexOf(posting);
        return -1;
    }

    /**
     * 返回指定文档id的Posting对象的下标
     *
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    @Override
    public int indexOf(int docId) {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getDocId() == docId)
                return i;
        }
        return -1;
    }

    /**
     * 是否包含指定Posting对象
     *
     * @param posting ： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    @Override
    public boolean contains(AbstractPosting posting) {
        return list.contains(posting);
    }

    /**
     * 删除指定下标的Posting对象
     *
     * @param index ：指定的下标
     */
    @Override
    public void remove(int index) {
        if(index >=0 && index < list.size())
            list.remove(index);
    }

    /**
     * 删除指定的Posting对象
     *
     * @param posting ：定的Posting对象
     */
    @Override
    public void remove(AbstractPosting posting) {
        if(list.contains(posting))
            list.remove(posting);
    }

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     *
     * @return ：PostingList的大小
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * 清除PostingList
     */
    @Override
    public void clear() {
        list.clear();
    }

    /**
     * PostingList是否为空
     *
     * @return 为空返回true;否则返回false
     */
    @Override
    public boolean isEmpty() {
        if(list == null) return true;
        else return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    @Override
    public void sort() {
        Collections.sort(list);
    }

    /**
     * 写到二进制文件
     * 一定要添加List的大小，否则无法读取
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            list = (List<AbstractPosting>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String...args)
    {
        PostingList postingList = new PostingList();
        ArrayList<Integer> postions1 = new ArrayList();
        for(int i= 1;i<5;i++)
            postions1.add(i);
        Posting posting1 = new Posting(1,4, postions1);
        ArrayList<Integer> postions2 = new ArrayList();
        postions2.add(2);postions2.add(4);postions2.add(3);postions2.add(1);
        Posting posting2 = new Posting(2,4,postions2);
        postingList.add(posting1);
        postingList.add(posting2);

        // 测试writeObject
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("D:\\HUST\\java\\实验1\\test_postinglist.txt")));
            postingList.writeObject(objectOutputStream);
            for(int i = 0; i < postingList.size(); i++){
                System.out.println(postingList.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 测试readObject
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("D:\\HUST\\java\\实验1\\test_postinglist.txt")));
            PostingList postingList1 = new PostingList();
            postingList1.readObject(objectInputStream);
            System.out.println("The test readObject is:\n" + postingList1.toString());
            //该部分说明Posting的序列化函数没问题，问题在于PostingList的序列化
//            try {
//                int a = (Integer)objectInputStream.readObject();
//                System.out.println("a is: " + a);
//                Posting postingz = new Posting();
//                postingz.readObject(objectInputStream);
//                System.out.println("postingz is:\n" + postingz);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}