/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: LengthTermTupleFilter
 * Author:   Administrator
 * Date:     2020/4/24 9:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * 〈一句话功能简述〉<br> 
 * 〈基于单词长度的过滤器〉
 *
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class LengthTermTupleFilter extends AbstractTermTupleFilter {

    /**
     * 构造函数
     * 长度过滤竟然还要将大写换为小写？？？发现每种过滤器都需要，因此在scanner阶段就更改
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */

    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        String s = new String();
        AbstractTermTuple termTuple = input.next();
        while(termTuple != null && ((termTuple.term.getContent()).length() > Config.TERM_FILTER_MAXLENGTH || termTuple.term.getContent().length() < Config.TERM_FILTER_MINLENGTH))
            termTuple = input.next();
        return termTuple;
    }
}