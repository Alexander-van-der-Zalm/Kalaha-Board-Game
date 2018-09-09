package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;

public interface IMethodCollection<T> {
    void Process(T argument);
    void Add(Method<T> method);
    void Remove(Method<T> method);
}