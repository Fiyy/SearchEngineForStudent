/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: PatternTermTupleFilter
 * Author:   Administrator
 * Date:     2020/4/24 9:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * 〈一句话功能简述〉<br> 
 * 〈基于正则表达式的过滤器〉
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {

    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple termTuple = input.next();
        while(termTuple != null && !termTuple.term.getContent().matches(Config.TERM_FILTER_PATTERN))
            termTuple = input.next();
        return termTuple;
    }
}