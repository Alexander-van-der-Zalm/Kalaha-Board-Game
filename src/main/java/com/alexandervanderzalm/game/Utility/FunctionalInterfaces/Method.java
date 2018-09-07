package com.alexandervanderzalm.game.Utility.FunctionalInterfaces;

@FunctionalInterface
public interface Method<T> {
    void Process(T argument);
}
