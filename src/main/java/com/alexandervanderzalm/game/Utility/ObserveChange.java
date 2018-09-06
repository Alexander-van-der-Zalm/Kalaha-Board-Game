package com.alexandervanderzalm.game.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ObserveChange<T> {
    private List<Procedure> OnChanged = new ArrayList<Procedure>();
    private T observeThis;

    public T Get(){
        return observeThis;
    }

    public void Set(T observeChanges){
        T old = observeThis;
        observeThis = observeChanges;
        if(old != observeThis)
            OnChanged();
    }

    private void OnChanged() {
        if(OnChanged == null || OnChanged.isEmpty())
            return;

        for(Procedure f : OnChanged){
            f.Process();
        }
    }

    public void OnChangeAdd(Procedure f){
        OnChanged.add(f);
    }

    public void OnChangedRemove(Procedure f){
        OnChanged.remove(f);
    }


}
