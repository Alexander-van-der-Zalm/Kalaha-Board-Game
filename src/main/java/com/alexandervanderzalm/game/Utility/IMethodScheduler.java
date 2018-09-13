package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;

public interface IMethodScheduler<T> {
    void ScheduleMethod(Method<T> method);
    void ScheduleMethod(Method<T> method, Integer priority);
    void ProcessMethods(T argument);

    interface IFunctionCollection {
    }
}
