package com.alexandervanderzalm.game.Model.Pits;

import java.util.List;

public interface IPit<T> {
    public void Add(T stones);
    public int Amount();
    public T GrabAll();
}




// TODO Think about function hookups if needed
// Like the actions

