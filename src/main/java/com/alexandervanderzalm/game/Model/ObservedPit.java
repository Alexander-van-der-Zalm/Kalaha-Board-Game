package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.ITriggerProcedureOnChange;

public class ObservedPit extends Pit {

    // Helper for triggering procedures on change
    public ITriggerProcedureOnChange OnChanged;

    public ObservedPit(ITriggerProcedureOnChange onChangedHelper) {
        OnChanged = onChangedHelper;
    }

    public void Add(Integer stones){
        super.Add(stones);
        OnChanged.TriggerOnChangedProcedures();
    }

    public Integer GrabAll() {
        Integer result = super.GrabAll();
        OnChanged.TriggerOnChangedProcedures();
        return result;
    }
}
