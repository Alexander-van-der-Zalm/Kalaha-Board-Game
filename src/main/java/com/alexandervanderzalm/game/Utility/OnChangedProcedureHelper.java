package com.alexandervanderzalm.game.Utility;

import java.util.ArrayList;
import java.util.List;

public class OnChangedProcedureHelper implements IObserveChange{

    private List<Procedure> onChangedProcedures = new ArrayList<Procedure>();

    @Override
    public void OnChanged() {
        onChangedProcedures.forEach((p) -> p.Process());
    }

    @Override
    public void AddOnChangedProcedure(Procedure p) {
        onChangedProcedures.add(p);
    }

    @Override
    public void RemoveOnChangedProcedure(Procedure p) {
        onChangedProcedures.remove(p);
    }
}
