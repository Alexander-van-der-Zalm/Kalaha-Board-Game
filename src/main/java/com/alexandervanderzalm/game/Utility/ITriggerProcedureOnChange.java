package com.alexandervanderzalm.game.Utility;

import java.util.ArrayList;
import java.util.List;

public interface ITriggerProcedureOnChange {
    void TriggerOnChangedProcedures();
    void AddOnChangedProcedure(Procedure p);
    void RemoveOnChangedProcedure(Procedure p);
}