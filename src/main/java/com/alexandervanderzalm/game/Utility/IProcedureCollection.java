package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;

public interface IProcedureCollection {
    void Process();
    void Add(Procedure p);
    void Remove(Procedure p);
}