package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;

import java.util.PriorityQueue;

public class MethodSchedulerPriorityQueue<T> implements IMethodScheduler<T> {

    private PriorityQueue<SortedMethod<T>> queue = new PriorityQueue<SortedMethod<T>>();

    /**
     * Schedules a method to be processed later. Default priority of 0
     * @param method the method to be executed
     * @param priority Higher means first
     */
    @Override
    public void ScheduleMethod(Method<T> method) {
        queue.add(new SortedMethod<T>(method));
    }

    /**
     * Schedules a method to be processed later
     * @param method the method to be executed
     * @param priority Higher means first
     */
    @Override
    public void ScheduleMethod(Method<T> method, Integer priority) {
        queue.add(new SortedMethod<T>(method, priority));
    }

    @Override
    public void ProcessMethods(T argument) {
        queue.forEach((m)->m.method.Process(argument));
        queue.clear();
    }
}

class SortedMethod<T> implements Comparable<SortedMethod<T>>{
    Method<T> method;
    Integer Priority;

    public SortedMethod(Method<T> method) {
        this.method = method;
        this.Priority = 0;
    }

    public SortedMethod(Method<T> method, Integer priority) {
        this.method = method;
        Priority = priority;
    }

    // Higher priority is first
    @Override
    public int compareTo(SortedMethod<T> m) {
        return -1* Priority.compareTo(m.Priority);
    }
}
