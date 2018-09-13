package com.alexandervanderzalm.game.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionCollection<A, R> implements IFunctionCollection<A, R>{

    public List<Function<A, R>> list = new ArrayList<>();

    @Override
    public List<R> Process(A arg){
        List<R>  result = new ArrayList<>();
        list.forEach((f) -> result.add(f.apply(arg)));
        return result;
    }

    @Override
    public void Add(Function<A, R> function) {
        list.add(function);
    }

    @Override
    public void Remove(Function<A, R> function) {
        list.remove(function);
    }
}
