package com.alexandervanderzalm.game.Model.Pits;

import com.alexandervanderzalm.game.Utility.MethodCollection;

public class ReactivePit implements IPit<Integer>{

    public MethodCollection<Integer> OnAdd = new MethodCollection<>();
    public MethodCollection<Integer> OnGrab = new MethodCollection<>();

    private KalahaPitData data;

    public ReactivePit(KalahaPitData data) {
        this.data = data;
    }

    public ReactivePit() {
        data = new KalahaPitData();
    }

    @Override
    public void Add(Integer stones) {
        data.stones += stones;
        OnAdd.Process(stones);
    }

    @Override
    public int Amount() {
        return data.stones;
    }

    @Override
    public Integer GrabAll() {
        Integer result = data.stones;
        data.stones = 0;
        OnGrab.Process(-result);
        return result;
    }

    public KalahaPitData Data(){
        return data;
    }
}
