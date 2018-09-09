package com.alexandervanderzalm.game.Model;

import com.alexandervanderzalm.game.Utility.IProcedureCollection;

public class ObservedPit<T> implements IPit<T> {

    // Helper for triggering procedures on change
    //@JsonIgnore
    public IProcedureCollection OnChanged;
    private IPit<T> p;

    public ObservedPit(IProcedureCollection onChangedHelper, IPit<T> pit) {
        OnChanged = onChangedHelper;
        p = pit;
    }

    public void Add(T stones){
        p.Add(stones);
        OnChanged.Process();
    }

    @Override
    public int Amount() {
        return p.Amount();
    }

    @Override
    public T GrabAll() {
        T result = p.GrabAll();
        OnChanged.Process();
        return result;
    }
}
