/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: TermTuple
 * Author:   Administrator
 * Date:     2020/4/24 9:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号             描述
 */
package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

/**
 * 一个TermTuple对象为三元组(单词，出现频率，出现的当前位置).
 * 当解析一个文档时，每解析到一个单词，应该产生一个对应的三元组，其中freq始终为1(因为单词出现了一次).
 * @author Administrator
 * @create 2020/4/24
 * @since 1.0.0
 */
public class TermTuple extends AbstractTermTuple {

    /**
     *  缺省构造函数
     */
    public TermTuple(){}


    /**
     * 创建TermTuple
     * @param term
     * @param curPos
     */
    public TermTuple(AbstractTerm term, int curPos){
        this.term = term;
        this.curPos = curPos;
    }


    /**
     * 判断二个三元组内容是否相同
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof TermTuple){
            if(this.curPos == ((TermTuple) obj).curPos && this.freq == ((TermTuple) obj).freq && this.term.equals(((TermTuple) obj).term))
                return true;
            else return false;
        }
        else return false;
    }

    /**
     * 获得三元组的字符串表示
     * @return ： 三元组的字符串表示
     */
    @Override
    public String toString() {
        String string = new String();
        string += ("freq:" + this.freq + '\n');
        string += ("curPos: " + this.curPos);
        string += '\n';
        string += term.toString();
        return string;
    }
}