package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Method;

import java.util.ArrayList;
import java.util.List;

public class MethodCollection<T> implements IMethodCollection<T> {

    List<Method<T>> methodList = new ArrayList<>();

    @Override
    public void Process(T argument) {
        methodList.forEach((m) -> m.Process(argument));
    }

    @Override
    public void Add(Method<T> method) {
        methodList.add(method);
    }

    @Override
    public void Remove(Method<T> method) {
        methodList.remove(method);
    }
}
