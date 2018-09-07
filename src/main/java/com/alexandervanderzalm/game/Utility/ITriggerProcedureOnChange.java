package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;

public interface ITriggerProcedureOnChange {
    void TriggerOnChangedProcedures();
    void AddOnChangedProcedure(Procedure p);
    void RemoveOnChangedProcedure(Procedure p);
}