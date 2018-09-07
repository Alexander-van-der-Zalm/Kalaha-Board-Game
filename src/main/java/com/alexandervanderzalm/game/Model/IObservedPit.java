package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.ITriggerProcedureOnChange;

public interface IObservedPit<T> extends IPit<T>{
    ITriggerProcedureOnChange OnChanged();
}
