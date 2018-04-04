package com.cc.android.zrouter.multiprocess;

/**
 * 是BaseApplicationLogic的包裹类
 */
public class PriorityLogicWrapper implements Comparable<PriorityLogicWrapper> {

    public int priority = 0;
    public Class<? extends BaseApplicationLogic> logicClass = null;
    public BaseApplicationLogic instance;

    public PriorityLogicWrapper(int priority, Class<? extends BaseApplicationLogic> logicClass) {
        this.priority = priority;
        this.logicClass = logicClass;
    }

    @Override
    public int compareTo(PriorityLogicWrapper o) {
        return o.priority - this.priority;
    }
}
