package com.alexandervanderzalm.game.Utility;

import com.alexandervanderzalm.game.Utility.FunctionalInterfaces.Procedure;

import java.util.ArrayList;
import java.util.List;

public class TriggerSingleProcedureOnChangeOfAnyElementInSingle<T extends IObserveChange> implements ITriggerSingleProcedureOnChangeOfAnyElementInCollection<T> {

    private List<T> collection = new ArrayList<T>();
    private Boolean isDirty = false;
    private IMethodScheduler<T> scheduler;
    private Procedure SetDirty = () -> isDirty = true;

    // TODO think about how to actually schedule the event and use the isDirty flag properly
    // I remember thinking along the lines of a custom OnChanged event
    // Schedule it to end of turn?
    // I want to mark a group and add a function
    public TriggerSingleProcedureOnChangeOfAnyElementInSingle(IMethodScheduler<T> MethodScheduler) {

    }

    @Override
    public void add(T item) {
        collection.add(item);
        item.OnChanged().AddProcedure(SetDirty);
    }

    @Override
    public void remove(T item) {
        collection.remove(item);
        item.OnChanged().RemoveProcedure(SetDirty);
    }

    @Override
    public ITriggerProcedureOnChange OnChanged() {
        return null;
    }
}
