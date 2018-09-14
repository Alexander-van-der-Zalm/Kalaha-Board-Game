package com.alexandervanderzalm.game.Model.Pits;

public interface IKalahaPit extends IObservedPit<Integer> {
    Boolean IsKalaha();
    void MakeKalaha();
    Integer GetPlayer();
    void SetPlayer(Integer player);
    PitData Data();
}
