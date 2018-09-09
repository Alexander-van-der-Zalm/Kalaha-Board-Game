package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.IProcedureCollection;

public class ObservedPit extends Pit {

    // Helper for triggering procedures on change
    public IProcedureCollection OnChanged;

    public ObservedPit(IProcedureCollection onChangedHelper) {
        OnChanged = onChangedHelper;
    }

    public void Add(Integer stones){
        super.Add(stones);
        OnChanged.Process();
    }

    public Integer GrabAll() {
        Integer result = super.GrabAll();
        OnChanged.Process();
        return result;
    }
}
