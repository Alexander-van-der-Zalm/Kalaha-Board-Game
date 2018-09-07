package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;

public interface ITriggerProcedureOnChange {
    void TriggerOnChangedProcedures();
    void AddProcedure(Procedure p);
    void RemoveProcedure(Procedure p);
}