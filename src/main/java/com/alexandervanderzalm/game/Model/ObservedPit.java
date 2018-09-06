package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.IObserveChange;
import com.alexandervanderzalm.game.Utility.OnChangedProcedureHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class ObservedPit extends Pit {

    public IObserveChange OnChangedHelper;// = new OnChangedProcedureHelper();

    public ObservedPit(IObserveChange onChangedHelper) {
        OnChangedHelper = onChangedHelper;
    }

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
