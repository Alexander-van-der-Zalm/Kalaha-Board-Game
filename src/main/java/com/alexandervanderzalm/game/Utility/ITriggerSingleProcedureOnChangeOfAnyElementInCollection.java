package com.alexandervanderzalm.game.Utility;

public interface ITriggerSingleProcedureOnChangeOfAnyElementInCollection<T> {
    void add(T item);
    void remove(T item);
    ITriggerProcedureOnChange OnChanged();
}
