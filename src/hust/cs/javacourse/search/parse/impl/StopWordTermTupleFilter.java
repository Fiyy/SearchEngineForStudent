/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: StopWordTermTupleFilterImpl
 * Author:   Administrator
 * Date:     2020/4/24 9:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Document;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 〈一句话功能简述〉<br> 
 * 〈停用词过滤器〉
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class StopWordTermTupleFilter extends AbstractTermTupleFilter {

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple termTuple = input.next();
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(StopWords.STOP_WORDS));
        while(termTuple != null && strings.contains(termTuple.term.getContent()))
            termTuple = input.next();
        return termTuple;
    }

    /**
     * 测试三个过滤器的功能，测试成功，功能正确
     * @param args
     */
    public static void main(String args[]){
        DocumentBuilder documentBuilder = new DocumentBuilder();
        String filePath = "D:\\HUST\\java\\实验1\\真实测试数据集\\testFilter.txt";
        File file = new File(filePath);
        AbstractDocument document = documentBuilder.build(0,filePath,file);
        System.out.println(document.getTuples());
    }
}