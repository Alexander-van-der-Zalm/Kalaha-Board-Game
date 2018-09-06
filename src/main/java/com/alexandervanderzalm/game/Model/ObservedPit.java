package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.OnChangedProcedureHelper;

public class ObservedPit extends Pit {
    public OnChangedProcedureHelper OnChangedHelper = new OnChangedProcedureHelper();

    public void Add(Integer stones){
        super.Add(stones);
        OnChangedHelper.OnChanged();
    }

    public Integer GrabAll() {
        Integer result = super.GrabAll();
        OnChangedHelper.OnChanged();
        return result;
    }
}
