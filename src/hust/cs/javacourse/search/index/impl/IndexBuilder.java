/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: IndexBuilder
 * Author:   Administrator
 * Date:     2020/4/24 9:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static hust.cs.javacourse.search.util.FileUtil.list;

/**
 * IndexBuilder是索引构造器类
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class IndexBuilder extends AbstractIndexBuilder {

    /**
     * 构造函数中舒适化docBuilder
     * @param docBuilder ：文档构造器
     */
    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();
        List<String> fileList = new ArrayList<>();
        fileList = FileUtil.list(rootDirectory);
        int docId = 0;

        for(String filePath : fileList){
            File file = new File(filePath);
            AbstractDocument document = docBuilder.build(docId++,filePath,file);
            //测试是否能成功获取document
            //System.out.println(document.getTuples());
            //①出现问题，程序
            index.addDocument(document);
            //测试是否可以成功添加，发现没有添加document到index的三元组映射中，bug在addDocument中
            //((Index)index).showTermToPostingListMapping();
        }
        index.optimize();
        return index;
    }

    /**
     * 测试indexBuilder是否能成功创建index
     * 出现bug1，生成的index的第二个map没有内容
     * 找到bug，addDocument函数中没有写map不包含term的部分
     *
     * 我在功能测试集增加4.txt和5.txt，测试出现频率和文档编号和所在位置，测试结果正确
     * @param args
     */
    public static void main(String args[]){
        DocumentBuilder documentBuilder = new DocumentBuilder();
        IndexBuilder indexBuilder = new IndexBuilder(documentBuilder);
        String root = "D:\\HUST\\java\\实验1\\功能测试数据集";
        AbstractIndex index = indexBuilder.buildIndex(root);
        ((Index)index).optimize(); //测试排序
        ((Index)index).showTermToPostingListMapping();
        //System.out.println(index.toString());
    }
}