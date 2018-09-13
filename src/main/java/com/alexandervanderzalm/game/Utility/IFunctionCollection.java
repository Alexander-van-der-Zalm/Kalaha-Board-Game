package com.alexandervanderzalm.game.Utility;

import java.util.List;
import java.util.function.Function;

public interface IFunctionCollection<A, R> {
    List<R> Process(A argument);
    void Add(Function<A, R> method);
    void Remove(Function<A, R> method);
}

