/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: IndexSearcher
 * Author:   Administrator
 * Date:     2020/4/29 10:09
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.index.impl.*;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 *  实现具体的检索类
 *
 * @author ZhangYun
 * @create 2020/4/29
 * @since 1.0.0
 */
public class IndexSearcher extends AbstractIndexSearcher {

    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     * 其实就是反序列化
     * 这里是否需要进行文件存在检测？？？？？？
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        try {
            index.load(new File(indexFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据单个检索词进行搜索
     * 只对单个词进行检索
     * 有要在程序中构造出一个Hit数组
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        ArrayList<AbstractHit> hits = new ArrayList<>(); //最终的返回数组
        //获取查询的postingList
        AbstractPostingList postingList = index.search(queryTerm);
        if(postingList == null || postingList.isEmpty()) return null; //其实不需要检测是否为空，search直接返回了null
        for(int i = 0; i< postingList.size(); i++){
            AbstractPosting posting = postingList.get(i);
            String filepath = index.getDocName(posting.getDocId());
            Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
            termPostingMapping.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), filepath, termPostingMapping);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }
        sorter.sort(hits);
        AbstractHit[] hits1 = new AbstractHit[hits.size()];
        return hits.toArray(hits1);
    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        ArrayList<AbstractHit> hits = new ArrayList<>(); //最终的返回数组
        if(combine == LogicalCombination.ADN){
            //获取查询的postingList
            AbstractPostingList postingList1 = index.search(queryTerm1);
            AbstractPostingList postingList2 = index.search(queryTerm2);

            if(postingList1 == null || postingList1.isEmpty()) return null; //其实不需要检测是否为空，search直接返回了null
            // 第一个查询词的列表中找第二个词是否有相同的文档
            for(int i = 0; i< postingList1.size(); i++){
                AbstractPosting posting1 = postingList1.get(i);

                int pos = ((PostingList)postingList2).indexOf(posting1.getDocId()); // pos记录相同文档在第二个词的PostingList中的位置s
                //如果第二个词的文档和第一个词的文档有相同，向hits中加入该文档
                if(pos != -1){
                    String filepath = index.getDocName(posting1.getDocId());

                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting1);
                    termPostingMapping.put(queryTerm2, postingList2.get(pos));

                    AbstractHit hit = new Hit(posting1.getDocId(), filepath, termPostingMapping);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                }
            }
        }
        else if(combine == LogicalCombination.OR){
            //获取查询的postingList
            AbstractPostingList postingList1 = index.search(queryTerm1);
            AbstractPostingList postingList2 = index.search(queryTerm2);
            if((postingList1 == null || postingList1.isEmpty()) && (postingList2 == null || postingList2.isEmpty())) return null; //其实不需要检测是否为空，search直接返回了null
            // 第一个查询词的列表中找第二个词是否有相同的文档
            for(int i = 0; i < postingList1.size(); i++){
                AbstractPosting posting1 = postingList1.get(i);
                int pos = ((PostingList)postingList2).indexOf(posting1.getDocId());
                String filepath = index.getDocName(posting1.getDocId());

                if(pos == -1){ // 只有List1中有
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting1);
                    AbstractHit hit = new Hit(posting1.getDocId(), filepath, termPostingMapping);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                }
                else{   // List1和List2中都有

                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting1);
                    termPostingMapping.put(queryTerm2, postingList2.get(pos));

                    AbstractHit hit = new Hit(posting1.getDocId(), filepath, termPostingMapping);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                }
            }
            for(int i = 0; i < postingList2.size(); i++){
                AbstractPosting posting2 = postingList2.get(i);
                int pos = ((PostingList)postingList1).indexOf(posting2.getDocId());
                String filepath = index.getDocName(posting2.getDocId());

                if(pos == -1){ // 只有List2中有
                    Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<>();
                    termPostingMapping.put(queryTerm1, posting2);
                    AbstractHit hit = new Hit(posting2.getDocId(), filepath, termPostingMapping);
                    hit.setScore(sorter.score(hit));
                    hits.add(hit);
                }
                // 如果List1 和 List2都有，则不加入
            }
        }
        sorter.sort(hits);
        return (AbstractHit[])hits.toArray();
    }

    /**
     * 测试搜索功能，程序正确
     * @param args
     */
    public static void main(String args[]){
        AbstractDocumentBuilder documentBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(documentBuilder);
        AbstractIndex index = indexBuilder.buildIndex("D:\\HUST\\java\\exp1\\功能测试数据集");
        index.save(new File("D:\\HUST\\java\\exp1\\index_save.txt"));
        ((Index)index).showTermToPostingListMapping();
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open("D:\\HUST\\java\\exp1\\index_save.txt");
        AbstractTerm term = new Term("activity");
        SimpleSorter sorter = new SimpleSorter();
        System.out.println(Arrays.toString(indexSearcher.search(term, sorter)));
        System.out.println(indexSearcher.search(term, sorter)[0].compareTo(indexSearcher.search(term, sorter)[1]));

    }
}