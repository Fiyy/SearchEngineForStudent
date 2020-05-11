package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     *  使用真实测试数据集和coronavirus单词测试，测试通过，结果和pdf文档一致
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        IndexSearcher indexSearcher = new IndexSearcher();
        indexSearcher.open("D:\\HUST\\java\\exp1\\index_save.txt");
        Term term = new Term("coronavirus");
        AbstractHit[] hits = indexSearcher.search(term, new SimpleSorter());
        for(AbstractHit hit:hits){
            System.out.println(hit);
        }
    }
}
