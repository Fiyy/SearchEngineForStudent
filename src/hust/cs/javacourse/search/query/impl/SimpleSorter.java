/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: SimpleSorter
 * Author:   Administrator
 * Date:     2020/4/29 10:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 简单的排序器，Sort接口的子类
 *
 * @author Administrator
 * @create 2020/4/29
 * @since 1.0.0
 */
public class SimpleSorter implements Sort {

    /**
     * 对命中结果集合根据文档得分排序
     *
     * @param hits ：命中结果集合
     */
    @Override
    public void sort(List<AbstractHit> hits) {
        Collections.sort(hits, Collections.reverseOrder());
    }

    /**
     * <pre>
     * 计算命中文档的得分, 作为命中结果排序的依据.
     *      计算文档的得分可以采取不同的策略, 因此这里的设计模式采用了策略模式，没有把这个方法放到AbstractHit及其子类里.
     *      而是放到接口Sort里，当我们需要不同的排序策略，只需要重新实现Sort的子类即可。即排序策略与被排序的对象(AbstractHit及其子类)应该分开。
     *      比如如果不排序，只需实现一个最简单的Sort接口实现类，比如叫NullSort类，在这个类里把所有文档的得分设置成一样的值。
     *      文档的得分值计算出来后要设置到AbstractHit子类对象里.
     *
     *      此处直接使用单词的出现频率作为分数参考
     * @param hit ：命中文档
     * @return ：命中文档的得分
     * </pre>
     */
    @Override
    public double score(AbstractHit hit) {
        Map<AbstractTerm, AbstractPosting> map = hit.getTermPostingMapping();
        double score = 0;
        for(AbstractTerm term: map.keySet()){
            score += map.get(term).getFreq();
        }
        return score;
    }
}